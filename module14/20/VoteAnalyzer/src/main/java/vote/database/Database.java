package vote.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database implements AutoCloseable {
    private final String url;
    private final String user;
    private final String password;
    private Connection connection;
    private boolean closed;

    public Database(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        if(closed) {
            throw new IllegalStateException("Соединение с базой данных уже закрыто");
        }

        if(connection == null) {
            connection = DriverManager.getConnection(url, user, password);
        }

        return connection;
    }

    public void transact(DatabaseTransaction transaction) throws SQLException {
        Connection connection = getConnection();

        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);

        try {
            transaction.execute();
            connection.commit();
        }
        catch(Exception exception) {
            connection.rollback();
            throw exception;
        }
        finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public void close() {
        if(!closed) {
            if(connection != null) {
                try {
                    connection.close();
                }
                catch(SQLException exception) {
                    System.out.println("При закрытии соединения с БД возникло исключение:");
                    exception.printStackTrace(System.out);
                }

                connection = null;
            }

            closed = true;
        }
    }
}
