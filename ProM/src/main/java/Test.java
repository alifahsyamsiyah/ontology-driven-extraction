import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import it.unibz.krdb.obda.io.ModelIOManager;
import it.unibz.krdb.obda.model.OBDAModel;
import it.unibz.krdb.obda.owlapi3.bootstrapping.DirectMappingBootstrapper;

import org.deckfour.xes.factory.XFactoryBufferedImpl;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeMapImpl;
import org.semanticweb.owlapi.io.FileDocumentTarget;
import org.semanticweb.owlapi.model.OWLOntology;


public class Test {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		

        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/example/ontologytemp.owl"));
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/example/test.owl"));
		
		
		/*File owl = new File("/Users/user/Dropbox/Thesis/OntopCLIfiles/ontology5.owl");
        File obda = new File("/Users/user/Dropbox/Thesis/OntopCLIfiles/ontology-conf.obda");
		String baseUri = "http://www.semanticweb.org/user/ontologies/";
		String jdbcUrl = "jdbc:mysql://localhost/conference10";
		String jdbcUserName = "root";
		String jdbcPassword = "root";
		String jdbcDriverClass = "com.mysql.jdbc.Driver";
		
		DirectMappingBootstrapper dm = new DirectMappingBootstrapper(baseUri, jdbcUrl, jdbcUserName, jdbcPassword, jdbcDriverClass);
		OBDAModel model = dm.getModel();
		OWLOntology onto = dm.getOntology();
		ModelIOManager mng = new ModelIOManager(model);
		mng.save(obda);
        onto.getOWLOntologyManager().saveOntology(onto,
                new FileDocumentTarget(owl));*/
		
		
		/*
		XFactoryBufferedImpl factory = new XFactoryBufferedImpl();	
		
		XAttribute attr = factory.createAttributeLiteral("concept:name", "tracevalue", null); // create attribute for trace
		XAttributeMap attrMapTrace = new XAttributeMapImpl(); // create a new map attribute
		attrMapTrace.put("concept:name", attr);	// put attribute to the map attribute
		XTrace xtrace = factory.createTrace(attrMapTrace); // create xtrace
		
		XAttributeMap attrMap = new XAttributeMapImpl(); // create a new map attribute
		attr = factory.createAttributeLiteral("key1", "value", null); // create attribute		
		//attr = factory.createAttributeLiteral("key2", "value", null); // create attribute
		attrMap.put("key1", attr); // put attribute to the map attribute
		//attrMap.put("key2", attr);
		XEvent xevent = factory.createEvent(attrMap); // create xevent
		xtrace.insertOrdered(xevent); // insert event in correct order in a trace
		
		attrMap = new XAttributeMapImpl(); // create a new map attribute
		attr = factory.createAttributeLiteral("key1", "value", null); // create attribute		
		//attr = factory.createAttributeLiteral("key2", "value", null); // create attribute
		attrMap.put("key1", attr); // put attribute to the map attribute
		//attrMap.put("key2", attr);
		XEvent xevent2 = factory.createEvent(attrMap); // create xevent
		
		System.out.println(xtrace.contains(xevent2));
		*/
	}

}
