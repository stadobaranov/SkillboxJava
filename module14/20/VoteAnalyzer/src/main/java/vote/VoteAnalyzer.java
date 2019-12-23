package vote;

import database.Database;
import vote.database.BatchedVoterVisitRegistrar;
import vote.database.BatchedVoterVisitRegistrarViaPreparedStatement;
import vote.database.BatchedVoterVisitRegistrarViaStringBuilder;
import vote.parsing.VoteParser;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class VoteAnalyzer {
    private static final int BATCH_THRESHOLD = 100000;

    private final Database database;
    private final Path filePath;
    private long parseAndRegisterVisitsTime;
    private long createVisitsTableIndexTime;
    private long fetchDuplicatedVisitsTime;
    private List<VoterVisitInfo> visitInfos;

    public VoteAnalyzer(Database database, Path filePath) {
        this.database = database;
        this.filePath = filePath;
    }

    public void analyze() throws SQLException {
        createVisitsTable();
        parseAndRegisterVisits();
        createVisitsTableIndex();
        fetchDuplicatedVisits();
        displayResult();
    }

    private void createVisitsTable() throws SQLException {
        try(Statement statement = database.getConnection().createStatement()) {
            statement.execute(
                "DROP TABLE IF EXISTS `voterVisits`"
            );

            statement.execute(
                "CREATE TABLE `voterVisits`(" +
                    "`id` INT NOT NULL AUTO_INCREMENT," +
                    "`name` VARCHAR(100) NOT NULL," +
                    "`birthDay` DATE NOT NULL," +
                    "PRIMARY KEY(`id`)" +
                ") ENGINE=InnoDB"
            );
        }
    }

    private void parseAndRegisterVisits() throws SQLException {
        long startTime = System.nanoTime();

//        try(BatchedVoterVisitRegistrar visitRegistrar = new BatchedVoterVisitRegistrarViaPreparedStatement(database, BATCH_THRESHOLD)) {
        try(BatchedVoterVisitRegistrar visitRegistrar = new BatchedVoterVisitRegistrarViaStringBuilder(database, BATCH_THRESHOLD)) {
            database.transact(() -> VoteParser.parse(filePath, visitRegistrar));
        }

        parseAndRegisterVisitsTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
    }

    private void createVisitsTableIndex() throws SQLException {
        long startTime = System.nanoTime();

        try(Statement statement = database.getConnection().createStatement()) {
            statement.execute("ALTER TABLE `voterVisits` ADD INDEX `name_birthDay`(`name`(100), `birthDay`)");
        }

        createVisitsTableIndexTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
    }

    private void fetchDuplicatedVisits() throws SQLException {
        long startTime = System.nanoTime();

        try(Statement statement = database.getConnection().createStatement()) {
            ResultSet rs = statement.executeQuery(
                "SELECT `name`, `birthDay`, COUNT(*) as `count` FROM `voterVisits` GROUP BY `name`, `birthDay` HAVING `count` > 1"
            );

            visitInfos = new ArrayList<>();

            while(rs.next()) {
                visitInfos.add(
                    new VoterVisitInfo(
                        new Voter(rs.getString("name"), rs.getDate("birthDay").toLocalDate()),
                        rs.getInt("count")
                    )
                );
            }
        }

        fetchDuplicatedVisitsTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
    }

    private void displayResult() {
        System.out.println("Список избирателей, посетивших выборы повторно:");

        for(VoterVisitInfo visitInfo: visitInfos) {
            System.out.printf("%s - %d раз(-а)%n", visitInfo.getVoter(), visitInfo.getVisitCount());
        }

        System.out.println();

        System.out.printf("Время парсинга файла и вставки посещения избирателей в таблицу - %d мс%n", parseAndRegisterVisitsTime);
        System.out.printf("Время добавления составного индекса по name и birthDay - %d мс%n", createVisitsTableIndexTime);
        System.out.printf("Время выборки дублей - %d мс%n", fetchDuplicatedVisitsTime);
    }

    public static void main(String... args) throws SQLException {
        try(Database database = new Database("jdbc:mysql://192.168.99.100:3306/VoteAnalyzer", "root", "root")) {
            VoteAnalyzer analyzer = new VoteAnalyzer(database, Paths.get("src/main/resources/data-1572M.xml"));
            analyzer.analyze();
        }
    }
}
