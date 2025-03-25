package hexaware.cars.util;

import java.io.IOException;

public class DBPropertyUtil {
    
    public static String getPropertyString(String propertyFileName) throws IOException {
        // Hardcoded connection string for testing
        return "jdbc:mysql://localhost:3306/cars?user=root&password=327748";
    }
}
