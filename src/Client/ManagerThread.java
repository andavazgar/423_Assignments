/*

Name: Andrés Vazquez (#40007182)
Course: SOEN 423
Assignment 2

*/

package Client;

import Models.Corba.ICenterServer;
import Models.Corba.ICenterServerHelper;
import Models.ManagerRecord;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

public class ManagerThread extends Thread {
    
    private ManagerRecord record;
    
    public ManagerThread(ManagerRecord record) {
        super();
        
        this.record = record;
    }
    
    public void run() {
        try {
            System.out.println("Thread " + this.getId() + " has been started.");

            // create and initialize the ORB
            ORB orb = ORB.init(new String[]{"-ORBInitialPort", "1050"}, null);
    
            // get the root naming context
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
    
            // Use NamingContextExt instead of NamingContext. This is part of the Interoperable naming Service.
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
    
            // resolve the Object Reference in Naming
            ICenterServer server = ICenterServerHelper.narrow(ncRef.resolve_str(record.getLocation() + "_Server"));
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
