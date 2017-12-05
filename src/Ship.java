/* Course: CMSC 335
 * Assignment: Project 3 - SeaPort Program
 * File: Ship.java
 * Author: Ngozi Young
 * Date: 10/27/2017
 * Modified Date: 12/03/2017
 * Purpose: To define elements of a Ship using data from input file.
 */

import java.util.ArrayList;
import java.util.Scanner;

class Ship extends Thing {

    private double draft;
    private double length;
    private double weight;
    private double width;
    private boolean jobInProcess;
    private final Object lock = new Object();
    private PortTime arrivalTime;
    private PortTime dockTime;

    ArrayList<Job> jobs;

    Ship(Scanner sc) {
        // ThingKeyword        name index parent(dock/port) weight length width draft
        // "pship" / "cship"    <string> <int> <int> <double> <double> <double> <double>
        super(sc);
        weight = sc.nextDouble();
        length = sc.nextDouble();
        width = sc.nextDouble();
        draft = sc.nextDouble();
        // Instantiates the object needed to organize job data.
        jobs = new ArrayList<>();
        dockTime = new PortTime();
    }

    // Checks that ship is docked before job can be assigned to persons
    public boolean isDocked() {
        if(parentThing instanceof SeaPort)
            ((SeaPort) parentThing).checkDocks();
        else
            ((Dock) parentThing).checkDock();
        return parentThing instanceof Dock && ((Dock) parentThing).getShip() == this;
    }

    // Keeps track of which jobs are in progress and ensures only one job is progressing in each dock
    public boolean doJob() {
        boolean doingJob = false;
        synchronized (lock) {
            if(!jobInProcess) {
                jobInProcess = true;
                doingJob = true;
            }
        }
        return doingJob;
    }

    // Checks that persons with the requested skill is available
    boolean requestSkilledPerson(ArrayList<String> requirements) {
        return ((Dock) parentThing).requestSkilledPerson(requirements);
    }

    // Returns the workers that are available to perform the job
    public ArrayList<Person> requestWorkers(ArrayList<String> requirements, Job j) {
        return ((Dock) parentThing).requestWorkers(requirements, j);

    }

    // Removes job from list once it is completed
    public void removeJob(Job job) {
        synchronized (lock) {
            jobInProcess = false;
        }
        if(jobs.size() > 0 && jobs.contains(job))
            jobs.remove(job);
        if(jobs.size() == 0)
            ((Dock) parentThing).leaveDock();
    }

    /* Getter methods */
    public double getDraft() {
        return draft;
    }

    public ArrayList<Job> getJobs() {
        return jobs;
    }

    public double getLength() {
        return length;
    }

    public double getWeight() {
        return weight;
    }

    public double getWidth() {
        return width;
    }

    public String toString() {
        return super.toString() + ", Weight: " + weight + ", Length: " + length + ", Width: " + width + ", Draft: "
                + draft;
    }
}