package EventLog;

import it.unibz.krdb.obda.exception.InvalidMappingException;
import it.unibz.krdb.obda.exception.InvalidPredicateDeclarationException;

import java.io.IOException;
import java.util.Set;

import org.deckfour.xes.extension.XExtension;
import org.deckfour.xes.id.XID;
import org.deckfour.xes.id.XIDFactory;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.XVisitor;
import org.deckfour.xes.model.impl.XEventImpl;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import Process.Query;

public class XEventOnDemandImpl implements XEvent {
	
	private String eventID;
	private XAttributeMap attributeMap;
	//private String[] attributeIDs;
	private String ontology;
	private String mapping;
	private Query query;
	private XFactoryOnDemandImpl factory = new XFactoryOnDemandImpl();		
	
	public XEventOnDemandImpl(String eventID, XAttributeMap attributeMap, String ontology, String mapping) throws OWLOntologyCreationException, IOException, InvalidPredicateDeclarationException, InvalidMappingException {
		this.eventID = eventID;
		this.attributeMap = attributeMap;
		this.ontology = ontology;
		this.mapping = mapping;
		this.query = new Query(ontology, mapping);
		//getAttributeIDs
	}
	
	//public String[] getAttributeIDs() {
		//return null;
	//}

	public XAttributeMap getAttributes() {
		// TODO Auto-generated method stub
		System.out.println("enter getAttribute Event");
		return attributeMap;
	}

	public Set<XExtension> getExtensions() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasAttributes() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setAttributes(XAttributeMap arg0) {
		// TODO Auto-generated method stub
		
	}

	public void accept(XVisitor arg0, XTrace arg1) {
		// TODO Auto-generated method stub
		
	}

	public XID getID() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Object clone() {
		return null;
	}

}
