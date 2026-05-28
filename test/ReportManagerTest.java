package edu.ncsu.csc316.trail.manager;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.Test;

/**
 * Test class for ReportManager.
 * 
 * @author Jayani Sivakumar
 */
public class ReportManagerTest {
    
	/** ReportManager instance used for testing. */
    private ReportManager reportManager;
    
    /**
     * Reads content of a file as a string.
     * 
     * @param filePath path to the file
     * @return file content as string
     * @throws FileNotFoundException if file is not found
     */
    private String readFile(String filePath) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filePath));
        StringBuilder content = new StringBuilder();
        while (scanner.hasNextLine()) {
            content.append(scanner.nextLine()).append("\n");
        }
        scanner.close();
        return content.toString().trim();
    }

    /**
     * Tests the getDistancesReport method.
     * 
     * @throws FileNotFoundException if input files are not found
     */
    @Test
    public void testGetDistancesReport() throws FileNotFoundException {
    	reportManager = new ReportManager("input/landmarkInfo.txt", "input/trails.txt");
        String report = reportManager.getDistancesReport("L02");
        String expected = readFile("output/landmarkDistances.txt");
        assertEquals(expected, report);
    }

    /**
     * Tests getProposedFirstAidLocations method.
     * 
     * @throws FileNotFoundException if input files are not found
     */
    @Test
    public void testGetProposedFirstAidLocations() throws FileNotFoundException {
    	reportManager = new ReportManager("input/landmarkInfo.txt", "input/trails.txt");
        String report = reportManager.getProposedFirstAidLocations(1);
        String expected = readFile("output/firstAidLocations.txt");
        assertEquals(expected, report);
    }
    
    /**
     * Tests ReportManager using custom input test files.
     * 
     * @throws FileNotFoundException  if input files are missing
     */
    @Test
    public void testReportManagerScenarios() throws FileNotFoundException {
    	reportManager = new ReportManager("input/sgLandmarks.txt", "input/sgTrails.txt");
        // testing invalid landmark
        assertEquals("The provided landmark ID (SG47) is invalid for the park.",
                     reportManager.getDistancesReport("SG47"));

        // testing no reachable landmarks
        assertEquals("No landmarks are reachable from Isolated Landmark (SG99).",
                     reportManager.getDistancesReport("SG99"));

        // testing the report of getting distances
        String report = reportManager.getDistancesReport("SG01");
        String expected = readFile("output/sgDistances.txt");
        assertEquals(expected, report);

        // testing the report of first aid locations
        String firstAidReport = reportManager.getProposedFirstAidLocations(2);
        String expectedFirstAid = readFile("output/sgFirstAid.txt");
        assertEquals(expectedFirstAid, firstAidReport);

        // testing no first aid locations
        assertEquals("No landmarks have at least 10 intersecting trails.",
                     reportManager.getProposedFirstAidLocations(10));

        // testing invalid
        assertEquals("Number of intersecting trails must be greater than 0.",
                     reportManager.getProposedFirstAidLocations(-1));
    }
}

