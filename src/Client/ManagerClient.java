/*

Name: Andres Vazquez (#40007182)
Course: SOEN 423
Assignment 3

*/

package Client;

import Models.*;
import Models.Enums.Location;

import java.io.File;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class ManagerClient {
    
    private static String[] commands = {
            "Create Manager Record",
            "Create Employee Record",
            "Get Record Counts",
            "Edit Record",
            "Transfer Record",
            "Log out"
    };
    private static Map<Location, ICenterServer> centerServers = new HashMap<>();
    private static String managerID = "";
    private static boolean isLoggedIn = false;
    private static Location location;
    private static Scanner sc = new Scanner(System.in);
    
    
    private static void printManagerMenu() {
        
        System.out.println("\nChoose one of the following options:");
        
        for(int i = 1; i <= commands.length; i++) {
            System.out.println(i + ") " + commands[i -1]);
        }
    }
    
    private static boolean verifyManagerID(String managerID) {
        Pattern idPattern = Pattern.compile("(?<location>[a-zA-Z]{2})(?<number>[0-9]{4})");
        Matcher matcher = idPattern.matcher(managerID);
        
        return matcher.matches() && Location.isValidLocation(matcher.group("location"));
    }
    
    private static void setRemoteStubs(String[] args) {
        try{
        	URL url;
        	QName qName = new QName("http://CenterServer/", "CenterServerService");
        	Service service;
        	
            for (Location loc: Location.values()) {
            	url = new URL("http://localhost:9000/" + loc + "_Server?wsdl");
            	service = Service.create(url, qName);
                centerServers.put(loc, service.getPort(ICenterServer.class));
            }
        }
        catch (Exception e) {
            System.out.println("ERROR: " + e) ;
            e.printStackTrace(System.out);
        }
    }
    
    private static void clearLog() {
        OperationLogger.deleteLogFile(new File("Logs/HR/" + managerID + ".txt"));
    }
    
    private static void addHRLogEntry(String... data) {
        String hrLogFilePath = "Logs/HR/" + managerID + ".txt";
        String dataToLog = String.join("\n", data) + "\n";
    
        OperationLogger.log(hrLogFilePath, dataToLog);
    }
    
    private static void createEmployee(boolean isManager) {
        String fName, lName, mailID;
        int empId;
        Pattern basicInfoPattern = Pattern.compile("(?<fname>[A-Za-z][A-Za-z ]*);[ ]*(?<lname>[A-Za-z][A-Za-z- ]*);[ ]*(?<empID>[0-9]+);[ ]*(?<mailID>[\\w-]+@[a-z0-9.]+)");
        Matcher matcher;
        
        System.out.println("Please enter the employee/manager information in the following order (separated by semicolons ';'):");
        System.out.println("First Name; Last Name; Employee ID; Mail ID");
        
        String recordBasicInfo = sc.nextLine();
        matcher = basicInfoPattern.matcher(recordBasicInfo);
    
        if (matcher.matches()) {
            fName = matcher.group("fname");
            lName = matcher.group("lname");
            empId = Integer.parseInt(matcher.group("empID"));
            mailID = matcher.group("mailID");
        }
        else {
            // Error
            String errorMsg = "The employee/manager's information was not entered with the proper format!";
            String operationPerformed = isManager ? "createMRecord" : "createERecord";
    
            LogEntry logEntry = new LogEntry(managerID, operationPerformed, false);
    
            addHRLogEntry(logEntry.toString(), "Error Message: " + errorMsg, "User Input (New Record's Basic Info): " + recordBasicInfo);
    
            System.out.println(errorMsg);
            return;
        }
        
        // IF the record is a Manager
        if (isManager) {
            Project project;
            Pattern projectInfoPattern = Pattern.compile("(?<projectID>P[0-9]{5});[ ]*(?<clientName>[A-Za-z][A-Za-z-. ]*);[ ]*(?<projectName>\\w[\\w- ]*)");
    
            System.out.println("\nPlease enter the Manager's PROJECT INFORMATION in the following order (separated by semicolons ';'):");
            System.out.println("ProjectID (format: P00001); Client's Name; Project Name");
            
            String projectInfo = sc.nextLine();
            matcher = projectInfoPattern.matcher(projectInfo);
    
            if (matcher.matches()) {
                project = new Project(matcher.group("projectID"), matcher.group("clientName"), matcher.group("projectName"));
            }
            else {
                // Error
                String errorMsg = "The project information was not entered with the proper format!";
                LogEntry logEntry = new LogEntry(managerID, "createMRecord", false);
    
                addHRLogEntry(logEntry.toString(), "Error Message: " + errorMsg, "User Input (New Manager's Project Info): " + projectInfo);
    
                System.out.println(errorMsg);
                return;
            }

            //  Send server request
            try {
                String logEntryServer = centerServers.get(location).createMRecord(managerID, fName, lName, empId, mailID, project, location.toString());
                
                addHRLogEntry(logEntryServer);
            }
            catch (Exception e) {
                String errorMsg = "The method createMRecord threw an exception (" + e.getMessage() + ")";
                LogEntry logEntry = new LogEntry(managerID, "createMRecord", false);
    
                addHRLogEntry(logEntry.toString(), "Error Message: " + errorMsg);
    
                System.out.println(errorMsg);
                System.out.println(e.getMessage());
            }
        }
        
        // It's a regular Employee
        else {
            String projectID;
            Pattern projectIDPattern = Pattern.compile("(?<projectID>P[0-9]{5})");
    
            System.out.println("\nPlease enter the Employee's Project ID (format: P00001):");
            
            projectID = sc.nextLine();
            matcher = projectIDPattern.matcher(projectID);
    
            if (matcher.matches()) {
                projectID = matcher.group("projectID");
            }
            else {
                // Error
                String errorMsg = "The Project ID entered does not have the proper format! (format: P00001)";
                LogEntry logEntry = new LogEntry(managerID, "createERecord", false);
    
                addHRLogEntry(logEntry.toString(), "Error Message: " + errorMsg, "User Input (New Employee's Project ID): " + projectID);
    
                System.out.println(errorMsg);
                return;
            }
    
            //  Send server request
            try {
                String logEntryServer = centerServers.get(location).createERecord(managerID, fName, lName, empId, mailID, projectID);
                
                addHRLogEntry(logEntryServer);
            }
            catch (Exception e) {
                String errorMsg = "The method createERecord threw an exception (" + e.getMessage() + ")";
                LogEntry logEntry = new LogEntry(managerID, "createERecord", false);
    
                addHRLogEntry(logEntry.toString(), "Error Message: " + errorMsg);
    
                System.out.println(errorMsg);
                System.out.println(e.getMessage());
            }
        }
    }
    
    private static void editRecord() {
        String recordID, fieldToChange, newValue;
        Pattern recordIDPattern = Pattern.compile("(?<recordID>(?>MR|ER)[0-9]{5})");
        Matcher matcher;
        ArrayList<String> fieldsCanBeChanged = new ArrayList<>();
    
        System.out.println("Please enter the Record ID:");
    
        recordID = sc.nextLine().toUpperCase();
        matcher = recordIDPattern.matcher(recordID);
    
        if (matcher.matches()) {
            recordID = matcher.group("recordID");
        }
        // Error
        else {
            String errorMsg = "The Record ID entered does not have the proper format! (ex: MR10000, ER10001)";
            LogEntry logEntry = new LogEntry(managerID, "editRecord", false);
    
            addHRLogEntry(logEntry.toString(), "Error Message: " + errorMsg, "User Input (Record ID): " + recordID);
    
            System.out.println(errorMsg);
            return;
        }
        
        if (recordID.substring(0, 2).equalsIgnoreCase("MR")) {
            fieldsCanBeChanged.addAll(Arrays.asList("mailID", "projectID", "clientName", "projectName", "location"));
            
        }
        else {
            fieldsCanBeChanged.addAll(Arrays.asList("mailID", "projectID"));
        }
        
        System.out.println("\nWhich of the following fields would you like to edit?");
        System.out.println(String.join(", ", fieldsCanBeChanged));
        
        fieldToChange = sc.nextLine();
        
        if (fieldsCanBeChanged.contains(fieldToChange)) {
            System.out.print("\nNew value: ");
            
            newValue = sc.nextLine();
            
            if (!newValue.equals("")) {
                if (!fieldToChange.equalsIgnoreCase("location") || (fieldToChange.equalsIgnoreCase("location") && Location.isValidLocation(newValue))) {
                    try {
                        String logEntryServer = centerServers.get(location).editRecord(managerID, recordID, fieldToChange, newValue);
                        
                        addHRLogEntry(logEntryServer);
    
                        if (!logEntryServer.contains("Error Message: ")) {
                            System.out.println("Successfully edited the record.");
                        }
                        else {
                            System.out.println(logEntryServer.substring(logEntryServer.indexOf("Error Message: ")));
                        }
                    }
                    // Error
                    catch (Exception e) {
                        String errorMsg = "The method editRecord threw an exception (" + e.getMessage() + ")";
                        LogEntry logEntry = new LogEntry(managerID, "editRecord", false);
    
                        addHRLogEntry(logEntry.toString(), "Error Message: " + errorMsg, "User Input (Edit Record): " + "Edit Record: " + "{recordID=" + recordID + ", fieldName=" + fieldToChange + ", newValue=" + newValue + "}");
    
                        System.out.println(errorMsg);
                        System.out.println(e.getMessage());
                    }
                }
                // Invalid Location Error
                else {
                    String errorMsg = "The new location is not valid. Valid locations = " + Location.printLocations();
                    LogEntry logEntry = new LogEntry(managerID, "editRecord", false);
    
                    addHRLogEntry(logEntry.toString(), "Error Message: " + errorMsg, "User Input (New value): " + newValue);
    
                    System.out.println(errorMsg);
                    return;
                }
            }
            // Error
            else {
                String errorMsg = "The new value cannot be empty";
                LogEntry logEntry = new LogEntry(managerID, "editRecord", false);
    
                addHRLogEntry(logEntry.toString(), "Error Message: " + errorMsg, "User Input (New value): " + newValue);
    
                System.out.println(errorMsg);
                return;
            }
            
        }
        // Error
        else {
            String errorMsg = "The field name that you wish to edit is not valid or allowed. The field names that can be modified are the following: " + String.join(", ", fieldsCanBeChanged);
            LogEntry logEntry = new LogEntry(managerID, "editRecord", false);
    
            addHRLogEntry(logEntry.toString(), "Error Message: " + errorMsg, "User Input (Field name to edit): " + fieldToChange);
    
            System.out.println(errorMsg);
            return;
        }
    }
    
    private static void transferRecord() {
        String recordID, centerServerLocation;
        List<String> locations = Location.getLocationsAsStrings();
        Pattern recordIDPattern = Pattern.compile("(?<recordID>(?>MR|ER)[0-9]{5})");
        Matcher matcher;
    
        System.out.println("Please enter the Record ID:");
    
        recordID = sc.nextLine().toUpperCase();
        matcher = recordIDPattern.matcher(recordID);
    
        if (matcher.matches()) {
            recordID = matcher.group("recordID");
        }
        else {
            String errorMsg = "The Record ID entered does not have the proper format! (ex: MR10000, ER10001)";
            LogEntry logEntry = new LogEntry(managerID, "transferRecord", false);
        
            addHRLogEntry(logEntry.toString(), "Error Message: " + errorMsg, "User Input (Record ID): " + recordID);
        
            System.out.println(errorMsg);
            return;
        }
        
        locations.remove(location.toString());
    
        System.out.println("\nWhere would you like to transfer the record?");
        System.out.println("Options: " + String.join(", ", locations));
    
        centerServerLocation = sc.nextLine().toUpperCase();
        
        if (locations.contains(centerServerLocation)) {
            String serverLog = centerServers.get(location).transferRecord(managerID, recordID, centerServerLocation);
            
            addHRLogEntry(serverLog);
            
            if (!serverLog.contains("Error Message: ")) {
                System.out.println("Successfully transferred the record.");
            }
            else {
                System.out.println(serverLog.substring(serverLog.indexOf("Error Message: ")));
            }
        }
        else {
            String errorMsg = "The center server location entered is not valid.";
            LogEntry logEntry = new LogEntry(managerID, "transferRecord", false);
    
            addHRLogEntry(logEntry.toString(), "Error Message: " + errorMsg, "User Input (Center Server Location): " + centerServerLocation);
    
            System.out.println(errorMsg);
        }
    }
    
    private static void performCommand(int commandIndex) throws RemoteException {
        
        switch (commandIndex) {
            case 0:
                createEmployee(true);
                break;
            case 1:
                createEmployee(false);
                break;
            case 2:
                String recordCount = centerServers.get(location).getRecordCounts(managerID);
                System.out.println(recordCount);
                break;
            case 3:
                editRecord();
                break;
            case 4:
                transferRecord();
                break;
            case 5:
            isLoggedIn = false;
            break;
        }
    }
    
    public static void main(String[] args) {
        
        int optionSelected = 0;
        
        System.out.println("Welcome to the ManagerClient system.");
        
        while(!isLoggedIn) {
            System.out.print("Please enter your Manager ID: ");
            managerID = sc.nextLine();
            isLoggedIn = verifyManagerID(managerID);
            
            if(!isLoggedIn) {
                System.out.println("The manager ID entered (" + managerID + ") is not valid. Valid Manager ID format = AA9999\n");
            }
        }
    
        location = Location.getLocation(managerID.substring(0, 2));
        
        setRemoteStubs(args);
        
        LogEntry logEntry = new LogEntry(managerID, "Login", true);
        addHRLogEntry(logEntry.toString());
        
        while (isLoggedIn) {
            printManagerMenu();
            
            if (sc.hasNextInt()) {
                optionSelected = sc.nextInt();
                sc.nextLine();   // The break line (\n) at the end of the line needs to be consumed. nextInt() doesn't consume it.
    
                if (optionSelected <= commands.length) {
                    try {
                        performCommand(optionSelected - 1);
                    }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                else {
                    sc.nextLine();
                    System.out.println("Invalid option. Please select an option between 1-" + commands.length);
                }
            }
            else {
                sc.nextLine();
                System.out.println("Invalid option. Please select an option between 1-" + commands.length);
            }
            
        }
    
        logEntry = new LogEntry(managerID, "Logout", true);
        addHRLogEntry(logEntry.toString());
    
        clearLog();
    }
}
