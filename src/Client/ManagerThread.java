/*

Name: Andres Vazquez (#40007182)
Course: SOEN 423
Assignment 3

*/

package Client;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import Models.ICenterServer;
import Models.ManagerRecord;

public class ManagerThread extends Thread {
    
    private ManagerRecord record;
    
    public ManagerThread(ManagerRecord record) {
        super();
        
        this.record = record;
    }
    
    public void run() {
        try {
            System.out.println("Thread " + this.getId() + " has been started.");
            
            URL url = new URL("http://localhost:9000/" + record.getLocation() + "_Server?wsdl");
        	QName qName = new QName("http://CenterServer/", "CenterServerService");
        	Service service = Service.create(url, qName);;
        	
            ICenterServer server = service.getPort(ICenterServer.class);
            
            String serverLog = server.createMRecord("ManagerClient_Test", record.getfName(), record.getlName(), record.getEmpID(), record.getMailID(), record.getProject(), record.getLocation());
            int recordID_Index = serverLog.indexOf("id='") +4;
            String recordID = serverLog.substring(recordID_Index, recordID_Index +7);
    
    
            System.out.println("Thread " + this.getId() + " editRecord() = " + server.editRecord("ManagerClient_Test", recordID, "mailID", record.getMailID() + "EDITED"));
            if (!record.getLocation().equalsIgnoreCase("CA")) {
                System.out.println("Thread " + this.getId() + " transferRecord() = " + server.transferRecord("ManagerClient_Test", recordID, "CA"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
