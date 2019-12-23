package vote.database;

import vote.Voter;
import java.sql.SQLException;

public abstract class BatchedVoterVisitRegistrar implements VoterVisitRegistrar, AutoCloseable {
    private final int batchThreshold;
    private int count;
    private boolean closed;

    public BatchedVoterVisitRegistrar(int batchThreshold) {
        this.batchThreshold = batchThreshold;
    }

    protected int getBatchThreshold() {
        return batchThreshold;
    }

    protected int getCount() {
        return count;
    }

    @Override
    public void register(Voter voter) throws SQLException {
        if(closed) {
            throw new IllegalStateException("Регистратор посещений избирателей закрыт");
        }

        registerVisit(voter);
        count++;

        if(count >= batchThreshold) {
            flush();
        }
    }

    protected abstract void registerVisit(Voter voter) throws SQLException;

    private void flush() throws SQLException {
        if(count > 0) {
            flushVisits();
            count = 0;
        }
    }

    protected abstract void flushVisits() throws SQLException;

    @Override
    public void close() throws SQLException {
        if(!closed) {
            flush();
            closed = true;
        }
    }
}
