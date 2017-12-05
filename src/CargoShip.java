/* Course: CMSC 335
 * Assignment: Project 3 - SeaPort Program
 * File: CargoShip.java
 * Author: Ngozi Young
 * Date: 11/03/2017
 * Modified Date: 12/03/2017
 * Purpose: To define the elements of a Cargo Ship.
 */

import java.util.Scanner;

public class CargoShip extends Ship {
    private double cargoWeight;
    private double cargoVolume;
    private double cargoValue;

    CargoShip(Scanner sc) {
        // ThingKeyword    name index parent(dock/port) weight length width draft cargoWeight cargoVolume cargoValue
        // "cship"         <string> <int> <int> <double> <double> <double> <double> <double> <double> <double>
        super(sc);
        if(sc.hasNextDouble())
            cargoWeight = sc.nextDouble();
        if(sc.hasNextDouble())
            cargoVolume = sc.nextDouble();
        if(sc.hasNextDouble())
        cargoValue = sc.nextDouble();
    }

    @Override
    public String toString() {
        return "Cargo Ship Name : " + super.toString() + ", Cargo Weight: " + cargoWeight + ", Cargo Volume: "
                + cargoVolume + ", Cargo Value: " + cargoValue;
    }

}