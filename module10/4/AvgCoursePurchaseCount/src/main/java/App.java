import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class App {
    private static final String DB_HOST = "localhost";
    private static final String DB_USER_NAME = "root";
    private static final String DB_USER_PASSWORD = "root";

    public static void main(String... args) throws SQLException {
        try(Connection connection = openConnection()) {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(
                "SELECT course_name AS name, COUNT(*) / MONTH(NOW()) as avg_purchase_count FROM PurchaseList GROUP BY name ORDER BY name"
            );

            while(resultSet.next()) {
                System.out.printf(
                    "%-40s - %6.2f%n",
                    resultSet.getString("name"),
                    resultSet.getDouble("avg_purchase_count")
                );
            }
        }
    }

    private static Connection openConnection() throws SQLException {
        return DriverManager.getConnection(
            String.format("jdbc:mysql://%s:3306/skillbox?serverTimezone=Europe/Moscow", DB_HOST),
            DB_USER_NAME,
            DB_USER_PASSWORD
        );
    }
}
