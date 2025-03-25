package hexaware.cars.model;

import java.time.LocalDate;

public class Incident {
    private int incidentID;
    private IncidentType incidentType;
    private LocalDate incidentDate;
    private String location;
    private double latitude;
    private double longitude;
    private String description;
    private Status status;
    private int victimID;
    private int suspectID;

    // Default Constructor
    public Incident() {
    	super();
    }

    // Parameterized Constructor
    public Incident(int incidentID, IncidentType incidentType, LocalDate incidentDate, String location,
                   double latitude, double longitude, String description, Status status,
                   int victimID, int suspectID) {
        this.incidentID = incidentID;
        this.incidentType = incidentType;
        this.incidentDate = incidentDate;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.status = status;
        this.victimID = victimID;
        this.suspectID = suspectID;
    }

    // Getters and Setters
    public int getIncidentID() {
        return incidentID;
    }

    public void setIncidentID(int incidentID) {
        this.incidentID = incidentID;
    }

    public IncidentType getIncidentType() {
        return incidentType;
    }

    public void setIncidentType(IncidentType incidentType) {
        this.incidentType = incidentType;
    }

    public LocalDate getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(LocalDate incidentDate) {
        this.incidentDate = incidentDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getVictimID() {
        return victimID;
    }

    public void setVictimID(int victimID) {
        this.victimID = victimID;
    }

    public int getSuspectID() {
        return suspectID;
    }

    public void setSuspectID(int suspectID) {
        this.suspectID = suspectID;
    }

    @Override
    public String toString() {
        return "Incident{" +
                "incidentID=" + incidentID +
                ", incidentType=" + incidentType +
                ", incidentDate=" + incidentDate +
                ", location='" + location + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", victimID=" + victimID +
                ", suspectID=" + suspectID +
                '}';
    }
}
