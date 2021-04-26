package com.ikhokha.techcheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CommentAnalyzer {
	
	private File file;
	// making a generic algorithm checking normal comments
	private Map<String, String> commentsTypes ; 
	// check all length comments
	public CommentAnalyzer(File file, Map<String, String> commentsTypes) {
		this.file = file;
		// initialize the comment types handler
		this.commentsTypes = commentsTypes;
	}
	
	public Map<String, Integer> analyze() {
		
		Map<String, Integer> resultsMap = new HashMap<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			
			String line = null;
			while ((line = reader.readLine()) != null) {
				// we leave this since it is dealing with length check
				if (line.length() < 15) {
					incOccurrence(resultsMap, "SHORTER_THAN_15");
				}
				// check if comments contains any generic values
				for (Map.Entry<String, String> entry : commentsTypes.entrySet()) {
					// used case insensitive searching to get more accurate results
					if(line.toLowerCase().contains(entry.getValue().toLowerCase())) {
						incOccurrence(resultsMap,  entry.getKey());	
					}
				}
				
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + file.getAbsolutePath());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Error processing file: " + file.getAbsolutePath());
			e.printStackTrace();
		}
		
		return resultsMap;
		
	}
	
	/**
	 * This method increments a counter by 1 for a match type on the countMap. Uninitialized keys will be set to 1
	 * @param countMap the map that keeps track of counts
	 * @param key the key for the value to increment
	 */
	private void incOccurrence(Map<String, Integer> countMap, String key) {
		
		countMap.putIfAbsent(key, 0);
		countMap.put(key, countMap.get(key) + 1);
	}

}
