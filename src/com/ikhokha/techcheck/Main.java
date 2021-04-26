package com.ikhokha.techcheck;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Main implements Runnable {
	// catering for threading
     private File selectedFile;
     private Map<String, Integer> totalResults;
     private Map<String, String>  commentsTypes;
     
     // catering for threading
     public  Main(File newFile, Map<String, Integer> totalResults,Map<String, String>  commentsTypes ) {
    	 this.selectedFile = newFile;
    	 this.totalResults = totalResults;
    	 this.commentsTypes = commentsTypes;
     }
     
	public static void main(String[] args) {
		
		Map<String, Integer> totalResults = new HashMap<>();
		Map<String, String>  commentsTypes = new HashMap<>();
		// add any case you want to find in the comments here
		commentsTypes.put("MOVER_MENTIONS", "Mover");
		commentsTypes.put("SHAKER_MENTIONS", "Shaker");
		// new metrics to be added 
		commentsTypes.put("QUESTIONS", "?");
		commentsTypes.put("SPAM", "http");
		
				
		File docPath = new File("docs");
		File[] commentFiles = docPath.listFiles((d, n) -> n.endsWith(".txt"));
		Integer fileSize = commentFiles.length;
		Thread[] fileThreads = new Thread[fileSize];
		  for(int a = 0; a < fileThreads.length; a++) {
	            fileThreads[a] = new Thread(new Main(commentFiles[a],totalResults, commentsTypes));
	            fileThreads[a].start();
	            try {
					fileThreads[a].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        }
		
		
		
		System.out.println("\nRESULTS\n=======");
		totalResults.forEach((k,v) -> System.out.println(k + " : " + v));
	}
	
	/**
	 * This method adds the result counts from a source map to the target map 
	 * @param source the source map
	 * @param target the target map
	 */
	private static void addReportResults(Map<String, Integer> source, Map<String, Integer> target) {

		for (Map.Entry<String, Integer> entry : source.entrySet()) {
			// bug fixes
			Integer total = entry.getValue();
			if(target.containsKey(entry.getKey())) {
				total += target.get(entry.getKey());
			}
			target.put(entry.getKey(), total);
		}
		
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
	    File commentFile = this.selectedFile;
	    System.out.println("Running thread on File "+ commentFile.getName());
	    try {
	    	// comments analyzer can now cater for all the cases
			CommentAnalyzer commentAnalyzer = new CommentAnalyzer(commentFile, commentsTypes);
			Map<String, Integer> fileResults = commentAnalyzer.analyze();
			addReportResults(fileResults, totalResults);
			 // trying to make sure the many threads don't kill the program
	         Thread.sleep(60);
	      } catch (InterruptedException e) {
	         System.out.println("Thread has interrupted");
	      }
	}

}
