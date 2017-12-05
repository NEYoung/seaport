/* Course: CMSC 335
 * Assignment: Project 3 - SeaPort Program
 * File: Thing.java
 * Author: Ngozi Young
 * Date: 10/27/2017
 * Modified Date: 12/03/2017
 * Purpose: To define the kind of things a SeaPort has.
 *          Superclass to: World, SeaPort, Dock, Ship, Person, and Job
 *          Ancestor class to: PassengerShip, and CargoShip
 */

import java.util.HashMap;
import java.util.Scanner;

public class Thing implements Comparable <Thing> {

    private int index;
    private int parent;
    private String name;
    Thing parentThing;

    Thing(Scanner sc) {
        // ThingKeyword                                     name index parent(null)
        // ("port"/"dock"/"pship"/"cship"/"person"/"job")   <string> <int> <int>
        if(sc != null) {
            name = sc.next();
            index = sc.nextInt();
            parent = sc.nextInt();
        }
    }

    @Override
    public int compareTo(Thing o) {
        return name.compareToIgnoreCase(o.getName());
    }

    // Uses HashMap to keep track of all things in the sea world.
    public void setParentThing(HashMap parentHashMap) {
        if(parentHashMap.get(parent) != null)
            parentThing = (Thing) parentHashMap.get(parent);
    }

    /* Getter methods */
    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public int getParent() {
        return parent;
    }

    public String toString() {
        return name + ", Index: " + index + ", Parent Index: " + parent;
    }

}