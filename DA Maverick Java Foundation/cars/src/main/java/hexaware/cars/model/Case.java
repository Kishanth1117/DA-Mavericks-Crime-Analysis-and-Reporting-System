package hexaware.cars.model;

import java.util.List;

public class Case {
    private int caseID;
    private String caseDescription;
    private List<Incident> incidents;

    // Default Constructor
    public Case() {
    }

    // Parameterized Constructor
    public Case(int caseID, String caseDescription, List<Incident> incidents) {
        this.caseID = caseID;
        this.caseDescription = caseDescription;
        this.incidents = incidents;
    }

    // Getters and Setters
    public int getCaseID() {
        return caseID;
    }

    public void setCaseID(int caseID) {
        this.caseID = caseID;
    }

    public String getCaseDescription() {
        return caseDescription;
    }

    public void setCaseDescription(String caseDescription) {
        this.caseDescription = caseDescription;
    }

    public List<Incident> getIncidents() {
        return incidents;
    }

    public void setIncidents(List<Incident> incidents) {
        this.incidents = incidents;
    }

    @Override
    public String toString() {
        return "Case{" +
                "caseID=" + caseID +
                ", caseDescription='" + caseDescription + '\'' +
                ", incidents=" + incidents +
                '}';
    }
}
