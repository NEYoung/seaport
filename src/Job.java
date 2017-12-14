/* Course: CMSC 335
 * Assignment: Project 3 - SeaPort Program
 * File: Job.java
 * Author: Ngozi Young
 * Date: 10/27/2017
 * Modified Date: 12/03/2017
 * Purpose: to define elements of a job in a ship. Implements a thread for each job representing a task that a ship
 *          requires.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Job extends Thing implements Runnable {

    private boolean goFlag = true;
    private boolean noKillFlag = true;
    private long jobTime;
    private ArrayList<String> requirements = new ArrayList<>(); // some of the skills of the persons
    private JButton jbGo = new JButton("Stop");
    private JButton jbKill = new JButton("Cancel");
    private Job tobeRemoved;
    private JPanel parentPanel;
    private JProgressBar pm = new JProgressBar();
    private Ship parentShip;
    private Status status = Status.WAITING;

    enum Status {RUNNING, SUSPENDED, WAITING, DONE};

    public Job(Scanner sc) {
        //job name     index parent jobTime [skill]+ (one or more, matches skill in person, may repeat)
        //job <string> <int> <int> <double> [<string>]+
        super(sc);
        jobTime = (long) sc.nextDouble();
        while(sc.hasNext()) {
            String requirement = sc.next();
            if(requirement != null && requirement.length() > 0)
             requirements.add(requirement);
        }
    }

    // Sets the parent of the job, and starts a new thread for each job
    @Override
    public void setParentThing(HashMap parentThing) {
        if(parentThing.get(getParent()) != null) {
            this.parentThing = (Thing) parentThing.get(getParent());
            parentShip = (Ship) this.parentThing;
            initJobElements();
            tobeRemoved = this;
            Thread thread = new Thread(this);
            thread.setName(this.getName());
            thread.start();

        }
    }

    // initializes the panel containing the jobs and its progress indicator
    private void initJobElements() {
        parentPanel = new JPanel();
        pm = new JProgressBar();
        pm.setStringPainted(true);
        jbGo.setMinimumSize(new Dimension(120,25));
        jbGo.setMaximumSize(new Dimension(120, 25));

        GroupLayout groupLayout = new GroupLayout(parentPanel);
        parentPanel.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        JLabel jLabel = new JLabel(parentShip.getName(), SwingConstants.CENTER);
        jLabel.setMinimumSize(new Dimension(150, 25));
        jLabel.setMaximumSize(new Dimension(150, 25));

        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addComponent(pm)
                .addComponent(jLabel)
                .addComponent(jbGo)
                .addComponent(jbKill));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(pm)
                .addComponent(jLabel)
                .addComponent(jbGo)
                .addComponent(jbKill));

        jbGo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleGoFlag();
            }
        });
        jbKill.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setKillFlag();
            }
        });
    }

    private void setKillFlag() {
        noKillFlag = false;
        jbKill.setBackground(Color.red);
    }

    private void toggleGoFlag() {
        goFlag = !goFlag;
    }

    // Shows the status of the job
    private void showStatus(Status st) {
        status = st;
        switch (status) {
            case RUNNING:
                jbGo.setBackground(Color.green);
                jbGo.setText("Running");
                break;
            case SUSPENDED:
                jbGo.setBackground(Color.yellow);
                jbGo.setText("Suspended");
                break;
            case WAITING:
                jbGo.setBackground(Color.orange);
                jbGo.setText("Waiting turn");
                break;
            case DONE:
                jbGo.setBackground(Color.red);
                jbGo.setText("Done");
                break;
        }
    }

    // Returns the jPanel for a single job
    public Component getContainerPanel() {
        return parentPanel;
    }

    @Override
    public void run() {
        while (!World.getInitStatus()) // Waits for world object to be initialized
            waitFor(300);
        while (!parentShip.isDocked()) {    // Waits if ship is not docked
//            showStatus(Status.WAITING);
            waitFor(100);
        }
        while(!parentShip.doJob()) {    // Waits if ship is docked but not yet being worked on
            waitFor(100);
        }
        ArrayList<Person> workers = null;
        if(requirements.size() == 0 || parentShip.requestSkilledPerson(requirements)) {
            if(requirements.size() != 0) {  // Job thread waits until the persons with the required skills are available.
                do {
                    waitFor(100);
                    workers = parentShip.requestWorkers(requirements, tobeRemoved);
                } while (workers == null || workers.size() != requirements.size());
            }
            long time = System.currentTimeMillis();
            long startTime = time;
            long stopTime = time + 1000 * jobTime;
            double duration = stopTime - time;
            // Simulates the progressing of the job
            while(time < stopTime && noKillFlag) {
                waitFor(10); // TODO: change back to 100 before submitting project
                if(goFlag) {
                    showStatus(Status.RUNNING);
                    time += 100;
                    pm.setValue((int) (((time - startTime) / duration) * 100));
                } else {
                    showStatus(Status.SUSPENDED);
                }
            }
            pm.setValue(100);
            showStatus(Status.DONE);
        } else {
            showStatus(Status.SUSPENDED);
        }
        if(workers != null && workers.size() > 0)
            parentShip.removeJob(tobeRemoved);
    }

    // Put job thread to sleep for a certain amount of time
    private void waitFor(long l) {
        try {
            Thread.sleep(l);
        } catch(InterruptedException e) {

        }
    }

    @Override
    public String toString() {
        return "Job Name: "  + super.toString() + ", Required Skills: " + requirements;
    }
}