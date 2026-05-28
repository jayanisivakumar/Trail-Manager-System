package edu.ncsu.csc316.trail.ui;

import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.ncsu.csc316.trail.manager.ReportManager;

/**
 * The TrailManagerUI class is a simple command-line UI for TrailManager.
 * It allows users to interact with the system to get shortest distances 
 * and first aid locations.
 * 
 * @author Jayani Sivakumar
 * 
 */
public class TrailManagerUI {
	
    /**
     * Main method to start the program.
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {
    	Scanner scanner = new Scanner(System.in);
    	ReportManager reportManager;


        try {
            // asking user for input file paths
            System.out.print("Enter the path to the landmark file: ");
            String landmarkFile = scanner.nextLine();
            System.out.print("Enter the path to the trail file: ");
            String trailFile = scanner.nextLine();

            reportManager = new ReportManager(landmarkFile, trailFile);

        } catch (FileNotFoundException e) {
            System.out.println("Invalid file path received. Please provide a new valid file path to load");
            scanner.close();
            return;
        }

        // displaying the menu to the user
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. View distances to all reachable landmarks.");
            System.out.println("2. View a report of the proposed landmarks at which first aid stations should be located.");
            System.out.println("3. Exit TrailManager.");
            System.out.print("Enter your answer: ");
            
            String answer = scanner.nextLine();

            switch (answer) {
                case "1":
                	System.out.print("Enter the landmark ID: ");
                    System.out.println(reportManager.getDistancesReport(scanner.nextLine()));
                    break;
                case "2":
                	System.out.print("Enter the minimum number of intersecting trails: ");
                    int intersections = Integer.parseInt(scanner.nextLine());
                    System.out.println(reportManager.getProposedFirstAidLocations(intersections));
                    break;
                case "3":
                    System.out.println("Exiting TrailManager.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid, please enter 1, 2, or 3.");
            }
        }
    }
}