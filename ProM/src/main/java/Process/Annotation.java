package Process;
import it.unibz.krdb.obda.exception.InvalidMappingException;
import it.unibz.krdb.obda.exception.InvalidPredicateDeclarationException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;


public class Annotation { 
	private Query query;
	private Hashtable<String,LinkedList<String>> hashOfSql  = new Hashtable<String, LinkedList<String>>();
	private Hashtable<String,LinkedList<String>> hashOfVar  = new Hashtable<String, LinkedList<String>>();

	/**
	 * The constructor of class Annotation.
	 * 
	 * @param owl
	 * 			An ontology file.
	 * @param firstmapping
	 * 			A file containing mapping from database to domain ontology.
	 * @throws OWLOntologyCreationException
	 * @throws InvalidMappingException 
	 * @throws InvalidPredicateDeclarationException 
	 * @throws IOException 
	 */
	public Annotation(String owl, String firstmapping) throws OWLOntologyCreationException, IOException, InvalidPredicateDeclarationException, InvalidMappingException {
		this.query = new Query(owl, firstmapping);
	}
	
	/**
	 * A method for writing second mapping (from domain to event ontology).
	 * Prefix and source declaration are gotten from first mapping file.
	 * While mapping declaration comes from extracting hash of Sql.
	 * Each annotation tag has different mapping.
	 * Variables are extracted from hash of variables.
	 * 
	 * @param firstmappingFile
	 * 			A file containing mapping from database to domain ontology.
	 * @param secondmappingFile
	 * 			A file containing mapping from domain to event ontology
	 * @throws IOException
	 * @throws OWLOntologyCreationException
	 */
	public void writeAnnotation(String firstmappingFile, String secondmappingFile) throws IOException, OWLOntologyCreationException {
		System.out.println("enter writeAnnotation");
		BufferedReader firstmapping = new BufferedReader(new FileReader(firstmappingFile));
		BufferedWriter secondmapping = new BufferedWriter(new FileWriter(secondmappingFile));
		
		/*
		 * Write prefix and source declaration
		 */
		String line;
		while(!(line = firstmapping.readLine()).contains("[MappingDeclaration]")) {
			secondmapping.write(line + "\n");
		}
		
		/*
		 * Write mapping declaration
		 */
		secondmapping.write("[MappingDeclaration] @collection [[\n");
		
		// for each Sql query in hash of Sql
		for(String key : hashOfSql.keySet()) {
			LinkedList<String> listofsql = hashOfSql.get(key);
			LinkedList<String> listofvar = hashOfVar.get(key);
			
			/*
			 * Write a mapping for trace
			 */
			if(key.equals("trace")) {
				
				for(int i = 0; i < listofsql.size(); i++) {					
					String[] arr = listofvar.get(i).split(" ");
					String t = arr[0].replace("?", ""); // trace
					String l = arr[1].replace("?", ""); // log --- deleted just for a while
					int c = 0;
					
					secondmapping.write("mappingId\t"+ key + i + c + "\n"); c++;
					secondmapping.write("target\t\t:log{" + l + "} :LcontainsT :trace{" + t + "} . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
					secondmapping.write("mappingId\t"+ key + i + c + "\n"); c++;
					secondmapping.write("target\t\t:trace{" + t + "} :TcontainsA :attr{" + t + "} . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
					secondmapping.write("mappingId\t"+ key + i + c + "\n"); c++;
					secondmapping.write("target\t\t:attr{" + t + "} :keyA \"concept:name\"^^xsd:string . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
					secondmapping.write("mappingId\t"+ key + i + c + "\n"); c++;
					secondmapping.write("target\t\t:attr{" + t + "} :valueA {" + t + "} . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
					secondmapping.write("mappingId\t"+ key + i + c + "\n"); c++;
					secondmapping.write("target\t\t:attr{" + t + "} :typeA \"literal\"^^xsd:string . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
				}
			
			/*
			 * Write a mapping for event
			 */
			} else if(key.equals("event")) {

				for(int i = 0; i < listofsql.size(); i++) {					
					String[] arr = listofvar.get(i).split(" ");
					String event = ":event";
					for(int j = 0; j < arr.length-1; j++) {
						String e = arr[j].replace("?", "");
						event = event + "{" + e + "}";
					}

					String t = arr[arr.length-1].replace("?", ""); // trace
					
					int c = 0;
					
					secondmapping.write("mappingId\t"+ key + i + c + "\n"); c++; 
					secondmapping.write("target\t\t:trace{" + t + "} :TcontainsE "+ event + " . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
				}
				
			
			/*
			 * Write a mapping for timestamp
			 */
			} else if(key.equals("timestamp")) {
				
				for(int i = 0; i < listofsql.size(); i++) {					
					String[] arr = listofvar.get(i).split(" ");
					String t = arr[0].replace("?", ""); // value of timestamp
					String time = ":attr{" + t + "}"; // object of timestamp
					String event = ":event"; //event
					for(int j = 1; j < arr.length; j++) {
						String e = arr[j].replace("?", "");
						event = event + "{" + e + "}";
						time = time + "{" + e + "}";
					}
					int c = 0;
					
					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t"+ event + " :EcontainsA " + time + " . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");

					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t"+ time + " :keyA \"time:timestamp\"^^xsd:string . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");

					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t"+ time + " :typeA \"timestamp\"^^xsd:string . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");

					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t"+ time + " :valueA {" + t + "} . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
				}
				
			
			/*
			 * Write a mapping for activityname
			 */
			} else if(key.equals("activityname")) {
				
				for(int i = 0; i < listofsql.size(); i++) {					
					String[] arr = listofvar.get(i).split(" ");
					String n = arr[0].replace("?", ""); // value of activity-name
					String name = ":attr{" + n + "}"; // object of activity-name
					String event = ":event"; //event
					for(int j = 1; j < arr.length; j++) {
						String e = arr[j].replace("?", "");
						event = event + "{" + e + "}";
						name = name + "{" + e + "}";
					}
					int c = 0;
					
					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t"+ event + " :EcontainsA " + name + " . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");

					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t"+ name + " :keyA \"concept:name\"^^xsd:string . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");

					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t"+ name + " :typeA \"literal\"^^xsd:string . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");

					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t"+ name + " :valueA {" + n + "} . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
				}
				
			/*
			 * Write a mapping for other attribute
			 */
			} else if(key.equals("attribute")) {
				
				for(int i = 0; i < listofsql.size(); i++) {	
					
					String[] arr = listofvar.get(i).split(" ");
					String v = arr[0].replace("?", ""); // attribute value
					String a = arr[1].replace("?", ""); // attribute name
					String c = arr[2].replace("?", ""); // instance class containing attribute
					/*String event = ":event"; //event
					for(int j = 3; j < arr.length; j++) {
						String e = arr[j].replace("?", "");
						event = event + "{" + e + "}";
					}*/	
					String event = ":event{" + c + "}"; // keep just for a while. it should be the variable event above
					
					String[] temp = listofsql.get(i).split("as "+"`"+a+"`");
					String[] temp2 = temp[0].split(" ");
					String attribute = temp2[temp2.length-1].replaceFirst("'", "<");
					attribute = attribute.replaceFirst("'", ">");
					
					String t = query.getType(attribute); // get attribute type from ontology file
					String s = convertType(t); // convert attribute type for this mapping
					
					int x = 0;
					
					secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
					secondmapping.write("target\t\t" + event + " :EcontainsA :attr{" + a + "}{" + c + "} .\n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
					secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
					secondmapping.write("target\t\t:attr{" + a + "}{" + c + "} :keyA :key{" + a + "}{" + c + "} .\n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
					secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
					secondmapping.write("target\t\t:attr{" + a + "}{" + c + "} :valueA {" + v + "} .\n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
					secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
					secondmapping.write("target\t\t:attr{" + a + "}{" + c + "} :typeA \""+ s + "\"^^xsd:string .\n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
					
					
				}
			
		/*
		 * Write a mapping for multi-value attribute	
		 */
		} else if(key.equals("multiattribute")) {
			
			for(int i = 0; i < listofsql.size(); i++) {	
				
				String[] arr = listofvar.get(i).split(" ");
				String v = arr[0].replace("?", ""); // attribute value
				String a = arr[1].replace("?", ""); // attribute name
				String c = arr[2].replace("?", ""); // instance class containing attribute
				String event = ":event"; //event
				for(int j = 3; j < arr.length; j++) {
					String e = arr[j].replace("?", "");
					event = event + "{" + e + "}";
				}
				
				String[] temp = listofsql.get(i).split("as "+"`"+ a +"`");
				String[] temp2 = temp[0].split(" ");
				String attribute = temp2[temp2.length-1].replaceFirst("'", "<");
				attribute = attribute.replaceFirst("'", ">");
				
				String t = query.getType(attribute); // get attribute type from ontology file
				String s = convertType(t); // convert attribute type for this mapping
				
				int x = 0;
				
				secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
				secondmapping.write("target\t\t" + event +" :EcontainsA :attr{" + a + "}{" + c + "} .\n");
				secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
				
				secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
				secondmapping.write("target\t\t:attr{" + a + "}{" + c + "} :keyA :key{" + a + "}{" +c+ "} .\n");
				secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");	
				
				secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
				secondmapping.write("target\t\t:attr{" + a + "}{" + c + "} :typeA \"container\"^^xsd:string .\n");
				secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
				
				secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
				secondmapping.write("target\t\t:attr{" + a + "}{" + c + "} :AcontainsA :attr{" + a + "}{" + c + "}{" + v + "} .\n");
				secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
			
				secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
				secondmapping.write("target\t\t:attr{" + a + "}{" + c + "}{" + v + "} :keyA :key{" + a + "}{" + c + "}{" + v + "} .\n");
				secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");	
				
				secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
				secondmapping.write("target\t\t:attr{" + a + "}{" + c + "}{" + v + "} :valueA {" + v + "} .\n");
				secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
				
				secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
				secondmapping.write("target\t\t:attr{" + a + "}{" + c + "}{" + v + "} :typeA {" + s + "} .\n");
				secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
			}
		}  
			
			
		}		
		
		// write close tuple
		secondmapping.write("]]\n\n");
		secondmapping.flush();
		secondmapping.close();
		firstmapping.close();
	}
	
	/**
	 * A method for reading user's annotation.
	 * It scans annotation file and process it based on tag:
	 * trace, event, timestamp, activityname, attribute, or multiattribute.
	 * Then it calls processAnnotation method to process annotation based on its tag.
	 * 
	 * @param annotation
	 * 			A buffer containing user's annotation for domain ontology
	 * @throws Exception
	 */
	public void readAnnotation(BufferedReader annotation) throws Exception {
		System.out.println("enter readAnnotation");
		String input;
		
		while((input = annotation.readLine()) != null) {
			
			/*
			 * Trace annotation
			 */
			if(input.equalsIgnoreCase("trace")) {
				String trace = "";
				while((input = annotation.readLine()) != null && !input.equals("")) {
					trace = trace + " " + input;
				}
				processAnnotation(trace, "trace");
			
			/*
			 * Event annotation 
			 */
			} else if(input.equalsIgnoreCase("event")) {
				String event = "";
				while((input = annotation.readLine()) != null && !input.equals("")) {
					event = event + " " + input;
				}
				processAnnotation(event, "event");
				
			/*
			 * Timestamp annotation
			 */
			} else if(input.equalsIgnoreCase("timestamp")) {
				String timestamp = "";
				while((input = annotation.readLine()) != null && !input.equals("")) {
					timestamp = timestamp + " " + input;
				}
				processAnnotation(timestamp, "timestamp");
				
			/*
			 * Activity name annotation
			 */
			} else if(input.equalsIgnoreCase("activityname")) {
				String activityname = "";
				while((input = annotation.readLine()) != null && !input.equals("")) {
					activityname = activityname + " " + input;
				}
				processAnnotation(activityname, "activityname");
				
			/*
			 * Attribute annotation 
			 */
			} else if(input.equalsIgnoreCase("attribute")) {
				String attribute = "";
				while((input = annotation.readLine()) != null && !input.equals("")) {
					attribute = attribute + " " + input;
				}
				processAnnotation(attribute, "attribute");
				
			/*
			 * Multi-value attribute annotation
			 */
			} else if(input.equalsIgnoreCase("multiattribute")) {
				String multiattribute = "";
				while((input = annotation.readLine()) != null && !input.equals("")) {
					multiattribute = multiattribute + " " + input;
				}
				processAnnotation(multiattribute, "multiattribute");
			}
		}
	}
	
	/**
	 * A method to retrieve variables appear in a Sparql Query (user's annotation).
	 * It is useful for unification when build the second mapping.
	 * 
	 * @param vars
	 * 			A Sparql Query that the variables will be taken.
	 * @return String containing variables in the query, each is separated by comma.
	 */
	private String readVariable(String vars) {
		vars = vars.replaceAll("[\\s\\n\\w\\d\\W]*select\\s+distinct\\s+", ""); // delete select distinct
		vars = vars.replaceAll("[\\s\\n\\w\\d\\W]*select\\s+", ""); // delete select
		vars = vars.replaceAll("where[\\s\\n\\w\\d\\W]*", ""); // delete the rest except the variables
		
		return vars;
	}
	
	/**
	 * A method for processing the annotation.
	 * First, it reads all variables appear in Sparql Query by calling readVariable method.
	 * It unfolds Sparql to become Sql Query by calling unfoldQuery in class Query.
	 * Then, it simplifies the Sql Query by calling simplifySQL in class Query.
	 * The rest is process to input Sql into hash of Sql Query based on annotation tag
	 * 
	 * @param sparql
	 * 			A Sparql Query contaning user annotation.
	 * @param key
	 * 			One of annotation tag: 
	 * 				trace, event, timestamp, activityname, attribute, or multiattribute.
	 * 			It will be used as a key in hash of Sql Query.
	 * @throws Exception
	 */
	private void processAnnotation(String sparql, String key) throws Exception {
		System.out.println("enter processAnnotation");
		/*
		 * Getting all variables appear in Sparql Query
		 */
		String vars = readVariable(sparql.toLowerCase());
		
		/*
		 * Unfolding Sparql to Sql
		 */
		String sql = query.unfoldQuery(sparql);
		//query.runQuery(sparql); // for testing only, you may erase it
		//System.out.println("-----unfold query: "+sql);
		
		/*
		 * Simplified the Sql 
		 */
		String sqlSimplified = query.simplifySQL(sql);
		//System.out.println("-----simplified query: "+sqlSimplified);
		
		/*
		 * Put simplified Sql into hash of Sql. The key of hash is annotation tag.
		 */
		LinkedList<String> list;
		LinkedList<String> list2;
		if(hashOfSql.containsKey(key)) {
			list = hashOfSql.get(key);
			list2 = hashOfVar.get(key);
		} else {
			list = new LinkedList<String>();	
			list2 = new LinkedList<String>();
		}
		list.add(sqlSimplified);
		hashOfSql.put(key, list);
		list2.add(vars);
		hashOfVar.put(key, list2);
	}

	/**
	 * A method for getting skolem function of an attribute.
	 * 
	 * @param type
	 * 			The type of attribute.
	 * @return String of skolem function.
	 */
	private String convertType(String type) {
		String skolem = "";
		
		if(type.equals("String")) skolem = "literal";
		else if(type.equals("Integer")) skolem = "discrete";
		else if(type.equals("Float")) skolem = "continuous";
		else if(type.equals("Boolean")) skolem = "boolean";
		else if(type.equals("DateTime")) skolem = "timestamp";
		else if(type.equals("Id")) skolem = "id";
		else if(type.equals("List")) skolem = "list";
		else if(type.equals("Container")) skolem = "container";
		
		return skolem;
	}
	
}
