package vote.database;

import database.Database;
import database.DatabaseUtils;
import vote.Voter;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BatchedVoterVisitRegistrarViaPreparedStatement extends BatchedVoterVisitRegistrar {
    private final Database database;
    private PreparedStatement statement;

    public BatchedVoterVisitRegistrarViaPreparedStatement(Database database, int batchThreshold) {
        super(batchThreshold);
        this.database = database;
    }

    @Override
    protected void registerVisit(Voter voter) throws SQLException {
        if(statement == null) {
            statement = database.getConnection().prepareStatement("INSERT INTO `voterVisits`(`name`, `birthDay`) VALUES(?, ?)");
        }

        String birthDayString = DatabaseUtils.formatDate(voter.getBirthDay());

        statement.setString(1, voter.getName());
        statement.setString(2, birthDayString);
        statement.addBatch();
    }

    @Override
    protected void flushVisits() throws SQLException {
        if(statement != null) {
            statement.executeBatch();
        }
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
