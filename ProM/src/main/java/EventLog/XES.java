package EventLog;

import it.unibz.krdb.obda.exception.InvalidMappingException;
import it.unibz.krdb.obda.exception.InvalidPredicateDeclarationException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.deckfour.xes.extension.XExtension;
import org.deckfour.xes.extension.XExtensionManager;
import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.factory.XFactoryBufferedImpl;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeMapImpl;
import org.deckfour.xes.out.XesXmlSerializer;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import Process.Query;
import EventLog.XTraceOnDemandImpl;

public class XES {
	
	private static String ontology;
	private static String mapping;
	private static XFactoryOnDemandImpl factory = new XFactoryOnDemandImpl();		

	public static void main(String[] args) throws Exception {	
		ontology = "src/main/resources/example/ontology.owl";
		mapping = "src/main/resources/example/secondmapping.obda";
		
		XLogOnDemandImpl xlog = new XLogOnDemandImpl(new XAttributeMapImpl(),ontology, mapping);
		
		XTrace xtrace = xlog.get(4);
		xlog.add(xtrace);
		XEvent xevent = xtrace.get(0);
		xtrace.add(xevent);
		
		
		// create serializer
		XesXmlSerializer s = new XesXmlSerializer();		
		File f = new File("src/main/resources/example/eventlog2.xes");
		OutputStream wr = new FileOutputStream(f);		
		s.serialize(xlog, wr);
		
		/*XExtensionManager extManager = XExtensionManager.instance();
		XExtension extension = extManager.getByUri(XConceptExtension.EXTENSION_URI);
		XAttribute attr = factory.createAttributeLiteral("concept:name", "AMADEUS HOLY", extension); // need to be changed
		XAttributeMap attrMap = new XAttributeMapImpl(); // create a new map attribute
		attrMap.put("concept:name", attr);	// put attribute to the map attribute
		XEvent xevent = factory.createEvent(attrMap);*/
		
		//System.out.println(xtrace.contains(xevent));
		
		
	}
}
