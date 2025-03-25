package hexaware.cars.model;

import java.time.LocalDate;

public class Evidence {
    private int evidenceID;
    private int incidentID;
    private String description;
    private String locationFound;
    private LocalDate dateCollected;
    private String handlingOfficer;
    private String storageLocation;
    
    // Default constructor
    public Evidence() {
    }
    
    // Parameterized constructor
    public Evidence(int evidenceID, int incidentID, String description, String locationFound, 
                   LocalDate dateCollected, String handlingOfficer, String storageLocation) {
        this.evidenceID = evidenceID;
        this.incidentID = incidentID;
        this.description = description;
        this.locationFound = locationFound;
        this.dateCollected = dateCollected;
        this.handlingOfficer = handlingOfficer;
        this.storageLocation = storageLocation;
    }
    
    // Getters and setters
    public int getEvidenceID() {
        return evidenceID;
    }
    
    public void setEvidenceID(int evidenceID) {
        this.evidenceID = evidenceID;
    }
    
    public int getIncidentID() {
        return incidentID;
    }
    
    public void setIncidentID(int incidentID) {
        this.incidentID = incidentID;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getLocationFound() {
        return locationFound;
    }
    
    public void setLocationFound(String locationFound) {
        this.locationFound = locationFound;
    }
    
    public LocalDate getDateCollected() {
        return dateCollected;
    }
    
    public void setDateCollected(LocalDate dateCollected) {
        this.dateCollected = dateCollected;
    }
    
    public String getHandlingOfficer() {
        return handlingOfficer;
    }
    
    public void setHandlingOfficer(String handlingOfficer) {
        this.handlingOfficer = handlingOfficer;
    }
    
    public String getStorageLocation() {
        return storageLocation;
    }
    
    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }
    
    @Override
    public String toString() {
        return "Evidence [evidenceID=" + evidenceID + ", incidentID=" + incidentID + ", description=" + description
                + ", locationFound=" + locationFound + ", dateCollected=" + dateCollected + ", handlingOfficer="
                + handlingOfficer + ", storageLocation=" + storageLocation + "]";
    }
}
