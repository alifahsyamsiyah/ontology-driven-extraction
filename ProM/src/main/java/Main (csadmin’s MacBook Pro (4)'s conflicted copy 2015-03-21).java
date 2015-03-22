import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.util.Date;

import Process.Annotation;
import Process.Query;
import EventLog.Extract;

public class Main {
	static String firstmappingFile;
	static String owlFile = "src/main/resources/example/ontology.owl";
	static String annotationFile;
	static BufferedReader ann; 
	
	public static void main(String[] args) throws Exception {
		/***************** another option: console ***************/
		
		Annotation annotation = new Annotation(owlFile, "src/main/resources/example/ontology.obda");		
		
		
		 // Read annotation from user and retrieve automatic mapping to event ontology
		 
		annotation.readAnnotation(new BufferedReader(new FileReader("src/main/resources/example/anno4.annotation")));
		
		
		 // Write the second mapping based on user annotation
		 
		annotation.writeAnnotation("src/main/resources/example/ontology.obda","src/main/resources/example/secondmapping.obda");
		
		
		 //  Query and Answer
		 
		Query query = new Query(owlFile, "src/main/resources/example/secondmapping.obda");	
		String sparqlQuery = 
				"PREFIX : <http://myproject.org/odbs#> \n" +
						"SELECT DISTINCT ?t ?vat ?e ?te ?ke ?ve ?ev\n"+
						"WHERE { ?t a :Trace ; :contain5 ?ta. ?ta :valueA ?vat ."
								+ "?t :contain2 ?e. ?e :contain3 ?a. ?a :typeA ?te ; :keyA ?ke; :valueA ?ve."
								+ "?ev :declare2 ?a . }" +
					    "ORDER BY ?t ?e";	
		
		
		String result = query.runQuery(sparqlQuery);
		Extract.extractEventLogFile(result);
		
		
		/***************** another option: GUI  ***************/
		
		/*	UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	        
	        SwingUtilities.invokeLater(new Runnable(){

	            public void run() {
	                InputFrame inputFrame = new InputFrame(context);
	                InteractionResult result = context.showWizard("Your dialog title", true, true, inputFrame);
	                
	                if (result == InteractionResult.FINISHED) {
	        			// Do the heavy lifting.
	        	    }
	                //inputFrame.setVisible(true);
	            }
	            
	        });*/
			
	}
}
