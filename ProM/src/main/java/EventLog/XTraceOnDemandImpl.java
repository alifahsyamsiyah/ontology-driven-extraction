package EventLog;

import it.unibz.krdb.obda.exception.InvalidMappingException;
import it.unibz.krdb.obda.exception.InvalidPredicateDeclarationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;

import org.deckfour.xes.extension.XExtension;
import org.deckfour.xes.extension.XExtensionManager;
import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.factory.XFactoryBufferedImpl;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.XVisitor;
import org.deckfour.xes.model.impl.XAttributeMapImpl;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import Process.Query;

public class XTraceOnDemandImpl extends ArrayList<XEvent> implements XTrace {
	private String traceID;
	private XAttributeMap attributeMap;
	//private String[] attributeIDs;
	private String[] eventIDs;
	private String ontology;
	private String mapping;
	private Query query;
	private XFactoryOnDemandImpl factory = new XFactoryOnDemandImpl();		
	
	public XTraceOnDemandImpl(String traceID, XAttributeMap attributeMap, String ontology, String mapping) throws OWLOntologyCreationException, IOException, InvalidPredicateDeclarationException, InvalidMappingException {
		this.traceID = traceID;
		this.attributeMap = attributeMap;
		this.ontology = ontology;
		this.mapping = mapping;
		this.query = new Query(ontology, mapping);
		this.eventIDs = getEventIDs();
	}
	
	private String[] getEventIDs() {
		// to get the current trace
		
		String sparql = "PREFIX : <http://myproject.org/odbs#> \n" +
				"SELECT DISTINCT ?event \n"+
				"WHERE { "+traceID+" a :Trace ; :contain2 ?event . }";
		
		String result = "";
		try {
			result = query.runQuery(sparql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result.split(", \n"); //NOOOOO YOU NEED TO SORT!
		
		// for sorting timestamp
		/*
		String sparql = "PREFIX : <http://myproject.org/odbs#> \n" +
				"SELECT DISTINCT ?date ?event \n"+
				"WHERE { "+traceID+" a :Trace ; :contain2 ?event .\n"
						+ "?event :contain3 ?attr .\n"
						+ "?attr :keyA \"time:timestamp\"^^xsd:string; :valueA ?date. }"; 
		
		String result = "";
		try {
			result = query.runQuery(sparql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[] elem =  result.split("\n");
		
		String[][] arr = new String[elem.length][2];
		for(int i = 0; i < elem.length; i++) {
			String[] tmp = elem[i].split(", ");
			arr[i][0] = tmp[0];
			arr[i][1] = tmp[1];
		}
		
		Arrays.sort(arr, new Comparator<String[]>() {
            public int compare(final String[] entry1, final String[] entry2) {
                final String time1 = entry1[0];
                final String time2 = entry2[0];
                return time1.compareTo(time2);
            }
        });
		
		for(int i = 0; i < elem.length; i++) {
			eventIDs[i] = arr[i][1];
		}
		
		return eventIDs;
		*/
		
		
	}
	
	public XEvent get(int index) {
		System.out.println("enter get");
		
		String eventID = eventIDs[index];
		
		String sparql = "PREFIX : <http://myproject.org/odbs#> \n" +
				"SELECT DISTINCT ?type ?key ?value \n"+
				"WHERE { "+eventID+" a :Event; :contain3 ?attr."
						+ "?attr :typeA ?type; :keyA ?key; :valueA ?value. }";
		
		String result = null;
		try {
			result = query.runQuery(sparql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[] elem = result.split("\n");
		
		XAttributeMapImpl attrMap = new XAttributeMapImpl();;
		for(int i = 0; i < elem.length; i++) {
			String[] tmp = elem[i].split(", ");
			String type = tmp[0].replaceAll("\"", "").replace("^^xsd:string", "");
			String key = tmp[1].replaceAll("\"", "").replace("^^xsd:string", "");
			String value = tmp[2].replaceAll("\"", "").replace("^^xsd:string", "");
			
			XExtensionManager extManager = XExtensionManager.instance();
			XExtension extension = extManager.getByUri(XConceptExtension.EXTENSION_URI);
			XAttribute attr = factory.createAttributeLiteral(key, value, extension); // need to be changed
			attrMap.put(key, attr);	// put attribute to the map attribute
		}
		
		XEvent xevent = null;
		try {
			xevent =  factory.createEvent(eventID, attrMap, ontology, mapping);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xevent;
		
	}
	
	public int size() {
		
		return eventIDs.length;
		
		/*String sparql = "PREFIX : <http://myproject.org/odbs#> \n" +
				"SELECT DISTINCT ?event \n"+
				"WHERE { "+currentTrace+" a :Trace ; :contain2 ?event . }";
		
		String result = "";
		try {
			result = query.runQuery(sparql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[] elem = result.split(", \n");
		if(elem[0] == "") return 0;
		
		return elem.length;*/
	}
	
	public int indexOf(Object o) {
		int size = size();
		for(int i = 0; i < size; i++) {
			if(get(i).equals(o)) return i;
		}
		
		return -1;
	}
	
	
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof XTrace)) {
			return false;
		}
		
		XTrace other = (XTrace) o;
		
		// check if the attributes are the same
		XAttributeMap attrMapOther = other.getAttributes();		
		XAttributeMap attrMap = getAttributes();		
		if(!(attrMap.equals(attrMapOther))) return false;
		
		// check if they are the same object
		XTrace xtrace = factory.createTrace(attrMap);
		
		int size = size();
		for(int i = 0; i < size; i++) {
			XEvent xevent = get(i);
			xtrace.add(xevent);
		}
		
		return xtrace.equals(other);
	}
	
	public boolean contains(Object o) {
		System.out.println("enter contains");
		int size = size();
		for(int i = 0; i < size; i++) {
			if(get(i).equals(o)) return true;
		}		
		
		return false;
	}
	
	public boolean isEmpty() {
		System.out.println("enter isEmpty");
		if(size() == 0) return true;
		else return false;
	}

	public XAttributeMap getAttributes() {
		// TODO Auto-generated method stub
		System.out.println("enter getAttributes");
		return attributeMap;
		
		/*String sparql = "PREFIX : <http://myproject.org/odbs#> \n" +
				"SELECT DISTINCT ?v \n"+
				"WHERE { ?t a :Trace ; :contain5 ?v . }";
		
		String result = "";
		try {
			result = query.runQuery(sparql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String value = result.replace(", \n", "");
		
		XExtensionManager extManager = XExtensionManager.instance();
		XExtension extension = extManager.getByUri(XConceptExtension.EXTENSION_URI);
		XAttribute attr = factory.createAttributeLiteral("concept:name", value, extension); 
		XAttributeMap attrMap = new XAttributeMapImpl(); // create a new map attribute
		attrMap.put("concept:name", attr);	// put attribute to the map attribute
		
		return attrMap;
		*/
	}

	public Set<XExtension> getExtensions() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasAttributes() {
		// TODO Auto-generated method stub
		
		XAttributeMap attrMap = getAttributes();
		
		if(attrMap.isEmpty()) return false;
		else return true;
	}

	public void setAttributes(XAttributeMap arg0) {
		// TODO Auto-generated method stub
		
	}

	public void accept(XVisitor arg0, XLog arg1) {
		// TODO Auto-generated method stub
		
	}

	public int insertOrdered(XEvent arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
