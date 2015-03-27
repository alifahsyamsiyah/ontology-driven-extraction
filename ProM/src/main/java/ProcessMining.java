import java.io.BufferedReader;
import java.io.FileReader;
//import java.util.Scanner;
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
            parameterLabels = {}, 
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
	public static XLog processMining(final UIPluginContext context) throws Exception {	
		
		/***************** plugin  ***************/
		
		 InputFrame inputFrame = new InputFrame(context);
         InteractionResult result = context.showWizard("Enter your input files", true, true, inputFrame);
         QueryFrame qFrame = new QueryFrame(context, inputFrame.ontology,"secondmapping.obda");
		 result = context.showWizard("Enter your SPQRQL Query", true, true, qFrame);       
		 
		 if(result == InteractionResult.FINISHED) {
			 String sparqlQuery = 
						"PREFIX : <http://myproject.org/odbs#> \n" +
								"SELECT DISTINCT ?t ?vat ?e ?te ?ke ?ve \n"+
								"WHERE { ?t a :Trace ; :TcontainsA ?ta. ?ta :valueA ?vat ."
										+ "?t :TcontainsE ?e. ?e :EcontainsA ?a. ?a :typeA ?te ; :keyA ?ke; :valueA ?ve . }" +
							    "ORDER BY ?t ?e";
				
			// materialize event logs
			Query query = new Query("src/main/resources/example/"+inputFrame.ontology,"src/main/resources/example/secondmapping.obda");	
			String logs = query.runQuery(sparqlQuery);
	        return Extract.extractEventLogFile(logs);
		 } else {
			 return null;
		 }
		
	}
}
