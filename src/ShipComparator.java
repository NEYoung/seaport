/* Course: CMSC 335
 * Assignment: Project 2 - SeaPort Program
 * File: ShipComparator.java
 * Author: Ngozi Young
 * Date: 11/19/2017
 * Purpose: To sort ships by the user selected option in ascending order
 */

import java.util.Comparator;

public class ShipComparator implements Comparator<Ship> {
    private String sortByOption;

    ShipComparator(String sortByOption) {
        this.sortByOption = sortByOption;
    }

    @Override
    // Sorts ships in ascending order
    public int compare(Ship s1, Ship s2) {
        double compareVal;
        int result = -1;
        switch(sortByOption) {
            case "draft":
                compareVal = s1.getDraft() - s2.getDraft();
                result = evaluate(compareVal);
                break;
            case "length":
                compareVal = s1.getLength() - s2.getLength();
                result = evaluate(compareVal);
                break;
            case "weight":
                compareVal = s1.getWeight() - s2.getWeight();
                result = evaluate(compareVal);
                break;
            case "width":
                compareVal = s1.getWidth() - s2.getWidth();
                result = evaluate(compareVal);
                break;
        }
        return result;
    }

    // Evaluates the comparison value of two ships
    private int evaluate(double compareVal) {
        if(compareVal < 0) {
            return -1;
        } else if(compareVal == 0) {
            return 0;
        } else if(compareVal > 0) {
            return 1;
        }
        return -1;
    }

}