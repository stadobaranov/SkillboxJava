package vote.database;

import database.Database;
import database.DatabaseUtils;
import vote.Voter;
import java.sql.SQLException;
import java.sql.Statement;

public class BatchedVoterVisitRegistrarViaStringBuilder extends BatchedVoterVisitRegistrar {
    private final StringBuilder valuesBuilder = new StringBuilder();
    private final Database database;

    public BatchedVoterVisitRegistrarViaStringBuilder(Database database, int batchThreshold) {
        super(batchThreshold);
        this.database = database;
    }

    @Override
    protected void registerVisit(Voter voter) {
        if(getCount() > 0) {
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
    protected void flushVisits() throws SQLException {
        try(Statement statement = database.getConnection().createStatement()) {
            statement.execute("INSERT INTO `voterVisits`(`name`, `birthDay`) VALUES" + valuesBuilder.toString());
        }

        valuesBuilder.setLength(0);
    }
}
