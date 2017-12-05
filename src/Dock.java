/* Course: CMSC 335
 * Assignment: Project 3 - SeaPort Program
 * File: Dock.java
 * Author: Ngozi Young
 * Date: 10/27/2017
 * Modified Date: 12/03/2017
 * Purpose: To define the elements of a Dock.
 */

import java.util.ArrayList;
import java.util.Scanner;

public class Dock extends Thing {

    private Ship ship;

    Dock(Scanner sc) {
        //ThingKeyword   name index parent(port)
        //"dock"        <string> <int> <int>
        super(sc);
    }

    // checks docked ship before requesting for a ship in the que
    public void checkDock() {
        if(ship == null || ship.jobs.size() == 0)
            requestShipInQue();
    }

    // Request for a ship waiting in que
    private void requestShipInQue() {
        Ship s = ((SeaPort) parentThing).getShipFromQue();
        if(s != null)
            setShip(s);
    }

    // Sets ship's parent to appropriate dock
    public void setShip(Ship s) {
        synchronized (this) {
            ship = s;
            if(s != null)
                ship.parentThing = this;
        }
    }

    // Request for a person with the needed skill for job
    boolean requestSkilledPerson(ArrayList<String> requirements) {
        return ((SeaPort) parentThing).requestSkilledPerson(requirements);
    }

    // Request for an available worker for the job
    public ArrayList<Person> requestWorkers(ArrayList<String> requirements, Job job) {
        return ((SeaPort) parentThing).requestWorkers(requirements, job);
    }

    // Undocks ship and checks dock for a new ship from que
    public void leaveDock() {
        setShip(null);
        checkDock();
    }

    public String getText() {
        return "Dock Name: " + super.toString();
    }

    // Getter method
    public Ship getShip() {
        return ship;
    }

    // Displays the data structure of the Dock
    @Override
    public String toString() {
        // name + index
        return "Dock: " + super.toString() +
                "\n Ship: " + ship;
    }

}