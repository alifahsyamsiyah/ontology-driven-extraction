import it.unibz.krdb.obda.io.ModelIOManager;
import it.unibz.krdb.obda.model.OBDAModel;
import it.unibz.krdb.obda.owlapi3.bootstrapping.DirectMappingBootstrapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.semanticweb.owlapi.io.FileDocumentTarget;
import org.semanticweb.owlapi.model.OWLOntology;

import GUI.BootstrapFrame;
import Model.AnnotationFile;
import Model.BootstrapFile;
import Model.OBDAFile;
import Model.OWLFile;

public class Bootstrap {
	@Plugin(
            name = "Ontology-Mapping Bootstrap Plugin", 
            parameterLabels = {}, 
            returnLabels = { "Ontology", "Mapping" }, 
            returnTypes = { OWLFile.class, OBDAFile.class }, 
            userAccessible = true, 
            help = "A plug-in for bootstrapping an ontology and a mapping, given a database"
    )
    @UITopiaVariant(
            affiliation = "Unibz", 
            author = "Alifah Syamsiyah", 
            email = "alifah.syamsiyah@stud-inf.unibz.it"
    )
	public static Object processMining(final UIPluginContext context) throws Exception {
		
		// JFrame
		
		BootstrapFrame bootFrame = new BootstrapFrame(context);
		InteractionResult result = context.showWizard("Enter your bootstrap configuration", true, true, bootFrame);
		
		if(result == InteractionResult.FINISHED) {

			String[] conf = bootFrame.getConfiguration();
			String owlFile = conf[0];
			String obdaFile = conf[1];
			String baseUri = conf[2];
			String jdbcUrl = conf[3];
			String jdbcUserName = conf[4];
			String jdbcPassword = conf[5]; 
			String jdbcDriverClass = conf[6]; 
			
			/*String owlFile = "/Users/user/Dropbox/Thesis/ProM/src/main/resources/example/ontology-boot-sakilas.owl";
			String obdaFile = "/Users/user/Dropbox/Thesis/ProM/src/main/resources/example/ontology-boot-sakilas.obda";
			String baseUri = "http://www.example.org/";
			String jdbcUrl = "jdbc:mysql://localhost/sakilas";
			String jdbcUserName = "root";
			String jdbcPassword = "root";
			String jdbcDriverClass = "com.mysql.jdbc.Driver"; */
			
			File owl = new File("src/main/resources/example/ontologytemp.owl");
			File obda = new File(obdaFile);
	
			
			// call ontop
			DirectMappingBootstrapper dm = new DirectMappingBootstrapper(baseUri, jdbcUrl, jdbcUserName, jdbcPassword, jdbcDriverClass);
			OBDAModel model = dm.getModel();
			OWLOntology onto = dm.getOntology();
			ModelIOManager mng = new ModelIOManager(model);
			mng.save(obda);
	        onto.getOWLOntologyManager().saveOntology(onto,
	                new FileDocumentTarget(owl));
	        
	        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/example/ontologytemp.owl"));
	        BufferedWriter writer = new BufferedWriter(new FileWriter(owlFile));
	        
	        writer.write(reader.readLine() + "\n");
	        writer.write("<!DOCTYPE rdf:RDF [\n"+
	        		"<!ENTITY owl \"http://www.w3.org/2002/07/owl#\" >\n"+
	        		"<!ENTITY xsd \"http://www.w3.org/2001/XMLSchema#\" >\n"+
	        		"<!ENTITY rdfs \"http://www.w3.org/2000/01/rdf-schema#\" >\n"+
	        		"<!ENTITY rdf \"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" >\n"+
	        		"]>\n");
	        
	        String line = "";
	        while((line=reader.readLine())!= null) {
	        	writer.write(line+"\n");
	        }
	        
	        writer.flush();
	        writer.close();
	        System.out.println("berhasil");
	        
	        // create the output file
	        OWLFile ontology = new OWLFile(owlFile);
	        OBDAFile mapping = new OBDAFile(obdaFile);
			
			
			return new Object[] { ontology, mapping };
		} else {
			return null;
		}
	}
		

}
