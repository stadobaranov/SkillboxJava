package vote.database;

import vote.Voter;
import java.sql.SQLException;

public interface VoterVisitRegistrar {
    public abstract void register(Voter voter) throws SQLException;
}
