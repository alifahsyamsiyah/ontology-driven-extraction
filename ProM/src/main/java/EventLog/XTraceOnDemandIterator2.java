/*
 * This class is implemented similar to XTraceIterator class in OpenXES
 * 
 */
package EventLog;

import it.unibz.krdb.obda.exception.InvalidMappingException;
import it.unibz.krdb.obda.exception.InvalidPredicateDeclarationException;

import java.io.IOException;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.deckfour.xes.extension.XExtension;
import org.deckfour.xes.extension.XExtensionManager;
import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.impl.XAttributeMapImpl;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import Process.Query;


public class XTraceOnDemandIterator2 implements ListIterator<XEvent> {

	
	protected String[] eventIDs;
	protected int position = 0;

	protected String ontology;
	protected String mapping;
	protected Query query;
	protected XFactoryOnDemandImpl factory = new XFactoryOnDemandImpl();		

	public XTraceOnDemandIterator2(String[] eventIDs, String ontology, String mapping) throws OWLOntologyCreationException, IOException, InvalidPredicateDeclarationException, InvalidMappingException {
		this(eventIDs, ontology, mapping, 0);
	}

	public XTraceOnDemandIterator2(String[] eventIDs, String ontology, String mapping, int position) throws OWLOntologyCreationException, IOException, InvalidPredicateDeclarationException, InvalidMappingException {
		
		this.eventIDs = eventIDs;
		this.ontology = ontology;
		this.mapping = mapping;
		this.position = position;
		this.query = new Query(ontology, mapping);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return (position < eventIDs.length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#next()
	 */
	public XEvent next() {
		XEvent result = null;
		try {
			result = get(position);
		} catch (IndexOutOfBoundsException e) {
			throw new NoSuchElementException("There is no next event in this trace");
		} finally {
			position++;
		}
		return result;
	}
	
	private XEvent get(int index) {
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
		
		XAttributeMapImpl attrMap = new XAttributeMapImpl();
		
		for(int i = 0; i < elem.length; i++) {
			String[] tmp = elem[i].split(", ");
			String type = tmp[0].replaceAll("\"", "").replace("^^xsd:string", "");
			String key = tmp[1].replaceAll("\"", "").replace("^^xsd:string", "");
			key = (key.replaceAll("[\\p{Punct}\\w\\d]+#","")).replace(">","");
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.eventIDsIterator#add(java.lang.Object)
	 */
	public void add(XEvent o) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.eventIDsIterator#hasPrevious()
	 */
	public boolean hasPrevious() {
		return (position > 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.eventIDsIterator#nextIndex()
	 */
	public int nextIndex() {
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.eventIDsIterator#previous()
	 */
	public XEvent previous() {
		position--;
		return get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.eventIDsIterator#previousIndex()
	 */
	public int previousIndex() {
		return (position - 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.eventIDsIterator#set(java.lang.Object)
	 */
	public void set(XEvent o) {
	}

}
