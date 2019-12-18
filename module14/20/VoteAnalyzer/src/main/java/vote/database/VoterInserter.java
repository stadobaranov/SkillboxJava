package vote.database;

import vote.Voter;
import java.sql.SQLException;

public interface VoterInserter {
    public abstract void insert(Voter voter) throws SQLException;
}
