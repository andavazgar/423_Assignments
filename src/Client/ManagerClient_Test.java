/*

Name: Andrés Vazquez (#40007182)
Course: SOEN 423
Assignment 2

*/

package Client;

import Models.Corba.ICenterServer;
import Models.Corba.ICenterServerHelper;
import Models.Corba.Project;
import Models.Enums.Location;
import Models.ManagerRecord;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;

public class ManagerClient_Test {
    
    public static void main(String[] args) {
        ManagerRecord[] testRecords = {
                new ManagerRecord("Joe", "Mars", 123, "joe@mars.com", new Project("P12345", "Me", "The Project"), Location.CA),
                new ManagerRecord("Martin", "Jones", 123, "martin@jones.com", new Project("P13443", "Bob", "The Bridge"), Location.US),
                new ManagerRecord("Lucas", "Brown", 123, "lucas@brown.com", new Project("P15655", "Susan", "The Chair"), Location.UK),
                new ManagerRecord("Kate", "Johnson", 123, "kate@johnson.com", new Project("P45456", "Mark", "The Floor"), Location.UK),
                new ManagerRecord("Jessica", "Harrison", 123, "jessica@harrison.com", new Project("P45645", "Bob", "The Cable"), Location.US),
                new ManagerRecord("Michael", "Allen", 123, "michael@allen.com", new Project("P12343", "Peter", "The TV"), Location.CA),
                new ManagerRecord("Mario", "Rodriguez", 123, "mario@rodriguez.com", new Project("P97764", "Luke", "The Lamp"), Location.UK),
                new ManagerRecord("Simon", "Bar", 123, "simon@bar.com", new Project("P56638", "Derek", "The House"), Location.US),
                new ManagerRecord("Lisa", "Jackson", 123, "lisa@jackson.com", new Project("P23564", "Han", "The Street"), Location.CA)
        };
            ManagerThread thread_0 = new ManagerThread(testRecords[0]);
            ManagerThread thread_1 = new ManagerThread(testRecords[1]);
            ManagerThread thread_2 = new ManagerThread(testRecords[2]);
            ManagerThread thread_3 = new ManagerThread(testRecords[3]);
            ManagerThread thread_4 = new ManagerThread(testRecords[4]);
            ManagerThread thread_5 = new ManagerThread(testRecords[5]);
            ManagerThread thread_6 = new ManagerThread(testRecords[6]);
            ManagerThread thread_7 = new ManagerThread(testRecords[7]);
            ManagerThread thread_8 = new ManagerThread(testRecords[8]);
        
            
            thread_0.start();
            thread_1.start();
            thread_2.start();
            thread_3.start();
            thread_4.start();
            thread_5.start();
            thread_6.start();
            thread_7.start();
            thread_8.start();
    
        try {
            thread_0.join();
            thread_1.join();
            thread_2.join();
            thread_3.join();
            thread_4.join();
            thread_5.join();
            thread_6.join();
            thread_7.join();
            thread_8.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    
            
        try {
        // create and initialize the ORB
        ORB orb = ORB.init(new String[]{"-ORBInitialPort", "1050"}, null);
    
        // get the root naming context
        org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
    
        // Use NamingContextExt instead of NamingContext. This is part of the Interoperable naming Service.
        NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
    
        // resolve the Object Reference in Naming
        
            ICenterServer server = ICenterServerHelper.narrow(ncRef.resolve_str("CA_Server"));
            System.out.println("\n\nFINAL: getRecordCounts() = " + server.getRecordCounts("ManagerClient_Test"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
