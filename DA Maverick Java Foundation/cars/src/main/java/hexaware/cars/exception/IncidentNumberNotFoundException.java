package hexaware.cars.exception;

public class IncidentNumberNotFoundException extends Exception {
    public IncidentNumberNotFoundException() {
        super("Incident number not found in the database.");
    }

    public IncidentNumberNotFoundException(String message) {
        super(message);
    }
}
