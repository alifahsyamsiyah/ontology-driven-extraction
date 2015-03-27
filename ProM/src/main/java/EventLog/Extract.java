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
	
	public static XLog extractEventLogFile(String logs) throws Exception{
		System.out.println("enter extractEventLogFile");
			
		// create extension manager
		XExtensionManager extManager = XExtensionManager.instance();
		
		// create log
		XLog log = factory.createLog();
		
		// create trace	and event			
		String prevtrace = ""; String prevevent = "";
		String[] elem = logs.split("\n");
		
		
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
			event = (event.replaceAll("[\\p{Punct}\\w\\d]+#","")).replace(">","");
			String type = (arr[3].replaceAll("\"", "")).replace("^^xsd:string","");
			String key = (arr[4].replaceAll("\"", "")).replace("^^xsd:string","");
			key = (key.replaceAll("[\\p{Punct}\\w\\d]+#","")).replace(">","");
			String value = (arr[5].replaceAll("\"", "")).replace("^^xsd:string","");
			value = (value.replaceAll("[\\p{Punct}\\w\\d]+#","")).replace(">","");
			
			// the same trace
			if(prevtrace.equals(trace)) {
				
				// the same event
				if(prevevent.equals(event)) {
							
					attr = createAttribute(key, value, null, type); // create attribute
					attrMap.put(key, attr); // put attribute to the map attribute
					
				// not the same event
				} else {
					
					// the previous event is already completed, and it needs to be printed
					XEvent xevent = factory.createEvent(attrMap); // create xevent
					xtrace.insertOrdered(xevent); // insert event in correct order in a trace
							
					attr = createAttribute(key, value, null, type); // create attribute	
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
				
				attr = factory.createAttributeLiteral("concept:name", tracevalue, null); // create attribute for trace
				attrMapTrace = new XAttributeMapImpl(); // create a new map attribute
				attrMapTrace.put("concept:name", attr);	// put attribute to the map attribute
				xtrace = factory.createTrace(attrMapTrace); // create xtrace
				log.add(xtrace); // add to log
				
				attr = createAttribute(key, value, null, type); // create attribute		
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
	
	@SuppressWarnings("deprecation")
	public static XAttribute createAttribute(String key, String value, XExtension extension, String type) {
		XAttribute attribute;
		
		if(type.equals("boolean")) {
			
			Boolean v = new Boolean(value);
			attribute = factory.createAttributeBoolean(key, v, extension);
			
		} else if(type.equals("literal")) {
			
			attribute = factory.createAttributeLiteral(key, value, extension);
		
		} else if(type.equals("timestamp")) {
			
			Date thedate = null;
			
			// date format: year-month-date hrs:min:sec
			String[] arr = value.split(" ");
			String[] d1 = arr[0].split("-");
			String[] d2 = arr[1].split(":");
			String year = d1[0]; String month = d1[1]; String date = d1[2];
			String hrs = d2[0]; String min = d2[1]; String sec = d2[2];
			
			thedate = new Date(Integer.parseInt(year)-1900, Integer.parseInt(month)-1, Integer.parseInt(date), Integer.parseInt(hrs), Integer.parseInt(min), Integer.parseInt(sec));
			
			attribute = factory.createAttributeTimestamp(key, thedate, extension);
			
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
