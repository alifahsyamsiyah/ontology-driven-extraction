package EventLog;

import it.unibz.krdb.obda.exception.InvalidMappingException;
import it.unibz.krdb.obda.exception.InvalidPredicateDeclarationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.extension.XExtension;
import org.deckfour.xes.extension.XExtensionManager;
import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.factory.XFactoryBufferedImpl;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.XVisitor;
import org.deckfour.xes.model.impl.XAttributeMapImpl;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import Process.Query;

public class XLogOnDemandImpl extends ArrayList<XTrace> implements XLog {
	
	private XAttributeMap attributeMap;
	//private String[] attributeIDs;
	private String[] traceIDs;
	private String ontology;
	private String mapping;
	private Query query;
	private XFactoryOnDemandImpl factory = new XFactoryOnDemandImpl();		
	
	private Set<XExtension> extensions;
	private List<XEventClassifier> classifiers;
	private List<XAttribute> globalTraceAttributes;
	private List<XAttribute> globalEventAttributes;
	
	public XLogOnDemandImpl(XAttributeMap attributeMap, String ontology, String mapping) throws OWLOntologyCreationException, IOException, InvalidPredicateDeclarationException, InvalidMappingException {
		this.attributeMap = attributeMap;
		this.ontology = ontology;
		this.mapping = mapping;
		this.query = new Query(ontology, mapping);
		this.traceIDs = getTraceIDs();
		
		this.extensions = new HashSet<XExtension>();
		this.classifiers = new ArrayList<XEventClassifier>();
		this.globalTraceAttributes = new ArrayList<XAttribute>();
		this.globalEventAttributes = new ArrayList<XAttribute>();
		//getAttributeIDs();
	}
	
	public String[] getTraceIDs() {
		
		String sparql = "PREFIX : <http://myproject.org/odbs#> \n" +
				"SELECT DISTINCT ?t \n"+
				"WHERE { ?t a :Trace . }";
		
		String result = null;
		try {
			result = query.runQuery(sparql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result.split(", \n");
	}

	public XTrace get(int index) {
		System.out.println("-----enter get in log");
		
		XTrace xtrace = null;
		String traceID = traceIDs[index];
				
		String sparql = "PREFIX : <http://myproject.org/odbs#> \n" +
				"SELECT DISTINCT ?value \n"+
				"WHERE { "+traceID+" a :Trace ; :contain5 ?attr. ?attr :valueA ?value . }";
		
		String result = null;
		try {
			result = query.runQuery(sparql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String tracevalue = result.replaceAll("\"", "").replace("^^xsd:string, \n", "");
		
		XExtensionManager extManager = XExtensionManager.instance();
		XExtension extension = extManager.getByUri(XConceptExtension.EXTENSION_URI);;	
		XAttribute attr = factory.createAttributeLiteral("concept:name", tracevalue, extension); ;
		XAttributeMapImpl attrMapTrace = new XAttributeMapImpl(); // create a new map attribute
		attrMapTrace.put("concept:name", attr);	// put attribute to the map attribute
	
		try {
			xtrace = factory.createTrace(traceID, attrMapTrace, ontology, mapping);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return xtrace;
	}
	
	/*public Iterator<XTrace> iterator() {
		return new XLogOnDemandIterator(this, traceIDs);
	}*/

	public XAttributeMap getAttributes() {
		// TODO Auto-generated method stub
		return attributeMap;
	}

	public Set<XExtension> getExtensions() {
		// TODO Auto-generated method stub
		return extensions;
	}

	public boolean hasAttributes() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setAttributes(XAttributeMap arg0) {
		// TODO Auto-generated method stub
		
	}

	public boolean accept(XVisitor arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public List<XEventClassifier> getClassifiers() {
		// TODO Auto-generated method stub
		return classifiers;
	}

	public List<XAttribute> getGlobalEventAttributes() {
		// TODO Auto-generated method stub
		return globalEventAttributes;
	}

	public List<XAttribute> getGlobalTraceAttributes() {
		// TODO Auto-generated method stub
		return globalTraceAttributes;
	}

	public XLogInfo getInfo(XEventClassifier arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setInfo(XEventClassifier arg0, XLogInfo arg1) {
		// TODO Auto-generated method stub
		
	}
	
}
