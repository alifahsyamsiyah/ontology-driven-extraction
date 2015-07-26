import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;

import EventLog.Extract;
import GUI.InputFrame;
import GUI.QueryFrame;
import Model.AnnotationFile;
import Model.OWLFile;
import Model.OBDAFile;
import Process.Annotation;
import Process.Query;

public class ProcessMining {
	static String firstmappingFile;
	static String owlFile = "src/main/resources/example/ontology.owl";
	static String annotationFile;
	static BufferedReader ann; 
	
	/**
	 * The main method.
	 * Given a fix database and ontology,
	 * it retrieves user's first mapping (from database to domain ontology)
	 * and user's annotation.
	 * Then it calls class Annotation to build second mapping
	 * (from domain to event ontology) automatically.
	 * In the end, it test the system by Sparql Query.
	 * 
	 * @param args
	 */
	
	/**
	 * @param context
	 * @return
	 * @throws Exception
	 */
	@Plugin(
            name = "Ontology-Driven Extraction Plugin", 
            parameterLabels = {"Domain Ontology", "First Mapping", "Annotation"}, 
            returnLabels = { "XES Event Log" }, 
            returnTypes = { XLog.class }, 
            userAccessible = true, 
            help = "Ontology-driven extraction of event logs from relational databases"
    )
    @UITopiaVariant(
            affiliation = "Unibz", 
            author = "Alifah Syamsiyah", 
            email = "alifah.syamsiyah@stud-inf.unibz.it"
    )
	public static XLog processMining(final UIPluginContext context, OWLFile domainOntology, OBDAFile firstMapping, AnnotationFile annotation) throws Exception {	
		
		/***************** plugin  ***************/
		
		 //InputFrame inputFrame = new InputFrame(context);
         //InteractionResult result = context.showWizard("Enter your input files", true, true, inputFrame);
		
		
		// take the inputs
		String domainonto = domainOntology.getFile();
		String firstmap = firstMapping.getFile();
		String anno = annotation.getFile();
		
		String path = "src/main/resources/example/";
		
		// combine the event ontology
		
		BufferedReader doReader = new BufferedReader(new FileReader(domainonto));
		BufferedReader eoReader = new BufferedReader(new FileReader(path+"eventontology.owl"));
		
		String combinedonto = path+"combinedonto.owl";
		
		BufferedWriter coWriter = new BufferedWriter(new FileWriter(combinedonto));
		
		String line = ""; String line2 = ""; String prefix = "";
		
		// write the header
	/*	coWriter.write(doReader.readLine() + "\n");
		while(!(line2 = eoReader.readLine()).equals("")) {
			coWriter.write(line2 + "\n");
		}
		line = doReader.readLine();
		prefix = line.substring(16, line.length()-1);
		coWriter.write(line + "\n");*/
		
		// write the rest
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
		
		// call annotation
		
		Annotation annotationProcess = new Annotation(combinedonto, firstmap, anno, path+"secondmapping.obda");		
		
		annotationProcess.readAnnotation();
		
        QueryFrame qFrame = new QueryFrame(context, combinedonto, path+"secondmapping.obda");
        InteractionResult result = context.showWizard("Enter your SPARQL Query", true, true, qFrame);       
		 
		 if(result == InteractionResult.FINISHED) {
			 String sparqlQuery = 
						"PREFIX : <"+prefix+"> \n" +
								"SELECT DISTINCT ?t ?vat ?e ?te ?ke ?ve \n"+
								"WHERE { ?t :TcontainsA ?ta. ?ta :valueA ?vat ."
										+ "?t :TcontainsE ?e. ?e :EcontainsA ?a. ?a :typeA ?te ; :keyA ?ke; :valueA ?ve . }"; 
			 String traces = 
						"PREFIX : <"+prefix+"> \n" +
								"SELECT DISTINCT ?t ?vat ?e \n"+
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
				
			// materialize event logs
			Query query = new Query(combinedonto,path+"secondmapping.obda");	
			
			HashMap<String, LinkedList<String>> resulttrace = query.runQueryTrace(traces);
			HashMap<String, LinkedList<String>> resulttype = query.runQueryEvent(eventtype);
			HashMap<String, LinkedList<String>> resultkey = query.runQueryEvent(eventkey);
			HashMap<String, LinkedList<String>> resultvalue = query.runQueryEvent(eventvalue);
			
	        return Extract.extractEventLog(resulttrace, resulttype, resultkey, resultvalue);
		 } else {
			 return null;
		 }
		
	}
}
