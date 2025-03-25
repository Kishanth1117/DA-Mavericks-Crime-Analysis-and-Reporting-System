-- Drop database if it exists and create a new one
DROP DATABASE IF EXISTS cars;
CREATE DATABASE cars;
USE cars;

-- Create Victims table
CREATE TABLE Victims (
    VictimID INT AUTO_INCREMENT PRIMARY KEY,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    DateOfBirth DATE,
    Gender VARCHAR(10),
    Address VARCHAR(255),
    PhoneNumber VARCHAR(20)
);

-- Create Suspects table
CREATE TABLE Suspects (
    SuspectID INT AUTO_INCREMENT PRIMARY KEY,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    DateOfBirth DATE,
    Gender VARCHAR(10),
    Address VARCHAR(255),
    PhoneNumber VARCHAR(20)
);

-- Create LawEnforcementAgencies table
CREATE TABLE LawEnforcementAgencies (
    AgencyID INT AUTO_INCREMENT PRIMARY KEY,
    AgencyName VARCHAR(100) NOT NULL,
    Jurisdiction VARCHAR(100),
    Address VARCHAR(255),
    PhoneNumber VARCHAR(20)
);

-- Create Officers table
CREATE TABLE Officers (
    OfficerID INT AUTO_INCREMENT PRIMARY KEY,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    BadgeNumber VARCHAR(20) UNIQUE NOT NULL,
    `Rank` VARCHAR(50),
    PhoneNumber VARCHAR(20),
    AgencyID INT,
    FOREIGN KEY (AgencyID) REFERENCES LawEnforcementAgencies(AgencyID)
);

-- Create Incidents table
CREATE TABLE Incidents (
    IncidentID INT AUTO_INCREMENT PRIMARY KEY,
    IncidentType ENUM('ROBBERY', 'HOMICIDE', 'THEFT', 'ASSAULT', 'BURGLARY', 'VANDALISM', 'FRAUD', 'OTHER') NOT NULL,
    IncidentDate DATE NOT NULL,
    Location VARCHAR(255),
    Latitude DECIMAL(10, 8),
    Longitude DECIMAL(11, 8),
    Description TEXT,
    Status ENUM('OPEN', 'CLOSED', 'UNDER_INVESTIGATION') NOT NULL,
    VictimID INT,
    SuspectID INT,
    FOREIGN KEY (VictimID) REFERENCES Victims(VictimID),
    FOREIGN KEY (SuspectID) REFERENCES Suspects(SuspectID)
);

-- Create Evidence table
CREATE TABLE Evidence (
    EvidenceID INT AUTO_INCREMENT PRIMARY KEY,
    Description TEXT NOT NULL,
    LocationFound VARCHAR(255),
    IncidentID INT,
    FOREIGN KEY (IncidentID) REFERENCES Incidents(IncidentID)
);

-- Create Reports table
CREATE TABLE Reports (
    ReportID INT AUTO_INCREMENT PRIMARY KEY,
    IncidentID INT,
    ReportingOfficer INT,
    ReportDate DATE NOT NULL,
    ReportDetails TEXT,
    Status ENUM('DRAFT', 'FINALIZED') NOT NULL,
    FOREIGN KEY (IncidentID) REFERENCES Incidents(IncidentID),
    FOREIGN KEY (ReportingOfficer) REFERENCES Officers(OfficerID)
);

-- Create Cases table
CREATE TABLE Cases (
    CaseID INT AUTO_INCREMENT PRIMARY KEY,
    CaseDescription TEXT NOT NULL
);

-- Create CaseIncidents junction table for many-to-many relationship
CREATE TABLE CaseIncidents (
    CaseID INT,
    IncidentID INT,
    PRIMARY KEY (CaseID, IncidentID),
    FOREIGN KEY (CaseID) REFERENCES Cases(CaseID),
    FOREIGN KEY (IncidentID) REFERENCES Incidents(IncidentID)
);

CREATE TABLE incident_case (
    id INT AUTO_INCREMENT PRIMARY KEY,
    IncidentID INT NOT NULL,
    CaseID INT NOT NULL,
    FOREIGN KEY (IncidentID) REFERENCES incidents(IncidentID),
    FOREIGN KEY (CaseID) REFERENCES cases(CaseID),
    UNIQUE (IncidentID, CaseID)
);


-- Insert sample data for testing

-- Insert sample victims
INSERT INTO Victims (FirstName, LastName, DateOfBirth, Gender, Address, PhoneNumber)
VALUES 
('John', 'Doe', '1985-05-15', 'Male', '123 Main St, Anytown', '555-123-4567'),
('Jane', 'Smith', '1990-08-22', 'Female', '456 Oak Ave, Somecity', '555-987-6543'),
('Robert', 'Johnson', '1978-11-30', 'Male', '789 Pine Rd, Otherville', '555-456-7890');

-- Insert sample suspects
INSERT INTO Suspects (FirstName, LastName, DateOfBirth, Gender, Address, PhoneNumber)
VALUES 
('Michael', 'Brown', '1982-03-10', 'Male', '321 Elm St, Anytown', '555-111-2222'),
('Sarah', 'Williams', '1988-07-19', 'Female', '654 Maple Dr, Somecity', '555-333-4444'),
('David', 'Miller', '1975-12-05', 'Male', '987 Cedar Ln, Otherville', '555-555-6666');

-- Insert sample law enforcement agencies
INSERT INTO LawEnforcementAgencies (AgencyName, Jurisdiction, Address, PhoneNumber)
VALUES 
('Anytown Police Department', 'Anytown', '100 Police Plaza, Anytown', '555-911-0000'),
('State Bureau of Investigation', 'State', '200 State Bldg, Capital City', '555-911-1111');

-- Insert sample officers
INSERT INTO Officers (FirstName, LastName, BadgeNumber, `Rank`, PhoneNumber, AgencyID)
VALUES 
('James', 'Wilson', 'APD-001', 'Detective', '555-911-2222', 1),
('Emily', 'Taylor', 'APD-002', 'Sergeant', '555-911-3333', 1),
('Thomas', 'Anderson', 'SBI-001', 'Special Agent', '555-911-4444', 2);

-- Insert sample incidents
INSERT INTO Incidents (IncidentType, IncidentDate, Location, Latitude, Longitude, Description, Status, VictimID, SuspectID)
VALUES 
('ROBBERY', '2023-01-15', 'First National Bank, 100 Main St', 40.7128, -74.0060, 'Armed robbery at bank', 'UNDER_INVESTIGATION', 1, 1),
('THEFT', '2023-02-22', 'Downtown Mall, 200 Shopping Ave', 40.7129, -74.0061, 'Shoplifting incident', 'CLOSED', 2, 2),
('ASSAULT', '2023-03-10', 'City Park, 300 Park Rd', 40.7130, -74.0062, 'Physical altercation between two individuals', 'OPEN', 3, 3);

-- Insert sample evidence
INSERT INTO Evidence (Description, LocationFound, IncidentID)
VALUES 
('Handgun, 9mm', 'Trash can behind bank', 1),
('Security camera footage', 'Mall security office', 2),
('Broken bottle', 'Near park bench', 3);

-- Insert sample reports
INSERT INTO Reports (IncidentID, ReportingOfficer, ReportDate, ReportDetails, Status)
VALUES 
(1, 1, '2023-01-15', 'Initial report of bank robbery. Suspect fled in a blue sedan.', 'FINALIZED'),
(2, 2, '2023-02-22', 'Theft of merchandise valued at approximately $500.', 'FINALIZED'),
(3, 3, '2023-03-10', 'Assault occurred after verbal disagreement. Both parties sustained minor injuries.', 'DRAFT');

-- Insert sample cases
INSERT INTO Cases (CaseDescription)
VALUES 
('Bank robberies in downtown area'),
('Retail theft investigation');

-- Link cases to incidents
INSERT INTO CaseIncidents (CaseID, IncidentID)
VALUES 
(1, 1),
(2, 2);

SELECT * FROM Incidents;
SELECT * FROM Victims;
SELECT * FROM Suspects;
SELECT * FROM LawEnforcementAgencies;
SELECT * FROM Officers;
SELECT * FROM Evidence;
SELECT * FROM Reports;
SELECT * FROM Cases;
SELECT * FROM CaseIncidents;
SELECT * FROM incident_case;
