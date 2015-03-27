package Process;
import it.unibz.krdb.obda.exception.InvalidMappingException;
import it.unibz.krdb.obda.exception.InvalidPredicateDeclarationException;
import it.unibz.krdb.obda.io.ModelIOManager;
import it.unibz.krdb.obda.model.OBDADataFactory;
import it.unibz.krdb.obda.model.OBDAModel;
import it.unibz.krdb.obda.model.impl.OBDADataFactoryImpl;
import it.unibz.krdb.obda.owlrefplatform.core.QuestConstants;
import it.unibz.krdb.obda.owlrefplatform.core.QuestPreferences;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWL;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLConnection;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLFactory;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLResultSet;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLStatement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;


public class Query {
	private String owlfile;
	private QuestOWL reasoner;
	
	/**
	 * A constructor for class Query.
	 * 
	 * @param owlfile
	 * 			An ontology file.
	 * @param obdafile
	 * 			A mapping file.
	 * @throws OWLOntologyCreationException 
	 * @throws InvalidMappingException 
	 * @throws InvalidPredicateDeclarationException 
	 * @throws IOException 
	 */
	public Query(String owlfile, String obdafile) throws OWLOntologyCreationException, IOException, InvalidPredicateDeclarationException, InvalidMappingException {
		this.owlfile = owlfile;
		this.reasoner = load(owlfile, obdafile);		
	}
	
	/**
	 * A method for loading the ontology, OBDA model, 
	 * prepare configuration for Quest, and
	 * create the instance of Quest OWL reasoner
	 * 
	 * @param owl
	 * 			An ontology file.
	 * @param obdafile
	 * 			A mapping file.
	 * @return QuestOWL reasoner
	 * @throws OWLOntologyCreationException
	 * @throws IOException
	 * @throws InvalidPredicateDeclarationException
	 * @throws InvalidMappingException
	 */
	private QuestOWL load(String owl, String obdafile) throws OWLOntologyCreationException, IOException, InvalidPredicateDeclarationException, InvalidMappingException {
		/*
		 * Load the ontology from an external .owl file.
		 */
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File(owlfile));

		/*
		 * Load the OBDA model from an external .obda file
		 */
		OBDADataFactory fac = OBDADataFactoryImpl.getInstance();
		OBDAModel obdaModel = fac.getOBDAModel();
		ModelIOManager ioManager = new ModelIOManager(obdaModel);
		ioManager.load(obdafile);

		/*
		 * Prepare the configuration for the Quest instance. The example below shows the setup for
		 * "Virtual ABox" mode
		 */
		QuestPreferences preference = new QuestPreferences();
		preference.setCurrentValueOf(QuestPreferences.ABOX_MODE, QuestConstants.VIRTUAL);
        preference.setCurrentValueOf(QuestPreferences.SQL_GENERATE_REPLACE, QuestConstants.FALSE);

		/*
		 * Create the instance of Quest OWL reasoner.
		 */
		QuestOWLFactory factory = new QuestOWLFactory();
		factory.setOBDAController(obdaModel);
		factory.setPreferenceHolder(preference);
		QuestOWL reasoner = (QuestOWL) factory.createReasoner(ontology, new SimpleConfiguration());
		
		return reasoner;
	}
	
	/**
	 * A method to get the type of an attribute.
	 * It will use ontology file for getting the type.
	 * 
	 * @param attribute
	 * 			An attribute name 
	 * @return String of attribute's type
	 * @throws OWLOntologyCreationException
	 */
	public String getType(String attribute) throws OWLOntologyCreationException {
		String type = "";
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File(owlfile));
		
		Set<OWLDataProperty> dataProperty = ontology.getDataPropertiesInSignature();
		
		for(OWLDataProperty o : dataProperty) {
			Set<OWLDataPropertyRangeAxiom> range = ontology.getDataPropertyRangeAxioms(o);
			
			for(OWLDataPropertyRangeAxiom r : range) {
				//System.out.println("r: "+r.getProperty().toString().toLowerCase());
				//System.out.println("attr: "+attribute);
				if(r.getProperty().toString().toLowerCase().contains(attribute)){
					OWLDataRange d = r.getRange();
					type = d.toString();
					type = type.replace("xsd:", "");
					type = type.replaceFirst("[^.]",type.substring(0,1).toUpperCase());
				}
				
			}
		}
		
		return type;
	}
	
	/**
	 * A method that run a Sparql Query. It gives result after doing
	 * query rewriting, unfolding, and execution into database.
	 * 
	 * @param sparqlQuery
	 * 			A Sparql Query to be run.
	 * @throws Exception
	 */
	public String runQuery(String sparqlQuery) throws Exception {
		System.out.println("enter runQuery");
		String result = "";
		
		/*
		 * Prepare the data connection for querying.
		 */
		QuestOWLConnection conn = reasoner.getConnection();
		QuestOWLStatement st = conn.createStatement();

		BufferedWriter w = new BufferedWriter(new FileWriter("src/main/resources/example/result.csv"));
		
		 /* Query example */
		String sqlQuery;
		try {	
			QuestOWLResultSet rs = st.executeTuple(sparqlQuery);
			int columnSize = rs.getColumnCount();
			while (rs.nextRow()) {
				for (int idx = 1; idx <= columnSize; idx++) {
					OWLObject binding = rs.getOWLObject(idx);
					
					w.write(binding.toString() + ", ");
					result = result + binding.toString() + ", ";
					//System.out.print(binding.toString() + ", "); // query result
				}
				w.write("\n");
				result = result + "\n";
				//System.out.print("\n");
			}
			w.close();
			rs.close();
			
			QuestOWLStatement qst = (QuestOWLStatement) st;
			sqlQuery = qst.getUnfolding(sparqlQuery);

			/*System.out.println();
			System.out.println("The input SPARQL query:");
			System.out.println("=======================");
			System.out.println(sparqlQuery);
			System.out.println();
			
			System.out.println("The output SQL query:");
			System.out.println("=====================");
			System.out.println(sqlQuery);
			System.out.println();*/
			
		} finally {
			/* Close connection and resources */
			 
			if (st != null && !st.isClosed()) {
				st.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
			reasoner.dispose();
		}
		
		return result;
	}
	
	/**
	 * A method to unfold Sparql Query in user's annotation to become
	 * a Sql Query so that it can be used as source query in second mapping.
	 * 
	 * @param sparqlQuery
	 * 			A Sparql Query to be unfolded.
	 * @return String of unfolded Sparql Query which is an Sql Query.
	 * @throws Exception
	 */
	public String unfoldQuery(String sparqlQuery) throws Exception {

		/*
		 * Prepare the data connection for querying.
		 */
		QuestOWLConnection conn = reasoner.getConnection();
		QuestOWLStatement st = conn.createStatement();

		
		 /* Query example */
		String sqlQuery;
		try {			 
			QuestOWLStatement qst = (QuestOWLStatement) st;
			sqlQuery = qst.getUnfolding(sparqlQuery);
			
		} finally {
			/* Close connection and resources */
			 
			if (st != null && !st.isClosed()) {
				st.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
			reasoner.dispose();
		}
		
		return sqlQuery;
	}
	
	/**
	 * A method to simplify Sql Query so that it can be written in second mapping
	 * and processed by Ontop.
	 * 
	 * @param sql
	 * 			A Sql to be simplified
	 * @return
	 */
	public String simplifySQL(String sql) {	
		System.out.println("enter simplifySQL");
		//System.out.println("----sql: "+sql);
		
		sql = sql.toLowerCase();
		sql = sql.replaceAll("select\\s\\p{Punct}\\sfrom\\s*\\p{Punct}", ""); // select * from(
		sql = sql.replaceAll("\\p{Punct}\\s*sub_qview", ""); // ) sub_qview
		sql = sql.replaceAll("order by sub_qview[\\w\\p{Punct}]*", ""); // order by sub_qview ...
		sql = sql.replaceAll("[\\d]+ as \\p{Punct}\\w+questtype\\p{Punct},", ""); // 1 as "xquesttype",
		sql = sql.replaceAll("null as \\p{Punct}\\w+lang\\p{Punct},", ""); // null as "xlang",
		sql = sql.replaceAll("concat\\('http[\\p{Punct}\\w\\d]+\\s",""); // concat('http...',
		sql = sql.replaceAll("cast",""); // cast
		sql = sql.replaceAll("as char\\(8000\\) character set utf8", ""); // as char(8000) character set utf8
		sql = sql.replaceAll("and\\n[\\w\\p{Punct}\\d]+ is not null", ""); // and ... is not null		
		sql = sql.replaceAll("[\\w\\p{Punct}\\d]+ is not null", ""); // ... is not null
		sql = sql.replaceAll("where[\\n\\s]*$", ""); // where alone
		sql = sql.replaceAll("where[\\n\\s]*and", "where "); // where and
		sql = sql.replaceAll("where[\\n\\s]*union", "union"); // where alone with union afterwards
		sql = sql.replaceAll("\\(", " "); //(
		sql = sql.replaceAll("\\)", " "); //)
		sql = sql.replaceAll("\\n", " "); // to make only in one line
		sql = sql.replaceAll("\\s+", " "); // multiple space
		
		//System.out.println("----simplified sql: "+sql);
		
		return sql;
		
	}
}
