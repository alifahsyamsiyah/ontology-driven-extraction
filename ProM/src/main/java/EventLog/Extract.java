package EventLog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.deckfour.xes.extension.XExtension;
import org.deckfour.xes.extension.XExtensionManager;
import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.factory.XFactoryBufferedImpl;
import org.deckfour.xes.id.XID;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XAttributeTimestamp;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.XVisitor;
import org.deckfour.xes.model.impl.XAttributeLiteralImpl;
import org.deckfour.xes.model.impl.XAttributeMapImpl;
import org.deckfour.xes.model.impl.XLogImpl;
import org.deckfour.xes.model.impl.XTraceImpl;
import org.deckfour.xes.out.XesXmlSerializer;

public class Extract {
	
	private static XFactoryBufferedImpl factory = new XFactoryBufferedImpl();		
	
	public static void main(String[] args) throws Exception {
	}
	
	public static XLog extractEventLogFile(String logs) throws Exception{
		
		/*logs = "trace1, lili, film1, literal, concept:name, membuat film, e1, \n"
				+ "trace1, lili, film1, timestamp, time:timestamp, 1015-7-1-10-5, e2, \n"
				+ "trace1, lili, film2, literal, concept:name, membuat skenario, e1, \n"
				+ "trace1, lili, film2, timestamp, time:timestamp, 93-07-31, e2, \n"
				+ "trace2, lilip, film3, literal, concept:name, membuat film, e1, \n"
				+ "trace2, lilip, film3, timestamp, time:timestamp, 93-07-31, e2, \n"
				+ "trace2, lilip, film4, literal, concept:name, membuat skenario, e1, \n"
				+ "trace2, lilip, film4, timestamp, time:timestamp, 93-07-31, e2, \n";*/
			
		// create extension manager
		XExtensionManager extManager = XExtensionManager.instance();
		
		// create log
		XLog log = factory.createLog();
		
		// create trace	and event			
		String prevtrace = ""; String prevevent = "";
		String[] elem = logs.split("\n");
		
		XExtension extension;	
		XAttribute attr;
		XAttributeMapImpl attrMapTrace = null;
		XAttributeMapImpl attrMap = null;
		XTrace xtrace = null;
			
		
		for(int i = 0; i < elem.length; i++) {
			// read every line
			String[] arr = elem[i].split(", ");
			String trace = arr[0];
			String tracevalue = (arr[1].replaceAll("\"", "")).replace("^^xsd:string","");
			String event = arr[2];
			String type = (arr[3].replaceAll("\"", "")).replace("^^xsd:string","");
			String key = (arr[4].replaceAll("\"", "")).replace("^^xsd:string","");
			key = (key.replaceAll("[\\p{Punct}\\w\\d]+#","")).replace(">","");
			String value = (arr[5].replaceAll("\"", "")).replace("^^xsd:string","");
			String ext = arr[6];
			
			// the same trace
			if(prevtrace.equals(trace)) {
				
				// the same event
				if(prevevent.equals(event)) {
					
					extension = extManager.getByUri(XConceptExtension.EXTENSION_URI); // get the extension			
					attr = createAttribute(key, value, extension, type); // create attribute
					attrMap.put(key, attr); // put attribute to the map attribute
					
				// not the same event
				} else {
					
					// the previous event is already completed, and it needs to be printed
					XEvent xevent = factory.createEvent(attrMap); // create xevent
					xtrace.insertOrdered(xevent); // insert event in correct order in a trace
					
					extension = extManager.getByUri(XConceptExtension.EXTENSION_URI); // get the extension			
					attr = createAttribute(key, value, extension, type); // create attribute	
					attrMap = new XAttributeMapImpl(); // create attr map for event
					attrMap.put(key, attr); // put attribute to the map attribute
					
					prevevent = event;
					
				}
				
			// not the same trace
			} else {
				
				// there is event need to be printed
				if(attrMap != null) {
					XEvent xevent = factory.createEvent(attrMap); // create xevent
					xtrace.insertOrdered(xevent); // insert event in correct order in a trace
				}
				
				extension = extManager.getByUri(XConceptExtension.EXTENSION_URI); // get the extension		
				attr = factory.createAttributeLiteral("concept:name", tracevalue, extension); // create attribute for trace
				attrMapTrace = new XAttributeMapImpl(); // create a new map attribute
				attrMapTrace.put("concept:name", attr);	// put attribute to the map attribute
				xtrace = factory.createTrace(attrMapTrace); // create xtrace
				log.add(xtrace); // add to log
				
				extension = extManager.getByUri(XConceptExtension.EXTENSION_URI); // get the extension			
				attr = createAttribute(key, value, extension, type); // create attribute		
				attrMap = new XAttributeMapImpl(); // create a new map attribute
				attrMap.put(key, attr); // put attribute to the map attribute
				
				prevtrace = trace;
				prevevent = event;
			}
		} 
		
		// there is event need to be printed
		if(attrMap != null) {
			XEvent xevent = factory.createEvent(attrMap); // create xevent
			xtrace.insertOrdered(xevent); // insert event in correct order in a trace
		}
		
				
		// create serializer
		XesXmlSerializer s = new XesXmlSerializer();		
		File f = new File("src/main/resources/example/eventlog.xes");
		OutputStream wr = new FileOutputStream(f);		
		s.serialize(log, wr);
		
		return log;
			
	}
	
	public static XAttribute createAttribute(String key, String value, XExtension extension, String type) {
		XAttribute attribute;
		
		if(type.equals("boolean")) {
			
			Boolean v = new Boolean(value);
			attribute = factory.createAttributeBoolean(key, v, extension);
			
		} else if(type.equals("literal")) {
			
			attribute = factory.createAttributeLiteral(key, value, extension);
		
		} else if(type.equals("timestamp")) {
			
			Date date = null;
			String[] arr = value.split("-");
			if(arr.length == 3) {				
				date = new Date(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]));
			} else if(arr.length == 5) {				
				date = new Date(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]), Integer.parseInt(arr[3]), Integer.parseInt(arr[4]));
			} else if(arr.length == 6) {				
				date = new Date(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]), Integer.parseInt(arr[3]), Integer.parseInt(arr[4]), Integer.parseInt(arr[5]));
			}
			attribute = factory.createAttributeTimestamp(key, date, extension);
			
		} else if(type.equals("discrete")) {
			
			Long l = new Long(value);
			attribute = factory.createAttributeDiscrete(key, l, extension);
			
		} else if(type.equals("continuous")) {
			
			Double d = new Double(value);
			attribute = factory.createAttributeContinuous(key, d, extension);
			
		} else if(type.equals("id")) {
			
			XID id = new XID();
			attribute = factory.createAttributeID(key, id, extension);
			
		} else if(type.equals("container")) {
			
			attribute = factory.createAttributeContainer(key, extension);
			
		} else if(type.equals("list")) {
			
			attribute = factory.createAttributeList(key, extension);
			
		} else {
			
			attribute = null;
			
		}
		
		return attribute;
	}
	
}
