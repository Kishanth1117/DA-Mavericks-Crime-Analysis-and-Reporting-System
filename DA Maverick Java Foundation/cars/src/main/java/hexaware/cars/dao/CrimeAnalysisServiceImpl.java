package hexaware.cars.dao;

import hexaware.cars.model.*;
import hexaware.cars.exception.*;
import hexaware.cars.util.DBConnUtil;
import hexaware.cars.util.DBPropertyUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CrimeAnalysisServiceImpl implements ICrimeAnalysisService {
    private Connection connection;
    
    public CrimeAnalysisServiceImpl() {
        try {
            String connectionString = DBPropertyUtil.getPropertyString("db.properties");
            connection = DBConnUtil.getConnection(connectionString);
            
            // Print schema for debugging
            // printTableSchema("incidents");
            // printTableSchema("cases");
            // printTableSchema("reports");
        } catch (IOException | DatabaseConnectionException e) {
            System.err.println("Error initializing database connection: " + e.getMessage());
        }
    }
    
    // Debug method to print table schema
    private void printTableSchema(String tableName) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + " LIMIT 1");
            java.sql.ResultSetMetaData metaData = rs.getMetaData();
            
            System.out.println("Table " + tableName + " schema:");
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                System.out.println("Column " + i + ": " + metaData.getColumnName(i) + 
                                  " (" + metaData.getColumnTypeName(i) + ")");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving schema for " + tableName + ": " + e.getMessage());
        }
    }
    
    // Helper method to check if a column exists in a table
    private boolean checkIfColumnExists(String tableName, String columnName) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("DESCRIBE " + tableName);
            while (rs.next()) {
                if (rs.getString("Field").equalsIgnoreCase(columnName)) {
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            System.out.println("Warning: Could not check for column " + columnName + ": " + e.getMessage());
            return false;
        }
    }
    
    // Helper method to add a column to a table
    private boolean addColumnToTable(String tableName, String columnName, String columnType) {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnType);
            System.out.println("Added " + columnName + " column to " + tableName + " table.");
            return true;
        } catch (SQLException e) {
            System.out.println("Warning: Could not add " + columnName + " column: " + e.getMessage());
            return false;
        }
    }
    
    // Incident Management Methods
    @Override
    public boolean createIncident(Incident incident) throws InvalidDataException, DatabaseConnectionException {
        if (incident == null) {
            throw new InvalidDataException("Incident cannot be null");
        }
        
        String sql = "INSERT INTO incidents (IncidentType, IncidentDate, Location, Latitude, Longitude, " +
                     "Description, Status, VictimID, SuspectID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, incident.getIncidentType().toString());
            pstmt.setDate(2, java.sql.Date.valueOf(incident.getIncidentDate()));
            pstmt.setString(3, incident.getLocation());
            pstmt.setDouble(4, incident.getLatitude());
            pstmt.setDouble(5, incident.getLongitude());
            pstmt.setString(6, incident.getDescription());
            pstmt.setString(7, incident.getStatus().toString());
            pstmt.setInt(8, incident.getVictimID());
            pstmt.setInt(9, incident.getSuspectID());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    incident.setIncidentID(rs.getInt(1));
                }
                return true;
            }
            
            return false;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error creating incident: " + e.getMessage());
        }
    }
    
    @Override
    public boolean updateIncidentStatus(Status newStatus, int incidentID) throws IncidentNumberNotFoundException, DatabaseConnectionException {
        String sql = "UPDATE incidents SET Status = ? WHERE IncidentID = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newStatus.toString());
            pstmt.setInt(2, incidentID);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new IncidentNumberNotFoundException("Incident with ID " + incidentID + " not found");
            }
            
            return true;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error updating incident status: " + e.getMessage());
        }
    }
    
    @Override
    public Incident getIncidentById(int incidentID) throws IncidentNumberNotFoundException, DatabaseConnectionException {
        String sql = "SELECT * FROM incidents WHERE IncidentID = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, incidentID);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToIncident(rs);
            } else {
                throw new IncidentNumberNotFoundException("Incident with ID " + incidentID + " not found");
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error retrieving incident: " + e.getMessage());
        }
    }
    
    @Override
    public List<Incident> getAllIncidents() throws DatabaseConnectionException {
        String sql = "SELECT * FROM incidents";
        List<Incident> incidents = new ArrayList<>();
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                incidents.add(mapResultSetToIncident(rs));
            }
            
            return incidents;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error retrieving incidents: " + e.getMessage());
        }
    }
    
    // Case Management Methods
    @Override
    public Case createCase(String caseDescription, List<Incident> incidents) throws InvalidDataException, DatabaseConnectionException {
        if (caseDescription == null || caseDescription.trim().isEmpty()) {
            throw new InvalidDataException("Case description cannot be empty");
        }
        
        // First, check if we have the IncidentIDs column in the cases table
        boolean hasIncidentIDsColumn = checkIfColumnExists("cases", "IncidentIDs");
        
        // If the column doesn't exist, try to add it
        if (!hasIncidentIDsColumn) {
            hasIncidentIDsColumn = addColumnToTable("cases", "IncidentIDs", "VARCHAR(255)");
        }
        
        String caseSql;
        if (hasIncidentIDsColumn) {
            caseSql = "INSERT INTO cases (CaseDescription, IncidentIDs) VALUES (?, ?)";
        } else {
            caseSql = "INSERT INTO cases (CaseDescription) VALUES (?)";
        }
        
        try {
            connection.setAutoCommit(false);
            
            // First, validate that all incidents exist
            List<Integer> validIncidentIDs = new ArrayList<>();
            if (incidents != null && !incidents.isEmpty()) {
                for (Incident incident : incidents) {
                    int incidentID = incident.getIncidentID();
                    
                    // Check if incident exists
                    String checkSql = "SELECT COUNT(*) FROM incidents WHERE IncidentID = ?";
                    PreparedStatement checkPstmt = connection.prepareStatement(checkSql);
                    checkPstmt.setInt(1, incidentID);
                    ResultSet checkRs = checkPstmt.executeQuery();
                    
                    if (checkRs.next() && checkRs.getInt(1) > 0) {
                        // Incident exists, add its ID to the list
                        validIncidentIDs.add(incidentID);
                    } else {
                        System.out.println("Warning: Incident with ID " + incidentID + " not found. Skipping association.");
                    }
                }
            }
            
            // Insert case
            PreparedStatement casePstmt = connection.prepareStatement(caseSql, Statement.RETURN_GENERATED_KEYS);
            casePstmt.setString(1, caseDescription);
            
            if (hasIncidentIDsColumn) {
                // Build comma-separated list of incident IDs
                StringBuilder incidentIDsStr = new StringBuilder();
                for (int i = 0; i < validIncidentIDs.size(); i++) {
                    if (i > 0) {
                        incidentIDsStr.append(",");
                    }
                    incidentIDsStr.append(validIncidentIDs.get(i));
                }
                casePstmt.setString(2, incidentIDsStr.toString());
            }
            
            casePstmt.executeUpdate();
            
            ResultSet rs = casePstmt.getGeneratedKeys();
            if (!rs.next()) {
                connection.rollback();
                throw new DatabaseConnectionException("Failed to create case, no ID obtained.");
            }
            
            int caseID = rs.getInt(1);
            
            connection.commit();
            
            // Create a list of Incident objects from the valid IDs
            List<Incident> validIncidents = new ArrayList<>();
            for (Integer id : validIncidentIDs) {
                try {
                    Incident incident = getIncidentById(id);
                    validIncidents.add(incident);
                } catch (IncidentNumberNotFoundException e) {
                    // This shouldn't happen since we just validated the IDs
                    System.out.println("Warning: Incident with ID " + id + " not found after validation.");
                }
            }
            
            Case newCase = new Case();
            newCase.setCaseID(caseID);
            newCase.setCaseDescription(caseDescription);
            newCase.setIncidents(validIncidents);
            
            return newCase;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                // Log rollback error
            }
            throw new DatabaseConnectionException("Error creating case: " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                // Log auto-commit reset error
            }
        }
    }
    
    @Override
    public boolean updateCaseDetails(Case caseObj) throws IncidentNumberNotFoundException, DatabaseConnectionException {
        if (caseObj == null) {
            throw new IncidentNumberNotFoundException("Case cannot be null");
        }
        
        // Check if the cases table has an IncidentIDs column
        boolean hasIncidentIDsColumn = checkIfColumnExists("cases", "IncidentIDs");
        
        String caseSql;
        if (hasIncidentIDsColumn) {
            caseSql = "UPDATE cases SET CaseDescription = ?, IncidentIDs = ? WHERE CaseID = ?";
        } else {
            caseSql = "UPDATE cases SET CaseDescription = ? WHERE CaseID = ?";
        }
        
        try {
            connection.setAutoCommit(false);
            
            // Validate that all incidents exist
            List<Integer> validIncidentIDs = new ArrayList<>();
            List<Incident> incidents = caseObj.getIncidents();
            
            if (incidents != null && !incidents.isEmpty()) {
                for (Incident incident : incidents) {
                    int incidentID = incident.getIncidentID();
                    
                    // Check if incident exists
                    String checkSql = "SELECT COUNT(*) FROM incidents WHERE IncidentID = ?";
                    PreparedStatement checkPstmt = connection.prepareStatement(checkSql);
                    checkPstmt.setInt(1, incidentID);
                    ResultSet checkRs = checkPstmt.executeQuery();
                    
                    if (checkRs.next() && checkRs.getInt(1) > 0) {
                        // Incident exists, add its ID to the list
                        validIncidentIDs.add(incidentID);
                    } else {
                        System.out.println("Warning: Incident with ID " + incidentID + " not found. Skipping association.");
                    }
                }
            }
            
            // Update case description
            PreparedStatement casePstmt = connection.prepareStatement(caseSql);
            casePstmt.setString(1, caseObj.getCaseDescription());
            
            if (hasIncidentIDsColumn) {
                // Build comma-separated list of incident IDs
                StringBuilder incidentIDsStr = new StringBuilder();
                for (int i = 0; i < validIncidentIDs.size(); i++) {
                    if (i > 0) {
                        incidentIDsStr.append(",");
                    }
                    incidentIDsStr.append(validIncidentIDs.get(i));
                }
                casePstmt.setString(2, incidentIDsStr.toString());
                casePstmt.setInt(3, caseObj.getCaseID());
            } else {
                casePstmt.setInt(2, caseObj.getCaseID());
            }
            
            int rowsAffected = casePstmt.executeUpdate();
            
            if (rowsAffected == 0) {
                connection.rollback();
                throw new IncidentNumberNotFoundException("Case with ID " + caseObj.getCaseID() + " not found");
            }
            
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                // Log rollback error
            }
            throw new DatabaseConnectionException("Error updating case: " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                // Log auto-commit reset error
            }
        }
    }
    
    @Override
    public Case getCaseDetails(int caseID) throws IncidentNumberNotFoundException, DatabaseConnectionException {
        String caseSql = "SELECT * FROM cases WHERE CaseID = ?";
        
        try {
            // Get case details
            PreparedStatement casePstmt = connection.prepareStatement(caseSql);
            casePstmt.setInt(1, caseID);
            
            ResultSet caseRs = casePstmt.executeQuery();
            
            if (!caseRs.next()) {
                throw new IncidentNumberNotFoundException("Case with ID " + caseID + " not found");
            }
            
            Case caseObj = new Case();
            caseObj.setCaseID(caseRs.getInt("CaseID"));
            caseObj.setCaseDescription(caseRs.getString("CaseDescription"));
            
            // Check if the cases table has an IncidentIDs column
            boolean hasIncidentIDsColumn = checkIfColumnExists("cases", "IncidentIDs");
            List<Incident> incidents = new ArrayList<>();
            
            if (hasIncidentIDsColumn) {
                // Get incident IDs from the IncidentIDs column
                String incidentIdsStr = caseRs.getString("IncidentIDs");
                
                if (incidentIdsStr != null && !incidentIdsStr.isEmpty()) {
                    String[] incidentIds = incidentIdsStr.split(",");
                    
                    for (String idStr : incidentIds) {
                        try {
                            int incidentID = Integer.parseInt(idStr.trim());
                            try {
                                Incident incident = getIncidentById(incidentID);
                                incidents.add(incident);
                            } catch (IncidentNumberNotFoundException e) {
                                // Log that incident was not found
                                System.out.println("Warning: Incident with ID " + incidentID + " not found");
                            }
                        } catch (NumberFormatException e) {
                            // Log invalid incident ID
                            System.out.println("Warning: Invalid incident ID format: " + idStr);
                        }
                    }
                }
            }
            
            caseObj.setIncidents(incidents);
            return caseObj;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error retrieving case details: " + e.getMessage());
        }
    }
    
    @Override
    public List<Case> getAllCases() throws DatabaseConnectionException {
        String sql = "SELECT * FROM cases";
        List<Case> cases = new ArrayList<>();
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Case caseObj = new Case();
                caseObj.setCaseID(rs.getInt("CaseID"));
                caseObj.setCaseDescription(rs.getString("CaseDescription"));
                
                // Note: For efficiency, we're not loading all incidents for each case in the list view
                cases.add(caseObj);
            }
            
            return cases;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error retrieving cases: " + e.getMessage());
        }
    }
    
    // Report Generation Methods
    @Override
    public Report generateIncidentReport(Incident incident) throws IncidentNumberNotFoundException, DatabaseConnectionException {
        if (incident == null) {
            throw new IncidentNumberNotFoundException("Incident cannot be null");
        }
        
        // Verify incident exists
        try {
            getIncidentById(incident.getIncidentID());
        } catch (IncidentNumberNotFoundException e) {
            throw e;
        }
        
        // Check the reports table structure
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("DESCRIBE reports");
            
            boolean hasReportingOfficerID = false;
            boolean hasReportingOfficeID = false;
            
            while (rs.next()) {
                String columnName = rs.getString("Field");
                if ("ReportingOfficerID".equalsIgnoreCase(columnName)) {
                    hasReportingOfficerID = true;
                } else if ("ReportingOfficeID".equalsIgnoreCase(columnName)) {
                    hasReportingOfficeID = true;
                }
            }
            
            String officerColumnName;
            if (hasReportingOfficerID) {
                officerColumnName = "ReportingOfficerID";
            } else if (hasReportingOfficeID) {
                officerColumnName = "ReportingOfficeID";
            } else {
                // Add the column if it doesn't exist
                stmt.executeUpdate("ALTER TABLE reports ADD COLUMN ReportingOfficerID INT");
                officerColumnName = "ReportingOfficerID";
            }
            
            String sql = "INSERT INTO reports (IncidentID, " + officerColumnName + ", ReportDate, Status, ReportDetails) " +
                         "VALUES (?, ?, ?, ?, ?)";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                int reportingOfficerID = 1; // Assuming a default officer ID for simplicity
                LocalDate reportDate = LocalDate.now();
                String reportStatus = "DRAFT"; // Changed from Status enum to String
                String reportDetails = "Report for incident #" + incident.getIncidentID() + " generated on " + reportDate;
                
                pstmt.setInt(1, incident.getIncidentID());
                pstmt.setInt(2, reportingOfficerID);
                pstmt.setDate(3, java.sql.Date.valueOf(reportDate));
                pstmt.setString(4, reportStatus);
                pstmt.setString(5, reportDetails);
                
                pstmt.executeUpdate();
                
                ResultSet genKeys = pstmt.getGeneratedKeys();
                if (genKeys.next()) {
                    int reportID = genKeys.getInt(1);
                    
                    Report report = new Report();
                    report.setReportID(reportID);
                    report.setIncidentID(incident.getIncidentID());
                    report.setReportingOfficerID(reportingOfficerID);
                    report.setReportDate(reportDate);
                    report.setStatus(reportStatus); // Changed from Status enum to String
                    report.setReportDetails(reportDetails);
                    
                    return report;
                } else {
                    throw new DatabaseConnectionException("Failed to generate report, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error generating report: " + e.getMessage());
        }
    }
    
    @Override
    public Report getReportById(int reportID) throws ReportNotFoundException, DatabaseConnectionException {
        // First, check the column name for reporting officer
        String officerColumnName = getReportingOfficerColumnName();
        
        String sql = "SELECT * FROM reports WHERE ReportID = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, reportID);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Report report = new Report();
                report.setReportID(rs.getInt("ReportID"));
                report.setIncidentID(rs.getInt("IncidentID"));
                report.setReportingOfficerID(rs.getInt(officerColumnName));
                report.setReportDate(rs.getDate("ReportDate").toLocalDate());
                report.setStatus(rs.getString("Status")); // Changed from Status.valueOf() to getString()
                report.setReportDetails(rs.getString("ReportDetails"));
                
                return report;
            } else {
                throw new ReportNotFoundException("Report with ID " + reportID + " not found");
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error retrieving report: " + e.getMessage());
        }
    }
    
    @Override
    public List<Report> getAllReports() throws DatabaseConnectionException {
        // First, check the column name for reporting officer
        String officerColumnName = getReportingOfficerColumnName();
        
        String sql = "SELECT * FROM reports";
        List<Report> reports = new ArrayList<>();
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Report report = new Report();
                report.setReportID(rs.getInt("ReportID"));
                report.setIncidentID(rs.getInt("IncidentID"));
                report.setReportingOfficerID(rs.getInt(officerColumnName));
                report.setReportDate(rs.getDate("ReportDate").toLocalDate());
                report.setStatus(rs.getString("Status")); // Changed from Status.valueOf() to getString()
                report.setReportDetails(rs.getString("ReportDetails"));
                
                reports.add(report);
            }
            
            return reports;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error retrieving reports: " + e.getMessage());
        }
    }
    
    // Helper method to get the correct column name for reporting officer
    private String getReportingOfficerColumnName() throws DatabaseConnectionException {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("DESCRIBE reports");
            
            boolean hasReportingOfficerID = false;
            boolean hasReportingOfficeID = false;
            
            while (rs.next()) {
                String columnName = rs.getString("Field");
                if ("ReportingOfficerID".equalsIgnoreCase(columnName)) {
                    hasReportingOfficerID = true;
                } else if ("ReportingOfficeID".equalsIgnoreCase(columnName)) {
                    hasReportingOfficeID = true;
                }
            }
            
            if (hasReportingOfficerID) {
                return "ReportingOfficerID";
            } else if (hasReportingOfficeID) {
                return "ReportingOfficeID";
            } else {
                // Add the column if it doesn't exist
                stmt.executeUpdate("ALTER TABLE reports ADD COLUMN ReportingOfficerID INT");
                return "ReportingOfficerID";
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error determining reporting officer column name: " + e.getMessage());
        }
    }
    
    // Search Operations Methods
    @Override
    public List<Incident> getIncidentsInDateRange(LocalDate startDate, LocalDate endDate) throws DatabaseConnectionException {
        String sql = "SELECT * FROM incidents WHERE IncidentDate BETWEEN ? AND ?";
        List<Incident> incidents = new ArrayList<>();
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDate(1, java.sql.Date.valueOf(startDate));
            pstmt.setDate(2, java.sql.Date.valueOf(endDate));
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                incidents.add(mapResultSetToIncident(rs));
            }
            
            return incidents;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error searching incidents by date range: " + e.getMessage());
        }
    }
    
    @Override
    public List<Incident> searchIncidents(IncidentType incidentType) throws DatabaseConnectionException {
        String sql = "SELECT * FROM incidents WHERE IncidentType = ?";
        List<Incident> incidents = new ArrayList<>();
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, incidentType.toString());
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                incidents.add(mapResultSetToIncident(rs));
            }
            
            return incidents;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error searching incidents by type: " + e.getMessage());
        }
    }
    
    // Helper method to map ResultSet to Incident
    private Incident mapResultSetToIncident(ResultSet rs) throws SQLException {
        Incident incident = new Incident();
        incident.setIncidentID(rs.getInt("IncidentID"));
        incident.setIncidentType(IncidentType.valueOf(rs.getString("IncidentType").trim())); // Added trim() to handle CHAR padding
        incident.setIncidentDate(rs.getDate("IncidentDate").toLocalDate());
        incident.setLocation(rs.getString("Location"));
        incident.setLatitude(rs.getDouble("Latitude"));
        incident.setLongitude(rs.getDouble("Longitude"));
        incident.setDescription(rs.getString("Description"));
        incident.setStatus(Status.valueOf(rs.getString("Status").trim())); // Added trim() to handle CHAR padding
        incident.setVictimID(rs.getInt("VictimID"));
        incident.setSuspectID(rs.getInt("SuspectID"));
        
        return incident;
    }
}
