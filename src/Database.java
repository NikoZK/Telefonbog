import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/telefonbog"; // Your database name
    private static final String USER = "root";  // Change if necessary
    private static final String PASSWORD = "Nikiersej10!";  // Your MySQL password

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error connecting to the database");
        }
    }
}
