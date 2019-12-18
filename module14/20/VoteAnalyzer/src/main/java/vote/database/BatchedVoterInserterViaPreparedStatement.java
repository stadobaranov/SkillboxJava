package vote.database;

import vote.Voter;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BatchedVoterInserterViaPreparedStatement extends BatchedVoterInserter {
    private final Database database;
    private PreparedStatement statement;

    public BatchedVoterInserterViaPreparedStatement(Database database, int votersPerQuery) {
        super(votersPerQuery);
        this.database = database;
    }

    @Override
    protected void insertVoter(Voter voter) throws SQLException {
        if(statement == null) {
            statement = database.getConnection().prepareStatement(
                "INSERT INTO `voters`(`name`, `birthDay`) VALUES(?, ?) ON DUPLICATE KEY UPDATE `count` = `count` + 1"
            );
        }

        String birthDayString = DatabaseUtils.formatDate(voter.getBirthDay());

        statement.setString(1, voter.getName());
        statement.setString(2, birthDayString);
        statement.addBatch();
    }

    @Override
    protected void flushVoters() throws SQLException {
        statement.executeBatch();
    }

    @Override
    public void close() throws SQLException {
        super.close();

        if(statement != null) {
            try {
                statement.close();
            }
            catch(SQLException exception) {
                System.out.println("При закрытии стайтмента возникло исключение:");
                exception.printStackTrace(System.out);
            }

            statement = null;
        }
    }
}
