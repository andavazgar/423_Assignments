/*

Name: Andrés Vazquez (#40007182)
Course: SOEN 423
Assignment 2

*/

package Models;

import Models.Corba.Project;
import Models.Enums.Location;

import java.io.Serializable;

public class ManagerRecord extends Record implements Serializable {
    private Project project;
    private String location;
    
    public ManagerRecord(String fName, String lName, int empId, String mailId, Project project, Location location) {
        super("MR", fName, lName, empId, mailId);
        
        this.project = project;
        this.location = location.toString();
    }
    
    public Project getProject() {
        return project;
    }
    
    public void setProject(Project project) {
        this.project = project;
    }
    
    public String getLocation() {
        return location;
    }
    
    public synchronized void setLocation(String location) {
        this.location = location;
    }
    
    @Override
    public String toString() {
        return "ManagerRecord{" +
                super.toString() +
                ", project=" + project +
                ", location='" + location + '\'' +
                '}';
    }
}
