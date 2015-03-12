package Controller;

import it.unibz.krdb.obda.exception.InvalidMappingException;
import it.unibz.krdb.obda.exception.InvalidPredicateDeclarationException;

import java.io.IOException;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import Process.Query;
import EventLog.Extract;

public class QueryFrameController {
	
	private String owlFile;
	private String obdaFile;
	private Query query;
	String path = "src/main/resources/example/";
	
	public QueryFrameController(String owlFile, String obdaFile) throws OWLOntologyCreationException, IOException, InvalidPredicateDeclarationException, InvalidMappingException {
		this.owlFile = owlFile;
		this.obdaFile = obdaFile;
		this.query = new Query(path+owlFile, path+obdaFile);
	}
	
	public String process(String sparql) throws Exception{
		return query.runQuery(sparql);
	}
	

}
