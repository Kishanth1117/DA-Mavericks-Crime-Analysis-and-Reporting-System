package hexaware.cars.exception;

public class DatabaseConnectionException extends Exception {
    public DatabaseConnectionException() {
        super("Failed to connect to the database.");
    }

    public DatabaseConnectionException(String message) {
        super(message);
    }
}
