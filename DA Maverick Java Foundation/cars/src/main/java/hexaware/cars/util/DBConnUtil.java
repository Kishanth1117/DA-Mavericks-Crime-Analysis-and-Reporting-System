package hexaware.cars.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import hexaware.cars.exception.DatabaseConnectionException;

public class DBConnUtil {
    
    public static Connection getConnection(String connectionString) throws DatabaseConnectionException {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(connectionString);
        } catch (ClassNotFoundException | SQLException e) {
            throw new DatabaseConnectionException("Failed to establish database connection: " + e.getMessage());
        }
        return connection;
    }
}
