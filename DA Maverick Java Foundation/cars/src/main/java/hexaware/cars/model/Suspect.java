package hexaware.cars.model;

import java.time.LocalDate;

public class Suspect {
    private int suspectID;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private String phoneNumber;
    private String criminalHistory;
    
    // Default constructor
    public Suspect() {
    }
    
    // Parameterized constructor
    public Suspect(int suspectID, String firstName, String lastName, LocalDate dateOfBirth, 
                  String gender, String address, String phoneNumber, String criminalHistory) {
        this.suspectID = suspectID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.criminalHistory = criminalHistory;
    }
    
    // Getters and setters
    public int getSuspectID() {
        return suspectID;
    }
    
    public void setSuspectID(int suspectID) {
        this.suspectID = suspectID;
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
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getCriminalHistory() {
        return criminalHistory;
    }
    
    public void setCriminalHistory(String criminalHistory) {
        this.criminalHistory = criminalHistory;
    }
    
    @Override
    public String toString() {
        return "Suspect [suspectID=" + suspectID + ", firstName=" + firstName + ", lastName=" + lastName + ", dateOfBirth="
                + dateOfBirth + ", gender=" + gender + ", address=" + address + ", phoneNumber=" + phoneNumber
                + ", criminalHistory=" + criminalHistory + "]";
    }
}
