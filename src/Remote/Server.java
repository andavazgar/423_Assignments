/*

Name: Andres Vazquez (#40007182)
Course: SOEN 423
Assignment 3

*/

package Remote;

import Models.Enums.Location;
import javax.xml.ws.Endpoint;

public class Server {
    
    public static void main(String[] args) {
        for (Location loc: Location.values()) {
            CenterServer server = new CenterServer(loc);
            Thread serverThread = new Thread(server);
            serverThread.start();
            
            Endpoint.publish("http://localhost:9000/"+ loc.toString() + "_Server", server);
        }
        
        System.out.println("All three CenterServers are now running on port 9000 (Web Service) ...");
    }
}