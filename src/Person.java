/* Course: CMSC 335
 * Assignment: Project 3 - SeaPort Program
 * File: Person.java
 * Author: Ngozi Young
 * Date: 10/27/2017
 * Modified Date: 12/03/2017
 * Purpose: To read in data and define elements of a person.
 */

import java.util.Scanner;

public class Person extends Thing {

    private String skill;
    boolean isAvailable = true;


    Person(Scanner sc) {
        // ThingKeyword name index parent skill
        // "person"     <string> <int> <int> <string>
        super(sc);
        skill = sc.next();
    }

    public boolean hasSkill(String requirement) {
        return requirement.toLowerCase().matches(skill.toLowerCase());
    }

    // Hires person with skill to complete one job at one time
    synchronized public Person hire() {
        if (isAvailable) {
            isAvailable = false;
            return this;
        }
        return null;
    }

    // Releases skilled person once job has been completed
    synchronized public void release() {
        if(!isAvailable)
            isAvailable = true;
    }

    // Getter method
    String getSkill() {
        return skill;
    }

    @Override
    public String toString() {
        return "Person Name: " + super.toString() + ", Skill: " + skill;
    }
}