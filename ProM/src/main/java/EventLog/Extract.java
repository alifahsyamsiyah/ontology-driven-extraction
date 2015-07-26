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
import java.util.Iterator;
import java.util.LinkedList;
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
	private static HashMap<String, LinkedList<String>> hashtrace = new HashMap<String, LinkedList<String>>();
	private static HashMap<String, LinkedList<String>> hashevent = new HashMap<String, LinkedList<String>>();
	
	private static HashMap<String, LinkedList<String>> hasheventtype = new HashMap<String, LinkedList<String>>();
	private static HashMap<String, LinkedList<String>> hasheventkey = new HashMap<String, LinkedList<String>>();
	private static HashMap<String, LinkedList<String>> hasheventvalue = new HashMap<String, LinkedList<String>>();
	
	public static XLog extractEventLogFile(String traces, String eventtypes, String eventkeys, String eventvalues) throws Exception{
		System.out.println("enter extractEventLogFile");
			
		// create extension manager
		//XExtensionManager extManager = XExtensionManager.instance();
		
		// create log
		XLog log = factory.createLog();
		
		// create hashtrace
		String[] elem = traces.split("\n");
		
		for(int i = 0; i < elem.length; i++) {
			String[] arr = elem[i].split(", ");
			String trace = arr[0];
			trace = (trace.replaceAll("[\\p{Punct}\\w\\d]+#","")).replace(">","");
			String tracevalue = (arr[1].replaceAll("\"", "")).replace("^^xsd:string","");
			String event = arr[2];
			event = (event.replaceAll("[\\p{Punct}\\w\\d]+#","")).replace(">","");
			
			if(hashtrace.containsKey(trace)){
				LinkedList<String> lltrace = hashtrace.get(trace);
				lltrace.add(event);
				hashtrace.put(trace, lltrace);
			} else {
				LinkedList<String> lltrace = new LinkedList<String>();
				lltrace.add(tracevalue);
				lltrace.add(event);
				hashtrace.put(trace, lltrace);
			}
		}
		
		//create hasheventtype
		elem = eventtypes.split("\n");
		
		for(int i = 0; i < elem.length; i++) {
			String[] arr = elem[i].split(", ");
			String event = arr[0];
			event = (event.replaceAll("[\\p{Punct}\\w\\d]+#","")).replace(">","");
			String type = (arr[1].replaceAll("\"", "")).replace("^^xsd:string","");
			
			LinkedList<String> llevent;
			if(hasheventtype.containsKey(event)){
				llevent = hasheventtype.get(event);
			} else {
				llevent = new LinkedList<String>();
			}
			llevent.add(type);
			hasheventtype.put(event, llevent);
		}
		
		//create hasheventkey
		elem = eventkeys.split("\n");
		
		for(int i = 0; i < elem.length; i++) {
			String[] arr = elem[i].split(", ");
			String event = arr[0];
			event = (event.replaceAll("[\\p{Punct}\\w\\d]+#","")).replace(">","");
			String key = (arr[1].replaceAll("\"", "")).replace("^^xsd:string","");
			key = (key.replaceAll("[\\p{Punct}\\w\\d]+#","")).replace(">","");
			
			LinkedList<String> llevent;
			if(hasheventkey.containsKey(event)){
				llevent = hasheventkey.get(event);
			} else {
				llevent = new LinkedList<String>();
			}
			llevent.add(key);
			hasheventkey.put(event, llevent);
		}
		
		//create hasheventvalue
		elem = eventvalues.split("\n");
		
		for(int i = 0; i < elem.length; i++) {
			String[] arr = elem[i].split(", ");
			String event = arr[0];
			event = (event.replaceAll("[\\p{Punct}\\w\\d]+#","")).replace(">","");
			String value = (arr[1].replaceAll("\"", "")).replace("^^xsd:string","");
			value = (value.replaceAll("[\\p{Punct}\\w\\d]+#","")).replace(">","");
			
			LinkedList<String> llevent;
			if(hasheventvalue.containsKey(event)){
				llevent = hasheventvalue.get(event);
			} else {
				llevent = new LinkedList<String>();
			}
			llevent.add(value);
			hasheventvalue.put(event, llevent);
		}
		
		//create objects of OpenXES
		for (String trace : hashtrace.keySet()) {
			LinkedList<String> lltrace = hashtrace.get(trace);
			
			String tracevalue = lltrace.get(0);
			XAttribute attr = factory.createAttributeLiteral("concept:name", tracevalue, null); // create attribute for trace
			XAttributeMap attrMapTrace = new XAttributeMapImpl(); // create a new map attribute
			attrMapTrace.put("concept:name", attr);	// put attribute to the map attribute
			XTrace xtrace = factory.createTrace(attrMapTrace); // create xtrace			
			
			// for each event id in a trace
			for(int i = 1; i < lltrace.size(); i++) {
				String event = lltrace.get(i);
				
				LinkedList<String> lleventtype = hasheventtype.get(event);
				LinkedList<String> lleventkey = hasheventkey.get(event);
				LinkedList<String> lleventvalue = hasheventvalue.get(event);
				
				int j = 0; XAttribute attrEvent; XAttributeMap attrMapEvent = new XAttributeMapImpl();
				
				// for each event attribute
				while(j < lleventtype.size()) {
					String type = lleventtype.get(j); 
					String key = lleventkey.get(j);
					String value = lleventvalue.get(j); 
					j++;
					
					attrEvent = createAttribute(key, value, null, type); // create attribute		
					attrMapEvent.put(key, attrEvent); // put attribute to the map attribute					
				}
				
				XEvent xevent = factory.createEvent(attrMapEvent);
				xtrace.insertOrdered(xevent);
			}
			
			log.add(xtrace); // add to log
		}
			
			
		// create serializer
		XesXmlSerializer s = new XesXmlSerializer();		
		File f = new File("src/main/resources/example/eventlog.xes");
		OutputStream wr = new FileOutputStream(f);		
		s.serialize(log, wr);
		
		return log;
			
	}
	
	public static XLog extractEventLog(HashMap<String, LinkedList<String>> traces, HashMap<String, LinkedList<String>> eventtypes, HashMap<String, LinkedList<String>> eventkeys, HashMap<String, LinkedList<String>> eventvalues) throws Exception{
		System.out.println("enter extractEventLogFromHash");
		
		// create log
		XLog log = factory.createLog();
		
		//create objects of OpenXES
		for (String trace : traces.keySet()) {
			LinkedList<String> lltrace = traces.get(trace);
			
			String tracevalue = lltrace.get(0);
			XAttribute attr = factory.createAttributeLiteral("concept:name", tracevalue, null); // create attribute for trace
			XAttributeMap attrMapTrace = new XAttributeMapImpl(); // create a new map attribute
			attrMapTrace.put("concept:name", attr);	// put attribute to the map attribute
			XTrace xtrace = factory.createTrace(attrMapTrace); // create xtrace			
			
			// for each event id in a trace
			for(int i = 1; i < lltrace.size(); i++) {
				String event = lltrace.get(i);
				
				LinkedList<String> lleventtype = eventtypes.get(event);
				LinkedList<String> lleventkey = eventkeys.get(event);
				LinkedList<String> lleventvalue = eventvalues.get(event);
				
				int j = 0; XAttribute attrEvent; XAttributeMap attrMapEvent = new XAttributeMapImpl();
				
				// for each event attribute
				while(j < lleventtype.size()) {
					String type = lleventtype.get(j); 
					String key = lleventkey.get(j);
					String value = lleventvalue.get(j); 
					j++;
					
					attrEvent = createAttribute(key, value, null, type); // create attribute		
					attrMapEvent.put(key, attrEvent); // put attribute to the map attribute					
				}
				
				XEvent xevent = factory.createEvent(attrMapEvent);
				xtrace.insertOrdered(xevent);
			}
			
			log.add(xtrace); // add to log
		}
			
			
		// create serializer
		XesXmlSerializer s = new XesXmlSerializer();		
		File f = new File("src/main/resources/example/eventlog.xes");
		OutputStream wr = new FileOutputStream(f);		
		s.serialize(log, wr);
		
		return log;
			
	}
	
//	public static XLog extractEventLogFile(String logs) throws Exception{
//		System.out.println("enter extractEventLogFile");
//		
//		// create extension manager
//		XExtensionManager extManager = XExtensionManager.instance();
//		
//		// create log
//		XLog log = factory.createLog();
//		
//		// create trace	and event	
//		String prevtrace = ""; String prevevent = "";
//		String[] elem = logs.split("\n");
//		
//		for(int i = 0; i < elem.length; i++) {
//			String[] arr = elem[i].split(", ");
//			String trace = arr[0];
//			trace = (trace.replaceAll("[\\p{Punct}\\w\\d]+#","")).replace(">","");
//			String tracevalue = (arr[1].replaceAll("\"", "")).replace("^^xsd:string","");
//			String event = arr[2];
//			event = (event.replaceAll("[\\p{Punct}\\w\\d]+#","")).replace(">","");
//			String type = (arr[3].replaceAll("\"", "")).replace("^^xsd:string","");
//			String key = (arr[4].replaceAll("\"", "")).replace("^^xsd:string","");
//			key = (key.replaceAll("[\\p{Punct}\\w\\d]+#","")).replace(">","");
//			String value = (arr[5].replaceAll("\"", "")).replace("^^xsd:string","");
//			value = (value.replaceAll("[\\p{Punct}\\w\\d]+#","")).replace(">","");
//			
//			if(hashtrace.containsKey(trace)){
//				
//				if(hashevent.containsKey(event)){
//					LinkedList<String> llevent = hashevent.get(event);
//					llevent.add(type);
//					llevent.add(key);
//					llevent.add(value);
//					hashevent.put(event, llevent);
//				} else {
//					LinkedList<String> lltrace = hashtrace.get(trace);
//					lltrace.add(event);
//					hashtrace.put(trace, lltrace);
//					
//					LinkedList<String> llevent = new LinkedList<String>();
//					llevent.add(type);
//					llevent.add(key);
//					llevent.add(value);
//					hashevent.put(event, llevent);
//				}
//				
//				
//			} else {
//				LinkedList<String> lltrace = new LinkedList<String>();
//				lltrace.add(tracevalue);
//				lltrace.add(event);
//				hashtrace.put(trace, lltrace);
//				
//				LinkedList<String> llevent = new LinkedList<String>();
//				llevent.add(type);
//				llevent.add(key);
//				llevent.add(value);
//				hashevent.put(event, llevent);
//			}
//		}
//		
//		// for each trace id in hashtrace
//		for (String trace : hashtrace.keySet()) {
//			LinkedList<String> lltrace = hashtrace.get(trace);
//			
//			String tracevalue = lltrace.get(0);
//			XAttribute attr = factory.createAttributeLiteral("concept:name", tracevalue, null); // create attribute for trace
//			XAttributeMap attrMapTrace = new XAttributeMapImpl(); // create a new map attribute
//			attrMapTrace.put("concept:name", attr);	// put attribute to the map attribute
//			XTrace xtrace = factory.createTrace(attrMapTrace); // create xtrace			
//			
//			// for each event id in a trace
//			for(int i = 1; i < lltrace.size(); i++) {
//				String event = lltrace.get(i);
//				
//				LinkedList<String> llevent = hashevent.get(event);
//				
//				int j = 0; XAttribute attrEvent; XAttributeMap attrMapEvent = new XAttributeMapImpl();
//				
//				// for each event attribute
//				while(j < llevent.size()) {
//					String type = llevent.get(j); j++;
//					String key = llevent.get(j); j++;
//					String value = llevent.get(j); j++;
//					
//					attrEvent = createAttribute(key, value, null, type); // create attribute		
//					attrMapEvent.put(key, attrEvent); // put attribute to the map attribute					
//				}
//				
//				XEvent xevent = factory.createEvent(attrMapEvent);
//				xtrace.insertOrdered(xevent);
//			}
//			
//			log.add(xtrace); // add to log
//		}
//		
//		// create serializer
//		XesXmlSerializer s = new XesXmlSerializer();		
//		File f = new File("src/main/resources/example/eventlog.xes");
//		OutputStream wr = new FileOutputStream(f);		
//		s.serialize(log, wr);
//		
//		return log;
//	}
	
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
