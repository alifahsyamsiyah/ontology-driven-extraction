import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import org.deckfour.xes.extension.XExtension;
import org.deckfour.xes.extension.XExtensionManager;
import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.factory.XFactoryBufferedImpl;
import org.deckfour.xes.model.XAttribute;

import Process.Annotation;
import Process.Query;
import EventLog.Extract;

public class Main {
	static String firstmappingFile;
	static String owlFile = "src/main/resources/example/ontology-wil.owl";
	static String annotationFile;
	static BufferedReader ann; 
	
	
	public static void main(String[] args) throws Exception {
		/***************** another option: console ***************/
		long startTime = System.nanoTime();
		
		BufferedReader doReader = new BufferedReader(new FileReader(owlFile));
		BufferedReader eoReader = new BufferedReader(new FileReader("src/main/resources/example/"+"eventontology.owl"));
		
		String combinedonto = "src/main/resources/example/"+"combinedonto.owl";
		
		BufferedWriter coWriter = new BufferedWriter(new FileWriter(combinedonto));
		
		String line = ""; String line2 = ""; String prefix = "";
		
		// write the header
		/*coWriter.write(doReader.readLine() + "\n");
		while(!(line2 = eoReader.readLine()).equals("")) {
			coWriter.write(line2 + "\n");
		}
		line = doReader.readLine();
		prefix = line.substring(16, line.length()-1);
		coWriter.write(line + "\n");*/
		
		while((line = doReader.readLine()) != null) {
			// to get prefix
			if(line.contains("xmlns="))
				prefix = line.substring(16, line.length()-1);
			
			// object properties
			if(line.equals("    // Object Properties")) {
				coWriter.write(line + "\n");
				while(!(line = doReader.readLine()).equals("    <!-- ")) {
					coWriter.write(line + "\n");
				}
				
				while(!(line2 = eoReader.readLine()).equals("// Data properties")) {
					line2 = line2.replace("http#", prefix);
					coWriter.write(line2 + "\n");
				}
				
				coWriter.write("    <!-- " + "\n");
			}
			
			// data properties
			else if(line.equals("    // Data properties")) {
				coWriter.write(line + "\n");
				while(!(line = doReader.readLine()).equals("    <!-- ")) {
					coWriter.write(line + "\n");
				}
				
				while(!(line2 = eoReader.readLine()).equals("// Classes")) {
					line2 = line2.replace("http#", prefix);
					coWriter.write(line2 + "\n");
				}
				
				coWriter.write("    <!-- " + "\n");
			}
			
			// classes
			else if(line.equals("    // Classes")) {
				coWriter.write(line + "\n");
				while(!(line = doReader.readLine()).equals("</rdf:RDF>")) {
					coWriter.write(line + "\n");
				}
				
				while((line2 = eoReader.readLine()) != null) {
					line2 = line2.replace("http#", prefix);
					coWriter.write(line2 + "\n");
				}
				
				coWriter.write("</rdf:RDF>" + "\n");
			}
			
			// else
			else {
				coWriter.write(line + "\n");
			}
		}
		
		coWriter.flush();
		coWriter.close();
		doReader.close();
		eoReader.close();
		
		Annotation annotation2 = new Annotation(combinedonto, "src/main/resources/example/ontology-wil.obda", "src/main/resources/example/anno21-wil.annotation", "src/main/resources/example/secondmapping.obda");
		
		annotation2.readAnnotation();
		
		//Annotation annotation = new Annotation(owlFile, "src/main/resources/example/ontology3.obda");		
		//annotation.readAnnotation(new BufferedReader(new FileReader("src/main/resources/example/anno3.annotation")));
		//annotation.writeAnnotation("src/main/resources/example/ontology3.obda","src/main/resources/example/secondmapping.obda");
		
		
		 //  Query and Answer
		 
		Query query = new Query(combinedonto, "src/main/resources/example/secondmapping.obda");	
		String sparqlQuery = 
				"PREFIX : <"+prefix+"> \n" +
						"SELECT DISTINCT ?t ?vat ?e ?te ?ke ?ve \n"+
						"WHERE { ?t :TcontainsA ?ta. ?ta :valueA ?vat ."
								+ "?t :TcontainsE ?e. ?e :EcontainsA ?a. ?a :typeA ?te ; :keyA ?ke; :valueA ?ve. }"; 
		
		String traces = 
				"PREFIX : <"+prefix+"> \n" +
						"SELECT ?t ?vat ?e \n"+
						"WHERE { ?t :TcontainsA ?ta. ?ta :valueA ?vat ."
								+ "?t :TcontainsE ?e . }"; 
		
		String eventtype = 
				"PREFIX : <"+prefix+"> \n" +
						"SELECT ?e ?te \n"+
						"WHERE { ?e :EcontainsA ?a. ?a :typeA ?te . }";
		
		String eventkey = 
				"PREFIX : <"+prefix+"> \n" +
						"SELECT ?e ?ke \n"+
						"WHERE { ?e :EcontainsA ?a. ?a :keyA ?ke . }";
		
		String eventvalue = 
				"PREFIX : <"+prefix+"> \n" +
						"SELECT ?e ?ve \n"+
						"WHERE { ?e :EcontainsA ?a. ?a :valueA ?ve. }";
		
		String tes = 
				"PREFIX : <"+prefix+"> \n" +
						"SELECT DISTINCT ?t \n"+
						"WHERE { ?t a :Trace . }"; 
		
		//String result = query.runQuery(tes);
		
//		String resulttrace = query.runQuery(traces);
//		String resulttype = query.runQuery(eventtype);
//		String resultkey = query.runQuery(eventkey);
//		String resultvalue = query.runQuery(eventvalue);
//		Extract.extractEventLogFile(resulttrace, resulttype, resultkey, resultvalue);
		
		HashMap<String, LinkedList<String>> resulthashtrace = query.runQueryTrace(traces);
		HashMap<String, LinkedList<String>> resulthashtype = query.runQueryEvent(eventtype);
		HashMap<String, LinkedList<String>> resulthashkey = query.runQueryEvent(eventkey);
		HashMap<String, LinkedList<String>> resulthashvalue = query.runQueryEvent(eventvalue);
		Extract.extractEventLog(resulthashtrace, resulthashtype, resulthashkey, resulthashvalue);
		
		//String result = query.runQuery(sparqlQuery);
		//Extract.extractEventLogFile(result);

		long endTime = System.nanoTime();
		long duration = (endTime - startTime); 
		System.out.println("Duration: " + duration + " s");
		
		
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
