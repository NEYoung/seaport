/* Course: CMSC 335
 * Assignment: Project 3 - SeaPort Program
 * File: World.java
 * Author: Ngozi Young
 * Date: 10/27/2017
 * Modified Date: 12/03/2017
 * Purpose: Using HashMaps, adds elements of the SeaPort to the correct data structure by comparing the
 *          index value. Also uses the element's index number to return the correct SeaPort element.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class World extends Thing {

    private static boolean initStatus = false;
    private ArrayList<SeaPort> ports;
    private PortTime time;
    private SeaPort port;

    World(Scanner sc) {
        super(sc); // Initializes Thing constructor with name, index, and parent(null).
        ports = new ArrayList<>(); // Instantiates the object needed to organize SeaPort data.
    }

    // Adds Seaports to the ArrayList
    void addPort(SeaPort p) {
        ports.add(p);
    }

    // Assigns dock to the correct seaport by searching the Hashmap
    // for a seaport record that matches the dock's parent value
    // and assigns the dock to that seaport.
    void assignDock(Dock d, HashMap<Integer, SeaPort> hmsp) {
        port = hmsp.get(d.getParent()); //TODO: Update
        if(port != null) {
            d.setParentThing(hmsp);
            port.docks.add(d);
        }
    }

    // Assigns ship to the correct dock by searching the Hashmap
    // for a dock record that matches the ship's parent value
    // and assigns the ship to that dock.
    void assignShip(Ship s, HashMap<Integer, SeaPort> hmsp, HashMap<Integer, Dock> hmd) {
        Dock md = hmd.get(s.getParent());
        port = hmsp.get(s.getParent()); //TODO: Update
        // Assign ship to que and to the correct port, if the ship does not belong to a dock
        if(md == null) {
            s.setParentThing(hmsp);
            port.ships.add(s);
            port.que.add(s);
            return;
        }
        if(md.getShip() != null) {
            port.que.add(s);
        } else {
            // Otherwise, ship is added to the correct dock
            md.setShip(s);
        }
        // Finally adds ship to ship HashMap
        port = hmsp.get(md.getParent());
        port.ships.add(s);
    }

    // Finds the ship before linking job
    void assignJob(Job j, HashMap<Integer, Ship> hms) {
        Ship s;
        if(hms.get(j.getParent()) != null) {
            s = hms.get(j.getParent());
            s.jobs.add(j);
            j.setParentThing(hms);
        }
    }

    // Assigns person to the correct seaport by searching the Hashmap
    // for a seaport record that matches the person's parent value
    // and assigns the person to that seaport.
    void assignPerson(Person p, HashMap<Integer, SeaPort> hmsp) {
        if(hmsp.containsKey(p.getParent())){
            port = hmsp.get(p.getParent());
            port.persons.add(p);
        }
    }


    // Searches the HashMaps by the provided index number
    // and displays elements of the Seaport that matches by index number
    public String searchByIndex(String st, HashMap<Integer, SeaPort> hmsp, HashMap<Integer, Dock> hmd,
                                HashMap<Integer, Ship> hms, HashMap<Integer, Person> hmp) {
        int x;
        String result = "";
        try {
            x = Integer.parseInt(st);

            if (hmsp.containsKey(x))    // HashMap of SeaPorts
                result += hmsp.get(x);

            if (hmd.containsKey(x))     // HashMap of Docks
                result += hmd.get(x);

            if (hms.containsKey(x))     // HashMap of Ships
                result += hms.get(x);

            if (hmp.containsKey(x))     // HashMap of Persons
                result += hmp.get(x);

            if (result.equals(""))
                result = "No results found!";

        } catch (NumberFormatException err) {
            result = "Error! An invalid integer number has been entered!";
        }
        return result;
    }

    // Searches all SeaPorts, Docks, Ships, Queues, and Persons arrays for
    // records, that matches the provided name, and returns all matching records
    public String searchByName(String st) {
        String searchResult = "";
        for(SeaPort sp : ports) {
            if(sp.getName().equalsIgnoreCase(st))
                searchResult += sp.toString() + "\n";

            ArrayList<Dock> dArray = sp.getDocks();
            for(Dock d: dArray) {
                if(d.getName().equalsIgnoreCase(st)) {
                    searchResult += d.toString() + "\n";
                }
            }

            ArrayList<Ship> sArray = sp.getShips();
            for(Ship s: sArray) {
                if(s.getName().equalsIgnoreCase(st)) {
                    searchResult += s.toString() + "\n";
                }
            }

            ArrayList<Ship> qArray = sp.getQue();
            for(Ship q : qArray) {
                if(q.getName().equalsIgnoreCase(st)) {
                    searchResult += q.toString() + "\n";
                }
            }

            ArrayList<Person> pArray = sp.getPersons();
            for(Person p : pArray) {
                if(p.getName().equalsIgnoreCase(st)) {
                    searchResult += p.toString() + "\n";
                }
            }
        }
        return searchResult;
    }

    // Searches all SeaPorts for persons with the matching provided skill
    public String searchBySkill(String st) {
        String result = "";
        for(SeaPort sp : ports) {
            ArrayList<Person> pArray = sp.getPersons();
            for(Person p : pArray) {
                if(p.getSkill().equalsIgnoreCase(st)) {
                    result += p.toString() + "\n";
                }
            }
        }
        return result;
    }

    /* Getter and Setter Methods */
    public ArrayList<SeaPort> getPorts() {
        return ports;
    }

    public static boolean getInitStatus() {
        return initStatus;
    }

    public static void setInitStatus(boolean b) {
        initStatus = b;
    }

    @Override
    // Displays the data structure of the SeaPort
    public String toString() {
        String st = "";
        for(SeaPort sp: ports)
            st += sp.toString() + "\n";

        return st;
    }

}