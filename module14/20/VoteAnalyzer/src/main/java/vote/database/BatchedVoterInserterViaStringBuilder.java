package vote.database;

import vote.Voter;
import java.sql.SQLException;
import java.sql.Statement;

public class BatchedVoterInserterViaStringBuilder extends BatchedVoterInserter {
    private final StringBuilder valuesBuilder = new StringBuilder();
    private final Database database;

    public BatchedVoterInserterViaStringBuilder(Database database, int votersPerQuery) {
        super(votersPerQuery);
        this.database = database;
    }

    @Override
    protected void insertVoter(Voter voter) throws SQLException {
        if(getVotersCount() > 0) {
            valuesBuilder.append(',');
        }

        String birthDayString = DatabaseUtils.formatDate(voter.getBirthDay());

        valuesBuilder.append("('");
        valuesBuilder.append(/*escapeString(*/voter.getName()/*)*/);
        valuesBuilder.append("','");
        valuesBuilder.append(birthDayString);
        valuesBuilder.append("')");
    }

    @Override
    protected void flushVoters() throws SQLException {
        try(Statement statement = database.getConnection().createStatement()) {
            statement.execute(
                String.format(
                    "INSERT INTO `voters`(`name`, `birthDay`) VALUES%s ON DUPLICATE KEY UPDATE `count` = `count` + 1",
                    valuesBuilder.toString()
                )
            );
        }

        valuesBuilder.setLength(0);
    }
}
