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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	public Hashtable<String,String> hashconstant;
	
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
		//System.out.println("type: "+type);
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

		//BufferedWriter w = new BufferedWriter(new FileWriter("src/main/resources/example/result.csv"));
		
		 /* Query example */
		String sqlQuery;
		try {	
			QuestOWLResultSet rs = st.executeTuple(sparqlQuery);
			int columnSize = rs.getColumnCount();
			while (rs.nextRow()) {
				for (int idx = 1; idx <= columnSize; idx++) {
					OWLObject binding = rs.getOWLObject(idx);
					
					//w.write(binding.toString() + ", ");
					result = result + binding.toString() + ", ";
					//System.out.print(binding.toString() + ", "); // query result
				}
				//w.write("\n");
				result = result + "\n";
				//System.out.print("\n");
			}
			//w.close();
			rs.close();
			
			/*QuestOWLStatement qst = (QuestOWLStatement) st;
			sqlQuery = qst.getUnfolding(sparqlQuery);

			System.out.println();
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
	
	
	public HashMap<String, LinkedList<String>> runQueryTrace(String sparqlQuery) throws Exception {
		System.out.println("enter runQueryTrace");
		
		String prefix = Annotation.prefix;

		HashMap<String, LinkedList<String>> hashtrace = new HashMap<String, LinkedList<String>>();
		
		/*
		 * Prepare the data connection for querying.
		 */
		QuestOWLConnection conn = reasoner.getConnection();
		QuestOWLStatement st = conn.createStatement();

		 /* Query example */
		String sqlQuery;
		try {	
			QuestOWLResultSet rs = st.executeTuple(sparqlQuery);
			while (rs.nextRow()) {
				
				String trace = rs.getOWLObject(1).toString();
				String tracevalue = rs.getOWLObject(2).toString();
				String event =  rs.getOWLObject(3).toString();
				
				trace = (trace.replaceAll("<" + prefix,"")).replace(">","");
				tracevalue = (tracevalue.replaceAll("<" + prefix, "")).replace(">","");
				event = (event.replaceAll("<" + prefix,"")).replace(">","");
				
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
			
			rs.close();
			
//			QuestOWLStatement qst = (QuestOWLStatement) st;
//			sqlQuery = qst.getUnfolding(sparqlQuery);
//
//			System.out.println();
//			System.out.println("The input SPARQL query:");
//			System.out.println("=======================");
//			System.out.println(sparqlQuery);
//			System.out.println();
//			
//			System.out.println("The output SQL query:");
//			System.out.println("=====================");
//			System.out.println(sqlQuery);
//			System.out.println();
			
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
		
		return hashtrace;
	}
	
	public HashMap<String, LinkedList<String>> runQueryEvent(String sparqlQuery) throws Exception {
		System.out.println("enter runQueryEvent");
		String prefix = Annotation.prefix;

		HashMap<String, LinkedList<String>> hashevent = new HashMap<String, LinkedList<String>>();
		
		/*
		 * Prepare the data connection for querying.
		 */
		QuestOWLConnection conn = reasoner.getConnection();
		QuestOWLStatement st = conn.createStatement();

		 /* Query example */
		String sqlQuery;
		try {	
			QuestOWLResultSet rs = st.executeTuple(sparqlQuery);
			while (rs.nextRow()) {
				
				String event =  rs.getOWLObject(1).toString();
				String attribute = rs.getOWLObject(2).toString();
				
				event = (event.replaceAll("<" + prefix,"")).replace(">","");
				attribute = (attribute.replaceAll("\"", "")).replace("^^xsd:string","");
				attribute = (attribute.replaceAll("<" + prefix,"")).replace(">","");
				
				LinkedList<String> llevent;
				if(hashevent.containsKey(event)){
					llevent = hashevent.get(event);
				} else {
					llevent = new LinkedList<String>();
				}
				llevent.add(attribute);
				hashevent.put(event, llevent);
				
			}
			
			rs.close();
			
//			QuestOWLStatement qst = (QuestOWLStatement) st;
//			sqlQuery = qst.getUnfolding(sparqlQuery);
//
//			System.out.println();
//			System.out.println("The input SPARQL query:");
//			System.out.println("=======================");
//			System.out.println(sparqlQuery);
//			System.out.println();
//			
//			System.out.println("The output SQL query:");
//			System.out.println("=====================");
//			System.out.println(sqlQuery);
//			System.out.println();
			
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
		
		return hashevent;
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
	public LinkedList<String> simplifySQL(String sql) {	
		System.out.println("enter simplifySQL");
		//System.out.println("----sql: "+sql.replaceAll("\\n\\s+", " ").toLowerCase());
		
		String prefix = Annotation.prefix;
		
		LinkedList<String> sqlSimplified = new LinkedList<String>(); // list for the result (sql that is already simplified)

		// replace unimportant things
		sql = sql.toLowerCase();
		sql = sql.replaceAll("select\\s\\p{Punct}\\sfrom\\s*\\p{Punct}", ""); // select * from(
		sql = sql.replaceAll("\\p{Punct}\\s*sub_qview", ""); // ) sub_qview
		sql = sql.replaceAll("order by sub_qview[\\w\\p{Punct}]*", ""); // order by sub_qview ...
		sql = sql.replaceAll("[\\d]+ as \\p{Punct}\\w+questtype\\p{Punct},", ""); // 1 as "xquesttype",
		sql = sql.replaceAll("null as \\p{Punct}\\w+lang\\p{Punct},", ""); // null as "xlang",
		sql = sql.replaceAll("concat",""); // concat
		sql = sql.replaceAll("cast",""); // cast
		sql = sql.replaceAll("as char\\(8000\\) character set utf8", ""); // as char(8000) character set utf8
		sql = sql.replaceAll("and\\n[\\w\\p{Punct}\\d]+ is not null", ""); // and ... is not null		
		sql = sql.replaceAll("[\\w\\p{Punct}\\d]+ is not null", ""); // ... is not null
		sql = sql.replaceAll("where[\\n\\s]*$", ""); // where alone
		sql = sql.replaceAll("where[\\n\\s]*and", "where "); // where and
		sql = sql.replaceAll("where[\\n\\s]*union", "union"); // where alone with union afterwards
		sql = sql.replaceAll("all", ""); // all
		sql = sql.replaceAll("\\n", " "); // to make only in one line
		sql = sql.replaceAll("\\s+", " "); // multiple space
		sql = sql.replaceAll("^\\s", ""); // space in front
		
		// push constant
		hashconstant = new Hashtable<String, String>();
		
	    String q[] = sql.split("union"); // retrieve all sub queries
	    
		for(int i = 0; i < q.length; i++) {
			String sub_sql = q[i];
			
			String[] tmp = sub_sql.split("from");
			String selectclause = tmp[0].replace("select ", ""); // get clause [] in "select [] from"
			selectclause = selectclause.replace("distinct ", "");
			String selectclausenew = "";
			
			Pattern pattern = Pattern.compile("(.+?) as `(.+?)`"); // pattern [... as ...]
		    Matcher matcher = pattern.matcher(selectclause);
		    int start;
		    int end;
		    
		    // loop for process: [... as ...] --> qselect = constant as alias
		    while (matcher.find()) {
		        start = matcher.start();
		        end = matcher.end();
		        String qselect = selectclause.substring(start, end);
		        //System.out.println("qselect: "+qselect);
		        
				String[] temp = qselect.split(" as ");
				String constant = temp[0].replace("(", "").replace(")", "").replace("'","").replaceAll("^[,\\s]*", "").replace(",","/").replaceAll("\\s+", " ");
				String alias = temp[1].replace("`","");
				//System.out.println("constant: "+constant);
				//System.out.println("alias: "+alias);
				
				Pattern pattern2 = Pattern.compile("qview(.+?)`(.+?)`"); // pattern [qview...`..`]
			    Matcher matcher2 = pattern2.matcher(constant);
			    int start2;
			    int end2;

			    ArrayList<String> qviews = new ArrayList<String>();
			    int counter = 0;
			    
			    // loop for qview
			    while (matcher2.find()) {
			        start2 = matcher2.start();
			        end2 = matcher2.end();
			        String qview = constant.substring(start2, end2);
			        //System.out.println("qview: "+qview);
			        
			        qviews.add(qview + " as `" + alias + counter + "`");
			        //constant = constant.replaceFirst(prefix,""); // http...#
			        constant = constant.replace(qview, "{" + alias + counter + "}"); counter++; // update constant which contains qview. become: skolem/{x0}
			        matcher2 = pattern2.matcher(constant);
			        //hashconstant.put(alias, constant);
//			        System.out.println("constant hashconstant: "+constant);
//					System.out.println("alias hashconstant: "+alias);
			    }
			    
			    constant = constant.replaceAll(prefix,""); // http...#
			    hashconstant.put(alias, constant);
			    
//			    if(qviews.isEmpty()) {
//			    	hashconstant.put(alias, constant);
//			    	System.out.println("constant hashconstant: "+constant);
//					System.out.println("alias hashconstant: "+alias);
//			    } else {
			    
			    if(!qviews.isEmpty()) {
				    String qselectnew = qviews.get(0) + " ";
				    for(int j = 1; j < qviews.size(); j++) {
				    	qselectnew = qselectnew + ", " + qviews.get(j);
				    }
				    
				    selectclausenew = qselectnew + ", " + selectclausenew;
			    }
			}
			
			//System.out.println("selectclause: " + selectclause);
			//System.out.println("selectclausenew: " + selectclausenew);
			sub_sql = sub_sql.replace(selectclause, " " + selectclausenew);
			
			sub_sql = sub_sql.replaceAll("\\,\\s+from", " from"); // , from
			sub_sql = sub_sql.replaceAll("\\s+", " "); // multiple space
			
			//System.out.println("after push constant: " + sub_sql);
			
			// replace qview
			String[] temp = sub_sql.split("from ");
			String[] temp2 = temp[1].split(" where");
			
			String f = temp2[0]; // inside from clause
			
			String table[] = f.split(" ");
			
			// for each table in from clause
			for(int j = 0; j < table.length; j++) {
				String tbl = table[j]; j++;
				String qview = table[j].replace(",", "");
				
				sub_sql = sub_sql.replaceAll(qview, tbl); // replace qview with the name of table
				sub_sql = sub_sql.replaceAll(tbl + " " + tbl, tbl); // replace "table table" with "table"
			}
			
			q[i] = sub_sql;
		}
		
		for(int i = 0; i < q.length; i++) {
			//System.out.println("simplified sql: "+q[i]);
			sqlSimplified.add(q[i]);
		}
	    
		return sqlSimplified;
	}
	
}
