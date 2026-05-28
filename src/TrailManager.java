package edu.ncsu.csc316.trail.manager;

import java.io.FileNotFoundException;

import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.dsa.map.Map.Entry;
import edu.ncsu.csc316.dsa.queue.Queue;
import edu.ncsu.csc316.trail.data.Landmark;
import edu.ncsu.csc316.trail.data.Trail;
import edu.ncsu.csc316.trail.dsa.DSAFactory;
import edu.ncsu.csc316.trail.dsa.DataStructure;
import edu.ncsu.csc316.trail.io.TrailInputReader;

/**
 * The Trail Manager class manages landmark and trail data for the trail system.
 * It provides methods to compute shortest distances and identify first aid locations.
 * 
 * @author Jayani Sivakumar
 * 
 */
public class TrailManager {
    
    /** Map storing landmark IDs and their corresponding landmark */
    private Map<String, Landmark> landmarks;
    
    /** Map storing landmarks and their list of trails */
    private Map<Landmark, List<Trail>> trails;

    /**
     * Constructs TrailManager and loads data from specified files.
     * 
     * @param pathToLandmarkFile the path to the landmark input file
     * @param pathToTrailFile the path to the trail input file
     * @throws FileNotFoundException if the file paths are invalid
     */
    public TrailManager(String pathToLandmarkFile, String pathToTrailFile) throws FileNotFoundException {     
    	// setting up the data structures to be used
    	DSAFactory.setMapType(DataStructure.SKIPLIST);
        DSAFactory.setListType(DataStructure.ARRAYBASEDLIST);
        
        // reading from the files
        List<Landmark> landmarkList = TrailInputReader.readLandmarks(pathToLandmarkFile);
        List<Trail> trailList = TrailInputReader.readTrails(pathToTrailFile);
        
        // initializing the maps
        this.landmarks = DSAFactory.getMap(null);
        this.trails = DSAFactory.getMap(null);
        
        // populating landmarks
        for (Landmark landmark : landmarkList) {
            landmarks.put(landmark.getId(), landmark);
            trails.put(landmark, DSAFactory.getIndexedList());
        }

        // populating trails
        for (Trail trail : trailList) {
            Landmark l1 = landmarks.get(trail.getLandmarkOne());
            Landmark l2 = landmarks.get(trail.getLandmarkTwo());

            if (l1 != null && l2 != null) {
                trails.get(l1).add(trails.get(l1).size(), trail);
                trails.get(l2).add(trails.get(l2).size(), trail);
            }
        }
    }
    
    /**
     * Computes the shortest distances from a given landmark to all reachable landmarks.
     * 
     * @param originLandmark ID of the starting landmark
     * @return map containing landmarks and their shortest distances from the starting landmark
     */
    public Map<Landmark, Integer> getDistancesToDestinations(String originLandmark) {
    	//Initialize empty map
    	Map<Landmark, Integer> distances = DSAFactory.getMap(null);
        // using our circular ArrayBasedQueue
    	//queue will hold landmarks that need to be visited
        Queue<Landmark> queue = DSAFactory.getQueue();
        Landmark start = landmarks.get(originLandmark);

        // invalid landmark
        if (start == null) {
        	return distances;
        }

        //place starting landmark with distance 0 in it
        //queue will hold landmarks that need to be visited
        distances.put(start, 0);
        queue.enqueue(start);

        while (!queue.isEmpty()) {
        	//get currentLandmark from queue	
            Landmark currentLandmark = queue.dequeue();
            
            //get list for specific landmark
            List<Trail> newList = trails.get(currentLandmark); 
            int n = newList.size();
            
            //get distance of currentLandmark in map
            int currentDistance = distances.get(currentLandmark);
            
            //traverse through list for currentLandMark 
            for (int i = 0; i < n; i++) {
            	// getting the trail object
            	Trail trail = newList.get(i);
            	// getting the landmark ID
            	String adjacentID = trail.getLandmarkOne().equals(currentLandmark.getId()) ? 
                        trail.getLandmarkTwo() : trail.getLandmarkOne();

                // converting ID to Landmark object
                Landmark adjacent = getLandmarkByID(adjacentID);
 
                // skipping if landmark is not found
                if (adjacent == null) {
                	continue; 
                }

                // obtaining the distance of trail
                int distance = trail.getLength() + currentDistance;

                //add landmark if not already in map or with new distance to map if shorter distance found
                Integer existingDistance = distances.get(adjacent);
                if (existingDistance == null || distance < existingDistance) {
                    distances.put(adjacent, distance);
                    queue.enqueue(adjacent);
                }
            }
        }
        return distances;
    }
    
    /**
     * Getting landmark based on ID.
     * 
     * @param landmarkID ID of the landmark
     * @return landmark object, else null
     */
    public Landmark getLandmarkByID(String landmarkID) {
        Landmark landmark = landmarks.get(landmarkID);
        return landmark;
    }
    
    /**
     * Searches landmarks that are proposed first aid locations that
     * have at least the specified number of intersecting trails.
     * 
     * @param numberOfIntersectingTrails minimum number of intersecting trails required
     * @return map containing landmarks and their trails
     */
    public Map<Landmark, List<Trail>> getProposedFirstAidLocations(int numberOfIntersectingTrails) {
        Map<Landmark, List<Trail>> proposedLocations = DSAFactory.getMap(null);

        for (Entry<Landmark, List<Trail>> entry : trails.entrySet()) {
        	// getting the landmark
            Landmark landmark = entry.getKey();
            // getting the list of trails for the landmark
            List<Trail> trailList = entry.getValue();

            if (trailList.size() >= numberOfIntersectingTrails) {
                proposedLocations.put(landmark, trailList);
            }
        }
        return proposedLocations;
    }

}

