package hexaware.cars.dao;

import hexaware.cars.model.*;
import hexaware.cars.exception.*;

import java.time.LocalDate;
import java.util.List;

public interface ICrimeAnalysisService {
    // Incident Management
    boolean createIncident(Incident incident) throws InvalidDataException, DatabaseConnectionException;
    boolean updateIncidentStatus(Status newStatus, int incidentID) throws IncidentNumberNotFoundException, DatabaseConnectionException;
    Incident getIncidentById(int incidentID) throws IncidentNumberNotFoundException, DatabaseConnectionException;
    List<Incident> getAllIncidents() throws DatabaseConnectionException;
    
    // Case Management
    Case createCase(String caseDescription, List<Incident> incidents) throws InvalidDataException, DatabaseConnectionException;
    boolean updateCaseDetails(Case caseObj) throws IncidentNumberNotFoundException, DatabaseConnectionException;
    Case getCaseDetails(int caseID) throws IncidentNumberNotFoundException, DatabaseConnectionException;
    List<Case> getAllCases() throws DatabaseConnectionException;
    
    // Report Generation
    Report generateIncidentReport(Incident incident) throws IncidentNumberNotFoundException, DatabaseConnectionException;
    Report getReportById(int reportID) throws ReportNotFoundException, DatabaseConnectionException;
    List<Report> getAllReports() throws DatabaseConnectionException;
    
    // Search Operations
    List<Incident> getIncidentsInDateRange(LocalDate startDate, LocalDate endDate) throws DatabaseConnectionException;
    List<Incident> searchIncidents(IncidentType incidentType) throws DatabaseConnectionException;
}
