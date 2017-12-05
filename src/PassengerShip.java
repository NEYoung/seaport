/* Course: CMSC 335
 * Assignment: Project 3 - SeaPort Program
 * File: PassengerShip.java
 * Author: Ngozi Young
 * Date: 10/27/2017
 * Modified Date: 12/03/2017
 * Purpose: To define the elements of a Passenger ship which is a kind of Ship.
 */

import java.util.Scanner;

public class PassengerShip extends Ship {
    private int numberOfOccupiedRooms;
    private int numberOfPassengers;
    private int numberOfRooms;

    public PassengerShip(Scanner sc) {
        // ThingKeyword  name index parent(dock/port) weight length width draft numPassengers numRooms numOccupied
        // "pship"      <string> <int> <int> <double> <double> <double> <double> <int> <int> <int>
        super(sc);
        // checks that these data are provided before initializing these fields
        if(sc.hasNextInt())
            numberOfPassengers = sc.nextInt();
        if(sc.hasNextInt())
            numberOfRooms = sc.nextInt();
        if(sc.hasNextInt())
            numberOfOccupiedRooms = sc.nextInt();
    }


    @Override
    public String toString() {
        return "Passenger ship: " + super.toString()  + ", Passenger Count: " + numberOfPassengers + ", Room Count: "
                + numberOfRooms + ", Occupied Room Count: " + numberOfOccupiedRooms;
    }

}