package database;

import java.sql.SQLException;

public interface DatabaseTransaction {
    public abstract void execute() throws SQLException;
}
