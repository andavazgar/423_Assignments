/*

Name: Andrés Vazquez (#40007182)
Course: SOEN 423
Assignment 2

*/

package Remote;

import Models.Corba.ICenterServer;
import Models.Corba.ICenterServerHelper;
import Models.Enums.Location;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

public class Server {
    
    public static void main(String[] args) {
        try{
            // create and initialize the ORB
            ORB orb = ORB.init(args, null);
        
            // get reference to rootpoa & activate the POAManager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();
    
            // get the root naming context
            // NameService invokes the name service
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
    
            // Use NamingContextExt which is part of the Interoperable
            // Naming Service (INS) specification.
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
    
            for (Location loc: Location.values()) {
                // get object reference from the servant
//                CenterServer location_server = new CenterServer(loc);
                CenterServer location_server = new CenterServer(loc);
                Thread location_server_thread = new Thread(location_server);
                location_server_thread.start();
                
                org.omg.CORBA.Object ref = rootpoa.servant_to_reference(location_server);
                ICenterServer href = ICenterServerHelper.narrow(ref);
    
                // bind the Object Reference in Naming
                NameComponent path[] = ncRef.to_name(loc + "_Server");
                ncRef.rebind(path, href);
            }
    
            System.out.println("All three CenterServers are now running on port 1050 ...");
        
            // wait for invocations from clients
            orb.run();
        }
    
        catch (Exception e) {
            System.err.println("ERROR: " + e);
            e.printStackTrace(System.out);
        }
    }
}
