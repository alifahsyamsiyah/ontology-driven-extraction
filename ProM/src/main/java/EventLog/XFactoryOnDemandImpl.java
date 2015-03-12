package EventLog;

import it.unibz.krdb.obda.exception.InvalidMappingException;
import it.unibz.krdb.obda.exception.InvalidPredicateDeclarationException;

import java.io.IOException;

import org.deckfour.xes.factory.XFactoryNaiveImpl;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.buffered.XAttributeMapBufferedImpl;
import org.deckfour.xes.model.impl.XAttributeMapLazyImpl;
import org.deckfour.xes.model.impl.XLogImpl;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

public class XFactoryOnDemandImpl extends XFactoryNaiveImpl implements XFactory {
	@Override
	public String getAuthor() {
		return "Alifah Syamsiyah";
	}
	
	public XLog createLog(XAttributeMap attributeMap, String ontology, String mapping) throws Exception {
		return new XLogOnDemandImpl(attributeMap, ontology, mapping);
	}
	
	public XTrace createTrace(String traceID, XAttributeMap attributeMap, String ontology, String mapping) throws Exception {
		return new XTraceOnDemandImpl(traceID, attributeMap, ontology, mapping);
	}
	
	public XEvent createEvent(String eventID,  XAttributeMap attributeMap, String ontology, String mapping) throws Exception {
		return new XEventOnDemandImpl(eventID, attributeMap, ontology, mapping);
	}
}
