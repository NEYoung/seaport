/* Course: CMSC 335
 * Assignment: Project 4 - SeaPort Program
 * File: SeaPortProgram.java
 * Author: Ngozi Young
 * Date: 10/27/2017
 * Modified Date: 12/03/2017
 * Purpose: Launches a program that simulates aspects of a Sea Port. Reads data from a text file to
 *          create the internal data structure of a Sea Port which is displayed
 *          to screen in a simple text area field and in a tree. This program allows searches by name, index value
 *          or skill. The user is able to sort ships in que in ascending order by weight, length, width and draft.
 *          The progress of each task required by the ship is simulated using a thread and a progress bar. The user can
 *          click on the appropriate buttons to either suspend or cancel the task.
 *          Data file keywords: ports, docks, pships, cships, jobs, persons
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;

public class SeaPortProgram extends JFrame {

    private static final long serialVersionUID = 1L;

    private World world = new World(null);
    // HashMaps to increase the efficiency of retrieving things in the world by its index number
    private HashMap<Integer, Dock> hmd = new HashMap<>();
    private HashMap<Integer, SeaPort> hmsp = new HashMap<>();
    private HashMap<Integer, Ship> hms = new HashMap<>();
    private HashMap<Integer, Person> hmp = new HashMap<>();

    // variables used by the GUI interface
    private static final int WIDTH = 1100;
    private static final int HEIGHT = 745;
    private DefaultMutableTreeNode root;
    private JComboBox<String> searchJbc, sortJbc;
    private JTextArea outputTxtArea;
    private JTextField searchTxtField;
    private String[] searchOptions = {"name", "index", "skill"};
    private String[] sortOptions = {"draft", "length", "weight", "width"};

    public SeaPortProgram(String title) {
        super(title);
        // start at dot, the current directory
        JFileChooser fileChooser = new JFileChooser(".");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files only", "txt");
        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            readFile(file); // Reads and process data in file
            createJTree();  // Creates JTree for displaying file content
            initGUI(); // Displays the GUI
            // Displays the data structure of the world
            outputTxtArea.setText(">>>>> The world: \n\n" + world);
        }
    }

    // Reads and process a text file to create the data structure in the world
    private void readFile(File file) {
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while((line = br.readLine()) != null) {
                Scanner sc = new Scanner(line.trim());
                // Ignores lines containing comments
                if(!line.startsWith("//")) {
                    String keyword = "";
                    if(sc.hasNext()) {
                        // keyword is the first word of each line in the data file
                        // which is converted to lower case to ignore case sensitivity.
                        keyword = sc.next().toLowerCase();
                    }

                    /* - Uses the keyword to determine which class constructor is invoked to initialize the class fields.
                       - Adds elements to Hashmap for efficient sorting.
                       - Adds or assigns the Seaport objects to its appropriate ArrayList
                    */
                    if(keyword.matches("port")) {
                        SeaPort sp = new SeaPort(sc);
                        hmsp.put(sp.getIndex(), sp);
                        world.addPort(sp);
                    } else if(keyword.matches("dock")) {
                        Dock d = new Dock(sc);
                        hmd.put(d.getIndex(), d);
                        world.assignDock(d, hmsp);
                    } else if(keyword.matches("pship")) {
                        Ship s = new PassengerShip(sc);
                        hms.put(s.getIndex(), s);
                        world.assignShip(s, hmsp, hmd);
                    } else if(keyword.matches("cship")) {
                        Ship s = new CargoShip(sc);
                        hms.put(s.getIndex(), s);
                        world.assignShip(s, hmsp, hmd);
                    } else if(keyword.matches("person")) {
                        Person p = new Person(sc);
                        hmp.put(p.getIndex(), p);
                        world.assignPerson(p, hmsp);
                    } else if(keyword.matches("job")) {
                        Job j = new Job(sc);
                        world.assignJob(j, hms);
                    }
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    // Creates a JTree for displaying the data structure of the sea world
    private void createJTree() {
        root = new DefaultMutableTreeNode("World");
        DefaultMutableTreeNode node, dockNode, portNode, queShipNode, shipNode;
        DefaultMutableTreeNode worldNode = new DefaultMutableTreeNode("Ports");
        root.add(worldNode);
        for(SeaPort sp: world.getPorts()) {
            portNode = new DefaultMutableTreeNode(sp.getText());
            worldNode.add(portNode);
            node = new DefaultMutableTreeNode("Docks");
            for(Dock d: sp.getDocks()) {
                dockNode = new DefaultMutableTreeNode(d.getText());
                dockNode.add(new DefaultMutableTreeNode(d.getShip()));
                node.add(dockNode);
                portNode.add(node);
            }
            node = new DefaultMutableTreeNode("Queued Ships");
            for(Ship qs: sp.getQue()) {
                queShipNode = new DefaultMutableTreeNode(qs);
                node.add(queShipNode);
                portNode.add(node);
            }
            node = new DefaultMutableTreeNode("All Ships");
            for(Ship s: sp.getShips()) {
                shipNode = new DefaultMutableTreeNode(s);
                node.add(shipNode);
                // Node: jobs needed for each ship
                for(Job j: s.getJobs()) {
                    shipNode.add(new DefaultMutableTreeNode(j));
                }
                portNode.add(node);
            }
            node = new DefaultMutableTreeNode("Persons");
            for(Person p: sp.getPersons()) {
                node.add(new DefaultMutableTreeNode(p));
                portNode.add(node);
            }
        }
    }

    // Creates and displays the program's GUI
    private void initGUI() {
        JPanel searchPanel = new JPanel();
        JLabel searchLbl = new JLabel("Search for: ");
        searchJbc = new JComboBox<>(searchOptions);
        searchTxtField = new JTextField(25);
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> searchActionPerformed(e));
        searchPanel.add(searchLbl);
        searchPanel.add(searchJbc);
        searchPanel.add(searchTxtField);
        searchPanel.add(searchBtn);

        JPanel sortPanel = new JPanel();
        JLabel sortLbl = new JLabel("Sort by: ");
        sortJbc = new JComboBox<>(sortOptions);
        JButton sortBtn = new JButton("Sort");
        sortBtn.addActionListener(e -> sortActionPerformed(e));
        sortPanel.add(Box.createRigidArea(new Dimension(310, 0)));
        sortPanel.add(sortLbl);
        sortPanel.add(sortJbc);
        sortPanel.add(sortBtn);

        outputTxtArea = new JTextArea(20, 25);
        JScrollPane outputJtaJsp = new JScrollPane(outputTxtArea);

        JTree tree = new JTree(root);
        JScrollPane treeJsp = new JScrollPane(tree);
        // Combined JPanels for search, sort, text area, and JTree components into one JPanel
        JPanel groupPanel = new JPanel();
        groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
        groupPanel.add(searchPanel);
        groupPanel.add(sortPanel);
        groupPanel.add(outputJtaJsp);
        groupPanel.add(treeJsp);

        // JPanel for displaying job progress
        JPanel jobsPanel = new JPanel();
        jobsPanel.setLayout(new BoxLayout(jobsPanel, BoxLayout.Y_AXIS));
        boolean hasJob = false;
        // Displays a JPanel for each task
        for(SeaPort sp: world.getPorts()) {
            for(Ship s: sp.getShips()) {
                for(Job j: s.getJobs()) {
                    jobsPanel.add(j.getContainerPanel());
                    hasJob = true;
                }
            }
        }
        JScrollPane jobsJsp = new JScrollPane(new JLabel("No jobs found!", SwingConstants.CENTER));
        if(hasJob)
            jobsJsp = new JScrollPane(jobsPanel);
        // To split the panel holding the job progress from the panel displaying the data content
        JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, groupPanel, jobsJsp);
        add(jSplitPane,BorderLayout.CENTER);

        setVisible(true);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        World.setInitStatus(true);
    }

    // Searches all SeaPorts based on the user selected option
    private void searchActionPerformed(ActionEvent e) {
        String inputTxt = searchTxtField.getText().toLowerCase();
        String searchByJcb = searchJbc.getSelectedItem().toString();
        String result = "";
        outputTxtArea.setText("");
        switch (searchByJcb) {
            case "name":
                result = world.searchByName(inputTxt);
                break;
            case "index":
                result = world.searchByIndex(inputTxt, hmsp, hmd, hms, hmp);
                break;
            case "skill":
                result = world.searchBySkill(inputTxt);
                break;
            default:
                break;
        }
        // Displays search result to the GUI's text area
        outputTxtArea.setText(result);
    }

    // Sorts all things in the world by name in its correct parent list
    // and sorts all ships in its port que by the user selected option
    private void sortActionPerformed(ActionEvent e) {
        String sortByJcb = sortJbc.getSelectedItem().toString();

        // Displays the sorted data
        outputTxtArea.setText(">>>>> The world sorted by name, and ships in que sorted by " + sortByJcb + ":");


        Collections.sort(world.getPorts());     // ports sorted by name
        for(SeaPort sp: world.getPorts()) {
            Collections.sort(sp.getDocks());    // docks sorted by name
            Collections.sort(sp.getShips());    // ships sorted by name
            // Ships in its que sorted by the user selected option
            Collections.sort(sp.getQue(), new ShipComparator(sortByJcb));
            Collections.sort(sp.getPersons());  // persons sorted by name
        }

        // Displays new sorted list
        outputTxtArea.append(world.toString());
    }

    public static void main(String[] arg) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SeaPortProgram("SeaPort Program");
            }
        });
        
    }

}