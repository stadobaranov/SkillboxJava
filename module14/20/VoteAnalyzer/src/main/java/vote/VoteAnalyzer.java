package vote;

import vote.database.BatchedVoterInserter;
import vote.database.BatchedVoterInserterViaPreparedStatement;
import vote.database.BatchedVoterInserterViaStringBuilder;
import vote.database.Database;
import vote.parsing.VoteParser;
import java.io.File;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

public class VoteAnalyzer {
    public static void main(String... args) throws SQLException {
        try(Database database = new Database("jdbc:mysql://192.168.99.100:3306/VoteAnalyzer", "root", "root")) {
            createTable(database);

            long startTime = System.nanoTime();

            database.transact(() -> {
                //try(BatchedVoterInserter inserter = new BatchedVoterInserterViaPreparedStatement(database, 100000)) {
                try(BatchedVoterInserter inserter = new BatchedVoterInserterViaStringBuilder(database, 100000)) {
                    VoteParser parser = new VoteParser(inserter);
                    parser.parse(new File("src/main/resources/data-1572M.xml"));
                }
            });

            System.out.printf("Вставка заняла %d мс", TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime));
        }
    }

    private static void createTable(Database database) throws SQLException {
        try(Statement statement = database.getConnection().createStatement()) {
            statement.execute(
                "DROP TABLE IF EXISTS `voters`"
            );

            statement.execute(
                "CREATE TABLE `voters`(" +
                    "`id` INT NOT NULL AUTO_INCREMENT," +
                    "`name` VARCHAR(255) NOT NULL," +
                    "`birthDay` DATE NOT NULL," +
                    "`count` INT NOT NULL DEFAULT 1," +
                    "PRIMARY KEY(id)," +
                    "UNIQUE KEY `unique`(`name`, `birthDay`)" +
                ") ENGINE=InnoDB"
            );
        }
    }
}
