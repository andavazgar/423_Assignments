/*

Name: Andres Vazquez (#40007182)
Course: SOEN 423
Assignment 3

*/

package Models;

import javax.jws.WebService;

@WebService()
public interface ICenterServer {
    
    public String createMRecord(String managerID, String firstName, String lastName, int employeeID, String mailID, Project project, String location);
    
    public String createERecord(String managerID, String firstName, String lastName, int employeeID, String mailID, String projectID);
    
    public String getRecordCounts(String managerID);
    
    public String editRecord(String managerID, String recordID, String fieldName, String newValue);
    
    public String transferRecord (String managerID, String recordID, String remoteCenterServerName);
    
    public void printData();
}
