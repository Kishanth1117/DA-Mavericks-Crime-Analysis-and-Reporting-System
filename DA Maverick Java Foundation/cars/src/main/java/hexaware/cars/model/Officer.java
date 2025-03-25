package hexaware.cars.model;

public class Officer {
    private int officerID;
    private String firstName;
    private String lastName;
    private String badgeNumber;
    private String rank;
    private String phoneNumber;
    private int agencyID;

    // Default Constructor
    public Officer() {
    }

    // Parameterized Constructor
    public Officer(int officerID, String firstName, String lastName, String badgeNumber,
                  String rank, String phoneNumber, int agencyID) {
        this.officerID = officerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.badgeNumber = badgeNumber;
        this.rank = rank;
        this.phoneNumber = phoneNumber;
        this.agencyID = agencyID;
    }

    // Getters and Setters
    public int getOfficerID() {
        return officerID;
    }

    public void setOfficerID(int officerID) {
        this.officerID = officerID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBadgeNumber() {
        return badgeNumber;
    }

    public void setBadgeNumber(String badgeNumber) {
        this.badgeNumber = badgeNumber;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getAgencyID() {
        return agencyID;
    }

    public void setAgencyID(int agencyID) {
        this.agencyID = agencyID;
    }

    @Override
    public String toString() {
        return "Officer{" +
                "officerID=" + officerID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", badgeNumber='" + badgeNumber + '\'' +
                ", rank='" + rank + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", agencyID=" + agencyID +
                '}';
    }
}
