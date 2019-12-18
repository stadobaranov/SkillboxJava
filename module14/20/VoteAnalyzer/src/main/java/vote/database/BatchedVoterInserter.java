package vote.database;

import vote.Voter;
import java.sql.SQLException;

public abstract class BatchedVoterInserter implements VoterInserter, AutoCloseable {
    private final int votersPerQuery;
    private int votersCount;
    private boolean closed;

    public BatchedVoterInserter(int votersPerQuery) {
        this.votersPerQuery = votersPerQuery;
    }

    protected int getVotersCount() {
        return votersCount;
    }

    @Override
    public void insert(Voter voter) throws SQLException {
        if(closed) {
            throw new IllegalStateException("Объект пакетной вставки избирателей в БД закрыт");
        }

        insertVoter(voter);
        votersCount++;

        if(votersCount >= votersPerQuery) {
            flush();
        }
    }

    protected abstract void insertVoter(Voter voter) throws SQLException;

    private void flush() throws SQLException {
        if(votersCount > 0) {
            flushVoters();
            votersCount = 0;
        }
    }

    protected abstract void flushVoters() throws SQLException;

    @Override
    public void close() throws SQLException {
        if(!closed) {
            flush();
            closed = true;
        }
    }
}
