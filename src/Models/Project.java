/*

Name: Andres Vazquez (#40007182)
Course: SOEN 423
Assignment 3

*/

package Models;

import java.io.Serializable;

public class Project implements Serializable {
    private String projectId;
    private String clientName;
    private String projectName;
    
    public Project() {
    
    }
    
    public Project(String projectId, String clientName, String projectName) {
        this.projectId = projectId;
        this.clientName = clientName;
        this.projectName = projectName;
    }
    
    public String getProjectId() {
        return projectId;
    }
    
    public synchronized void setProjectId(String projectId) {
        this.projectId = projectId;
    }
    
    public String getClientName() {
        return clientName;
    }
    
    public synchronized void setClientName(String clientName) {
        this.clientName = clientName;
    }
    
    public String getProjectName() {
        return projectName;
    }
    
    public synchronized void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    
    @Override
    public String toString() {
        return "Project{" +
                "projectId='" + projectId + '\'' +
                ", clientName='" + clientName + '\'' +
                ", projectName='" + projectName + '\'' +
                '}';
    }
}
