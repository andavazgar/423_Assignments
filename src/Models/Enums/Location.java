/*

Name: Andrés Vazquez (#40007182)
Course: SOEN 423
Assignment 2

*/

package Models.Enums;

import java.util.ArrayList;
import java.util.List;

public enum Location {
    CA(7000), US(7001), UK(7002);
    
    private int port;
    
    Location(int UDPPort) {
        port = UDPPort;
    }
    
    public static boolean isValidLocation(String location) {
        for (Location loc: values()) {
            if (loc.toString().equalsIgnoreCase(location)) {
                return true;
            }
        }
        
        return false;
    }
    
    public static Location getLocation(String location) {
        for (Location loc: values()) {
            if (loc.toString().equalsIgnoreCase(location)) {
                return loc;
            }
        }
        
        return null;
    }
    
    public static List<String> getLocationsAsStrings() {
        List<String> locations = new ArrayList<>();
        
        for (Location loc: values()) {
            locations.add(loc.toString());
        }
        
        return locations;
    }
    
    public static String printLocations() {
        String out = "";
    
        for (Location loc: values()) {
            out += loc + ", ";
        }
        
        out = out.substring(0, out.length() - 2);
    
        return out;
    }
    
    public int getUDPPort() {
        return port;
    }
}
