package hexaware.cars.main;

import hexaware.cars.dao.*;
import hexaware.cars.model.*;
import hexaware.cars.exception.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.lang.Exception;

public class MainModule {
    private static ICrimeAnalysisService service;
    private static Scanner scanner;
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        try {
            // Initialize service and scanner
            service = new CrimeAnalysisServiceImpl();
            scanner = new Scanner(System.in);

            boolean exit = false;
            while (!exit) {
                displayMainMenu();
                int choice = getIntInput("Enter your choice: ");

                switch (choice) {
                    case 1:
                        handleIncidentManagement();
                        break;
                    case 2:
                        handleCaseManagement();
                        break;
                    case 3:
                        handleReportGeneration();
                        break;
                    case 4:
                        handleSearchOperations();
                        break;
                    case 5:
                        System.out.println("Thank you for using Crime Analysis and Reporting System. Goodbye!");
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n===== CRIME ANALYSIS AND REPORTING SYSTEM =====");
        System.out.println("1. Incident Management");
        System.out.println("2. Case Management");
        System.out.println("3. Report Generation");
        System.out.println("4. Search Operations");
        System.out.println("5. Exit");
        System.out.println("==============================================");
    }

    private static void handleIncidentManagement() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== INCIDENT MANAGEMENT =====");
            System.out.println("1. Create New Incident");
            System.out.println("2. Update Incident Status");
            System.out.println("3. View All Incidents");      // Added option
            System.out.println("4. View Incident Details");   // Added option
            System.out.println("5. Back to Main Menu");       // Changed number
            System.out.println("==============================");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    createNewIncident();
                    break;
                case 2:
                    updateIncidentStatus();
                    break;
                case 3:
                    viewAllIncidents();                      // Added function call
                    break;
                case 4:
                    viewIncidentDetails();                   // Added function call
                    break;
                case 5:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void handleCaseManagement() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== CASE MANAGEMENT =====");
            System.out.println("1. Create New Case");
            System.out.println("2. Update Case Details");
            System.out.println("3. View Case Details");
            System.out.println("4. View All Cases");
            System.out.println("5. Back to Main Menu");
            System.out.println("==========================");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    createNewCase();
                    break;
                case 2:
                    updateCaseDetails();
                    break;
                case 3:
                    viewCaseDetails();
                    break;
                case 4:
                    viewAllCases();
                    break;
                case 5:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void handleReportGeneration() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== REPORT GENERATION =====");
            System.out.println("1. Generate Incident Report");
            System.out.println("2. View All Reports");         // Added option
            System.out.println("3. View Report Details");      // Added option
            System.out.println("4. Back to Main Menu");        // Changed number
            System.out.println("===========================");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    generateIncidentReport();
                    break;
                case 2:
                    viewAllReports();                         // Added function call
                    break;
                case 3:
                    viewReportDetails();                      // Added function call
                    break;
                case 4:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void handleSearchOperations() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== SEARCH OPERATIONS =====");
            System.out.println("1. Search Incidents by Date Range");
            System.out.println("2. Search Incidents by Type");
            System.out.println("3. Back to Main Menu");
            System.out.println("============================");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    searchIncidentsByDateRange();
                    break;
                case 2:
                    searchIncidentsByType();
                    break;
                case 3:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Incident Management Methods
    private static void createNewIncident() {
        try {
            System.out.println("\n===== CREATE NEW INCIDENT =====");
            
            // Display available incident types
            System.out.println("Available Incident Types:");
            for (IncidentType type : IncidentType.values()) {
                System.out.println("- " + type);
            }
            
            // Get incident details
            IncidentType incidentType = getIncidentType();
            LocalDate incidentDate = getDateInput("Enter incident date (yyyy-MM-dd): ");
            String location = getStringInput("Enter location: ");
            double latitude = getDoubleInput("Enter latitude: ");
            double longitude = getDoubleInput("Enter longitude: ");
            String description = getStringInput("Enter description: ");
            
            // Display available statuses
            System.out.println("Available Statuses:");
            for (Status status : Status.values()) {
                System.out.println("- " + status);
            }
            
            Status status = getStatus();
            int victimID = getIntInput("Enter victim ID: ");
            int suspectID = getIntInput("Enter suspect ID: ");
            
            // Create incident object
            Incident incident = new Incident(0, incidentType, incidentDate, location, latitude, longitude, 
                                            description, status, victimID, suspectID);
            
            // Save incident
            boolean success = service.createIncident(incident);
            if (success) {
                System.out.println("Incident created successfully!");
            } else {
                System.out.println("Failed to create incident.");
            }
        } catch (InvalidDataException | DatabaseConnectionException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void updateIncidentStatus() {
        try {
            System.out.println("\n===== UPDATE INCIDENT STATUS =====");
            int incidentID = getIntInput("Enter incident ID: ");
            
            // Display available statuses
            System.out.println("Available Statuses:");
            for (Status status : Status.values()) {
                System.out.println("- " + status);
            }
            
            Status newStatus = getStatus();
            
            boolean success = service.updateIncidentStatus(newStatus, incidentID);
            if (success) {
                System.out.println("Incident status updated successfully!");
            } else {
                System.out.println("Failed to update incident status.");
            }
        } catch (IncidentNumberNotFoundException | DatabaseConnectionException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // New method to view all incidents
    private static void viewAllIncidents() {
        try {
            System.out.println("\n===== ALL INCIDENTS =====");
            List<Incident> allIncidents = service.getAllIncidents();
            
            if (allIncidents != null && !allIncidents.isEmpty()) {
                for (Incident incident : allIncidents) {
                    System.out.println("Incident ID: " + incident.getIncidentID());
                    System.out.println("Type: " + incident.getIncidentType());
                    System.out.println("Date: " + incident.getIncidentDate());
                    System.out.println("Location: " + incident.getLocation());
                    System.out.println("Status: " + incident.getStatus());
                    System.out.println("------------------------------");
                }
                System.out.println("Total incidents: " + allIncidents.size());
            } else {
                System.out.println("No incidents found.");
            }
        } catch (DatabaseConnectionException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // New method to view incident details
    private static void viewIncidentDetails() {
        try {
            System.out.println("\n===== VIEW INCIDENT DETAILS =====");
            int incidentID = getIntInput("Enter incident ID: ");
            
            Incident incident = service.getIncidentById(incidentID);
            
            System.out.println("\nIncident ID: " + incident.getIncidentID());
            System.out.println("Type: " + incident.getIncidentType());
            System.out.println("Date: " + incident.getIncidentDate());
            System.out.println("Location: " + incident.getLocation());
            System.out.println("Coordinates: " + incident.getLatitude() + ", " + incident.getLongitude());
            System.out.println("Description: " + incident.getDescription());
            System.out.println("Status: " + incident.getStatus());
            System.out.println("Victim ID: " + incident.getVictimID());
            System.out.println("Suspect ID: " + incident.getSuspectID());
        } catch (IncidentNumberNotFoundException | DatabaseConnectionException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Case Management Methods
    private static void createNewCase() {
        try {
            System.out.println("\n===== CREATE NEW CASE =====");
            String caseDescription = getStringInput("Enter case description: ");
            
            // Get incidents to associate with the case
            List<Incident> incidents = new ArrayList<>();
            boolean addMoreIncidents = true;
            
            while (addMoreIncidents) {
                int incidentID = getIntInput("Enter incident ID to add to the case: ");
                
                // In a real application, you would retrieve the incident from the database
                // For simplicity, we'll create a dummy incident with the provided ID
                Incident incident = new Incident();
                incident.setIncidentID(incidentID);
                incidents.add(incident);
                
                String addMore = getStringInput("Add another incident? (y/n): ");
                addMoreIncidents = addMore.equalsIgnoreCase("y");
            }
            
            Case newCase = service.createCase(caseDescription, incidents);
            System.out.println("Case created successfully with ID: " + newCase.getCaseID());
        } catch (InvalidDataException | DatabaseConnectionException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void updateCaseDetails() {
        try {
            System.out.println("\n===== UPDATE CASE DETAILS =====");
            int caseID = getIntInput("Enter case ID: ");
            
            // Retrieve existing case
            Case existingCase = service.getCaseDetails(caseID);
            
            // Update case description
            String newDescription = getStringInput("Enter new case description (leave empty to keep current): ");
            if (!newDescription.isEmpty()) {
                existingCase.setCaseDescription(newDescription);
            }
            
            // Update associated incidents
            String updateIncidents = getStringInput("Update associated incidents? (y/n): ");
            if (updateIncidents.equalsIgnoreCase("y")) {
                List<Incident> incidents = new ArrayList<>();
                boolean addMoreIncidents = true;
                
                while (addMoreIncidents) {
                    int incidentID = getIntInput("Enter incident ID to add to the case: ");
                    
                    // In a real application, you would retrieve the incident from the database
                    // For simplicity, we'll create a dummy incident with the provided ID
                    Incident incident = new Incident();
                    incident.setIncidentID(incidentID);
                    incidents.add(incident);
                    
                    String addMore = getStringInput("Add another incident? (y/n): ");
                    addMoreIncidents = addMore.equalsIgnoreCase("y");
                }
                
                existingCase.setIncidents(incidents);
            }
            
            boolean success = service.updateCaseDetails(existingCase);
            if (success) {
                System.out.println("Case details updated successfully!");
            } else {
                System.out.println("Failed to update case details.");
            }
        } catch (IncidentNumberNotFoundException | DatabaseConnectionException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void viewCaseDetails() {
        try {
            System.out.println("\n===== VIEW CASE DETAILS =====");
            int caseID = getIntInput("Enter case ID: ");
            
            Case caseDetails = service.getCaseDetails(caseID);
            System.out.println("\nCase ID: " + caseDetails.getCaseID());
            System.out.println("Description: " + caseDetails.getCaseDescription());
            System.out.println("Associated Incidents:");
            
            List<Incident> incidents = caseDetails.getIncidents();
            if (incidents != null && !incidents.isEmpty()) {
                for (Incident incident : incidents) {
                    System.out.println("- Incident ID: " + incident.getIncidentID());
                    System.out.println("  Type: " + incident.getIncidentType());
                    System.out.println("  Date: " + incident.getIncidentDate());
                    System.out.println("  Status: " + incident.getStatus());
                    System.out.println("  Description: " + incident.getDescription());
                    System.out.println();
                }
            } else {
                System.out.println("No incidents associated with this case.");
            }
        } catch (IncidentNumberNotFoundException | DatabaseConnectionException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void viewAllCases() {
        try {
            System.out.println("\n===== ALL CASES =====");
            List<Case> allCases = service.getAllCases();
            
            if (allCases != null && !allCases.isEmpty()) {
                for (Case caseObj : allCases) {
                    System.out.println("Case ID: " + caseObj.getCaseID());
                    System.out.println("Description: " + caseObj.getCaseDescription());
                    System.out.println("Number of Associated Incidents: " + 
                                      (caseObj.getIncidents() != null ? caseObj.getIncidents().size() : 0));
                    System.out.println("------------------------------");
                }
            } else {
                System.out.println("No cases found.");
            }
        } catch (DatabaseConnectionException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Report Generation Methods
    private static void generateIncidentReport() {
        try {
            System.out.println("\n===== GENERATE INCIDENT REPORT =====");
            int incidentID = getIntInput("Enter incident ID: ");
            
            // In a real application, you would retrieve the incident from the database
            // For simplicity, we'll create a dummy incident with the provided ID
            Incident incident = new Incident();
            incident.setIncidentID(incidentID);
            
            Report report = service.generateIncidentReport(incident);
            System.out.println("\nReport generated successfully!");
            System.out.println("Report ID: " + report.getReportID());
            System.out.println("Incident ID: " + report.getIncidentID());
            System.out.println("Reporting Officer ID: " + report.getReportingOfficerID());
            System.out.println("Report Date: " + report.getReportDate());
            System.out.println("Status: " + report.getStatus());
            System.out.println("Details: " + report.getReportDetails());
        } catch (IncidentNumberNotFoundException | DatabaseConnectionException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // New method to view all reports
    private static void viewAllReports() {
        try {
            System.out.println("\n===== ALL REPORTS =====");
            List<Report> allReports = service.getAllReports();
            
            if (allReports != null && !allReports.isEmpty()) {
                for (Report report : allReports) {
                    System.out.println("Report ID: " + report.getReportID());
                    System.out.println("Incident ID: " + report.getIncidentID());
                    System.out.println("Report Date: " + report.getReportDate());
                    System.out.println("Status: " + report.getStatus());
                    System.out.println("------------------------------");
                }
                System.out.println("Total reports: " + allReports.size());
            } else {
                System.out.println("No reports found.");
            }
        } catch (DatabaseConnectionException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // New method to view report details
    private static void viewReportDetails() {
        try {
            System.out.println("\n===== VIEW REPORT DETAILS =====");
            int reportID = getIntInput("Enter report ID: ");
            
            Report report = service.getReportById(reportID);
            
            System.out.println("\nReport ID: " + report.getReportID());
            System.out.println("Incident ID: " + report.getIncidentID());
            System.out.println("Reporting Officer ID: " + report.getReportingOfficerID());
            System.out.println("Report Date: " + report.getReportDate());
            System.out.println("Status: " + report.getStatus());
            System.out.println("Details: " + report.getReportDetails());
        } catch (ReportNotFoundException | DatabaseConnectionException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Search Operations Methods
    private static void searchIncidentsByDateRange() {
        try {
            System.out.println("\n===== SEARCH INCIDENTS BY DATE RANGE =====");
            LocalDate startDate = getDateInput("Enter start date (yyyy-MM-dd): ");
            LocalDate endDate = getDateInput("Enter end date (yyyy-MM-dd): ");
            
            List<Incident> incidents = service.getIncidentsInDateRange(startDate, endDate);
            displayIncidentsList(incidents);
        } catch (DatabaseConnectionException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void searchIncidentsByType() {
        try {
            System.out.println("\n===== SEARCH INCIDENTS BY TYPE =====");
            
            // Display available incident types
            System.out.println("Available Incident Types:");
            for (IncidentType type : IncidentType.values()) {
                System.out.println("- " + type);
            }
            
            IncidentType incidentType = getIncidentType();
            
            List<Incident> incidents = service.searchIncidents(incidentType);
            displayIncidentsList(incidents);
        } catch (DatabaseConnectionException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Helper Methods
    private static void displayIncidentsList(List<Incident> incidents) {
        if (incidents != null && !incidents.isEmpty()) {
            System.out.println("\nFound " + incidents.size() + " incidents:");
            for (Incident incident : incidents) {
                System.out.println("Incident ID: " + incident.getIncidentID());
                System.out.println("Type: " + incident.getIncidentType());
                System.out.println("Date: " + incident.getIncidentDate());
                System.out.println("Location: " + incident.getLocation());
                System.out.println("Status: " + incident.getStatus());
                System.out.println("Description: " + incident.getDescription());
                System.out.println("------------------------------");
            }
        } else {
            System.out.println("No incidents found matching the criteria.");
        }
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid number.");
            System.out.print(prompt);
            scanner.next();
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // Consume the newline
        return input;
    }

    private static double getDoubleInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.println("Please enter a valid number.");
            System.out.print(prompt);
            scanner.next();
        }
        double input = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline
        return input;
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static LocalDate getDateInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String dateStr = scanner.nextLine();
                return LocalDate.parse(dateStr, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd format.");
            }
        }
    }

    private static IncidentType getIncidentType() {
        while (true) {
            try {
                String typeStr = getStringInput("Enter incident type: ");
                return IncidentType.valueOf(typeStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid incident type. Please enter one of the available types.");
            }
        }
    }

    private static Status getStatus() {
        while (true) {
            try {
                String statusStr = getStringInput("Enter status: ");
                return Status.valueOf(statusStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status. Please enter one of the available statuses.");
            }
        }
    }
}
