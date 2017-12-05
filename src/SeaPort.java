/* Course: CMSC 335
 * Assignment: Project 3 - SeaPort Program
 * File: SeaPort.java
 * Author: Ngozi Young
 * Date: 10/27/2017
 * Modified Date: 12/03/2017
 * Purpose: To define the elements of a SeaPort and display its data structure. Defines how workers are assigned
 *          to a job.
 */

import java.util.ArrayList;
import java.util.Scanner;

public class SeaPort extends Thing {
    private ArrayList<Job> jobRequestList = new ArrayList<>(); // stores the jobs for each ship

    ArrayList<Dock> docks = new ArrayList<>();
    ArrayList<Person> persons = new ArrayList<>();  // people with skills at this port
    ArrayList<Ship> que = new ArrayList<>();    // the list of ships waiting to dock
    ArrayList<Ship> ships = new ArrayList<>(); // a list of all the ships at this port

    SeaPort(Scanner sc) {
        // ThingKeyword    name index parent(null)
        // "port"          <string> <int> <int>
        super(sc);
    }

    // Checks dock for ship with an uncompleted task
    public void checkDocks() {
        for(Dock dock: docks) {
            dock.checkDock();
        }
    }

    // Retrieves ship pending in que
    public Ship getShipFromQue() {
        synchronized (this) {
            if(que.size() > 0) {
                Ship s = que.get(que.size() - 1);
                que.remove(s);
                return s;
            }
        }
        return null;
    }

    // Finds all the persons with matching skill for the job
    public ArrayList<Person> requestWorkers(ArrayList<String> requirements, Job job) {
        ArrayList<Person> requiredWorkers = new ArrayList<>();

        for(String requirement: requirements) {
            for(Person person: persons) {
                if(person.hasSkill(requirement))
                    synchronized (this) {
                        requiredWorkers.add(person.hire());
                    }
                // Checks that all persons for the job is available to work
                if(requiredWorkers.size() == requirements.size()) {
                    if(jobRequestList.contains(job)){
                        jobRequestList.remove(job);
                        return requiredWorkers;
                    }
                }
            }
        }
        // Adds jobs that are waiting for the available people to list
        if(!jobRequestList.contains(job))
            jobRequestList.add(job);
        if(requiredWorkers.size() > 0)
            releaseWorkers(requiredWorkers);
        return null;
    }

    // Release workers that have completed their task
    private void releaseWorkers(ArrayList<Person> workers) {
        for(Person worker: workers) {
            if(worker != null)
                synchronized (this) {
                    worker.release();
                }
        }
    }

    // Requests for persons with matching skill that is not already working on a task
    public boolean requestSkilledPerson(ArrayList<String> requirements) {
        ArrayList<Person> busyWorker = new ArrayList<>();
        for(String requirement: requirements) {
            for(Person person: persons) {
                if(person.hasSkill(requirement) && !busyWorker.contains(person))
                    busyWorker.add(person);
                // Checks that all the skills required for a job is available
                if(busyWorker.size() == requirements.size())
                    return true;
            }
        }
        return false;
    }

    /* Getter methods */
    public ArrayList<Dock> getDocks() {
        return docks;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public ArrayList<Ship> getQue() {
        return que;
    }

    public ArrayList<Ship> getShips() {
        return ships;
    }

    public String getText() {
        return "\n\nSeaPort Name: " + super.toString();
    }

    // Displays string representation of the seaport data structure
    @Override
    public String toString() {
        String st = "\n\nSeaPort: " + super.toString();

        for(Dock md: docks)
            st += "\n\n" + md;

        st += "\n\n --- List of all ships in que:";
        for(Ship ms: que)
            st += "\n > " + ms;

        st += "\n\n --- List of all ships:";
        for(Ship ms: ships) {
            st += "\n\n>" + ms;
            if (ms.jobs.size() == 0)
                st += "";
            for (Job mj : ms.jobs)
                st += "\n\t   - " + mj;
        }

        st += "\n\n --- List of all persons:";
        for(Person mp: persons)
            st += "\n > " + mp;

        return st;
    }

}