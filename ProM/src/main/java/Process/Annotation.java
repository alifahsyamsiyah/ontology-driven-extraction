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
					//String l = arr[1].replace("?", ""); // log --- deleted just for a while
					int c = 0;
					
					secondmapping.write("mappingId\t"+ key + i + c + "\n"); c++;
					secondmapping.write("target\t\t:trace{" + t + "} a :Trace . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
					secondmapping.write("mappingId\t"+ key + i + c + "\n"); c++;
					secondmapping.write("target\t\t:ext{" + t + "} a :Extension . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
					secondmapping.write("mappingId\t"+ key + i + c + "\n"); c++;
					secondmapping.write("target\t\t:attr{" + t + "} a :Attribute . \n");
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
					
					secondmapping.write("mappingId\t"+ key + i + c + "\n"); c++;
					secondmapping.write("target\t\t:trace{" + t + "} :contain5 :attr{" + t + "} . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
					secondmapping.write("mappingId\t"+ key + i + c + "\n"); c++;
					secondmapping.write("target\t\t:ext{" + t + "} :declare2 :attr{" + t + "} . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
					
					/*secondmapping.write("mappingId\t"+ key + i + "\n");
					
					secondmapping.write("target\t\t:trace{" + t + "} a :Trace ."
							+ " :ext{" + t + "} a :Extension ."
							+ " :attr{" + t + "} a :Attribute ."
							+ " :attr{" + t + "} :keyA \"concept:name\"^^xsd:string ."
							+ " :attr{" + t + "} :valueA {" + t + "} ."
							+ " :attr{" + t + "} :typeA \"literal\"^^xsd:string ."
							+ " :trace{" + t + "} :contain5 :attr{" + t + "} ."
							+ " :ext{" + t + "} :declare2 :attr{" + t + "} .\n");
					
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");*/
					
				}
			
			/*
			 * Write a mapping for event
			 */
			} else if(key.equals("event")) {

				for(int i = 0; i < listofsql.size(); i++) {					
					String[] arr = listofvar.get(i).split(" ");
					String e = arr[0].replace("?", ""); // event
					String t = arr[1].replace("?", ""); // trace
					
					int c = 0;
					
					secondmapping.write("mappingId\t"+ key + i + c + "\n"); c++; 
					secondmapping.write("target\t\t:event{" + e + "} a :Event . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
					secondmapping.write("mappingId\t"+ key + i + c + "\n"); c++; 
					secondmapping.write("target\t\t:trace{" + t + "} a :Trace . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
					secondmapping.write("mappingId\t"+ key + i + c + "\n"); c++; 
					secondmapping.write("target\t\t:trace{" + t + "} :contain2 :event{" + e + "} . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
					/*secondmapping.write("mappingId\t"+ key + i + "\n"); 
					
					secondmapping.write("target\t\t:event{" + e + "} a :Event ."
							+ ":trace{" + t + "} a :Trace ."
							+ ":trace{" + t + "} :contain2 :event{" + e + "} .\n");
					
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");*/
				}
				
			/*
			 * Write a mapping for event from attribute
			 */	
			} else if(key.equals("event-attribute")) {

				for(int i = 0; i < listofsql.size(); i++) {					
					String[] arr = listofvar.get(i).split(" ");
					String e = arr[0].replace("?", ""); // attribute which is annotated as an event
					String a = arr[1].replace("?", ""); // class containing the event
					String t = arr[2].replace("?", ""); // trace
					
					int c = 0;
					
					secondmapping.write("mappingId\t"+ key + i + c + "\n"); c++; 
					secondmapping.write("target\t\t:event{" + e + "}{" + a + "} a :Event . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
					secondmapping.write("mappingId\t"+ key + i + c + "\n"); c++; 
					secondmapping.write("target\t\t:trace{" + t + "} a :Trace . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
					secondmapping.write("mappingId\t"+ key + i + c + "\n"); c++; 
					secondmapping.write("target\t\t:trace{" + t + "} :contain2 :event{" + e + "}{" + a + "} . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
				
				}
			
			/*
			 * Write a mapping for timestamp
			 */
			} else if(key.equals("timestamp")) {
				
				for(int i = 0; i < listofsql.size(); i++) {					
					String[] arr = listofvar.get(i).split(" ");
					String t = arr[0].replace("?", ""); // timestamp
					String e = arr[1].replace("?", ""); // event
					int c = 0;
					
					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:event{" + e + "} a :Event . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:ext{" + t + "}{" +e+ "} a :Extension . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");

					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:attr{" + t + "}{" +e+ "} a :Attribute . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");

					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:attr{" + t + "}{" +e+ "} :keyA \"time:timestamp\"^^xsd:string . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");

					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:attr{" + t + "}{" +e+ "} :typeA \"timestamp\"^^xsd:string . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");

					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:attr{" + t + "}{" +e+ "} :valueA {" + t + "} . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");

					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:event{" + e + "} :contain3 :attr{" + t + "}{" +e+ "} . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");

					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:ext{" + t + "}{" +e+ "} :declare2 :attr{" + t + "}{" +e+ "} . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
					/*secondmapping.write("mappingId\t"+ key + i + "\n"); 
					secondmapping.write("target\t\t:event{" + e + "} a :Event ."
							+ ":ext{" + t + "}{" +e+ "} a :Extension ."
							+ ":attr{" + t + "}{" +e+ "} a :Attribute ."
							+ ":attr{" + t + "}{" +e+ "} :keyA \"time:timestamp\"^^xsd:string ."
							+ ":attr{" + t + "}{" +e+ "} :typeA \"timestamp\"^^xsd:string ."
							+ ":attr{" + t + "}{" +e+ "} :valueA {" + t + "} ."
							+ ":event{" + e + "} :contain3 :attr{" + t + "}{" +e+ "}."
							+ ":ext{" + t + "}{" +e+ "} :declare2 :attr{" + t + "}{" +e+ "} .\n");
					
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");*/
				}
				
			/*
			 * Write a mapping for timestamp from event-attribute
			 */
			} else if(key.equals("timestamp-attribute")) {
					
				for(int i = 0; i < listofsql.size(); i++) {					
					String[] arr = listofvar.get(i).split(" ");
					String t = arr[0].replace("?", ""); // timestamp
					String e = arr[1].replace("?", ""); // attribute which is annotated as an event
					String a = arr[2].replace("?", ""); // class containing the event
					int c = 0;
						
					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:event{" + e + "}{" + a + "} a :Event . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
						
					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:ext{" + t + "}{" +e+ "} a :Extension . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");

					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:attr{" + t + "}{" +e+ "} a :Attribute . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");

					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:attr{" + t + "}{" +e+ "} :keyA \"time:timestamp\"^^xsd:string . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");

					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:attr{" + t + "}{" +e+ "} :typeA \"timestamp\"^^xsd:string . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");

					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:attr{" + t + "}{" +e+ "} :valueA {" + t + "} . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");

					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:event{" + e + "}{" + a + "} :contain3 :attr{" + t + "}{" +e+ "} . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");

					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:ext{" + t + "}{" +e+ "} :declare2 :attr{" + t + "}{" +e+ "} . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
				}
			
			/*
			 * Write a mapping for activityname
			 */
			} else if(key.equals("activityname")) {
				
				for(int i = 0; i < listofsql.size(); i++) {					
					String[] arr = listofvar.get(i).split(" ");
					String t = arr[0].replace("?", ""); // activityname
					String e = arr[1].replace("?", ""); // event
					int c = 0;
					
					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:event{" + e + "} a :Event . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:ext{" + t + "}{" +e+ "} a :Extension . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");

					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:attr{" + t + "}{" +e+ "} a :Attribute . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");

					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:attr{" + t + "}{" +e+ "} :keyA \"concept:name\"^^xsd:string . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");

					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:attr{" + t + "}{" +e+ "} :typeA \"literal\"^^xsd:string . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");

					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:attr{" + t + "}{" +e+ "} :valueA {" + t + "} . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");

					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:event{" + e + "} :contain3 :attr{" + t + "}{" +e+ "} . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");

					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:ext{" + t + "}{" +e+ "} :declare2 :attr{" + t + "}{" +e+ "} . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
					
					/*secondmapping.write("mappingId\t"+ key + i + "\n"); 
					
					secondmapping.write("target\t\t:event{" + e + "} a :Event ."
							+ ":ext{" + t + "}{" +e+ "} a :Extension ."
							+ ":attr{" + t + "}{" +e+ "} a :Attribute ."
							+ ":attr{" + t + "}{" +e+ "} :keyA \"concept:name\"^^xsd:string ."
							+ ":attr{" + t + "}{" +e+ "} :typeA \"literal\"^^xsd:string ."
							+ ":attr{" + t + "}{" +e+ "} :valueA {" + t + "} ."
							+ ":event{" + e + "} :contain3 :attr{" + t + "}{" +e+ "} ."
							+ ":ext{" + t + "}{" +e+ "} :declare2 :attr{" + t + "}{" +e+ "} .\n");
					
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");*/
				}
				
			/*
			 * Write a mapping for activityname from event-attribute
			 */
			} else if(key.equals("activityname-attribute")) {
					
				for(int i = 0; i < listofsql.size(); i++) {					
					String[] arr = listofvar.get(i).split(" ");
					String t = arr[0].replace("?", ""); // activityname
					String e = arr[1].replace("?", ""); // attribute which is annotated as an event
					String a = arr[2].replace("?", ""); // class containing the event
					int c = 0;
						
					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:event{" + e + "}{" + a + "} a :Event . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
						
					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:ext{" + t + "}{" +e+ "} a :Extension . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");

					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:attr{" + t + "}{" +e+ "} a :Attribute . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");

					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:attr{" + t + "}{" +e+ "} :keyA \"concept:name\"^^xsd:string . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");

					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:attr{" + t + "}{" +e+ "} :typeA \"literal\"^^xsd:string . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");

					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:attr{" + t + "}{" +e+ "} :valueA {" + t + "} . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");

					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:event{" + e + "}{" + a + "} :contain3 :attr{" + t + "}{" +e+ "} . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");

					secondmapping.write("mappingId\t"+ key + i + c+ "\n"); c++;
					secondmapping.write("target\t\t:ext{" + t + "}{" +e+ "} :declare2 :attr{" + t + "}{" +e+ "} . \n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
						
				}
			
			/*
			 * Write a mapping for other attribute
			 */
			} else if(key.equals("attribute")) {
				
				for(int i = 0; i < listofsql.size(); i++) {	
					
					String[] arr = listofvar.get(i).split(" ");
					String a = arr[0].replace("?", ""); // attribute value
					String r = arr[1].replace("?", ""); // attribute name
					String c = arr[2].replace("?", ""); // instance class containing attribute
					//String e = arr[3].replace("?", ""); // event		
					String e = c;
					
					String[] temp = listofsql.get(i).split("as "+"`"+r+"`");
					String[] temp2 = temp[0].split(" ");
					String attribute = temp2[temp2.length-1].replaceFirst("'", "<");
					attribute = attribute.replaceFirst("'", ">");
					
					String t = query.getType(attribute); // get attribute type from ontology file
					String s = convertType(t); // convert attribute type for this mapping
					
					int x = 0;
					
					secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
					secondmapping.write("target\t\t:event{" + e + "} a :Event .\n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
					secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
					secondmapping.write("target\t\t:ext{" + r + "}{" +c+ "} a :Extension .\n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
					secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
					secondmapping.write("target\t\t:attr{" + r + "}{" +c+ "} a :Attribute .\n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
					secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
					secondmapping.write("target\t\t:attr{" + r + "}{" +c+ "} :keyA :key{" + r + "}{" +c+ "} .\n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
					secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
					secondmapping.write("target\t\t:attr{" + r + "}{" +c+ "} :valueA {" + a + "} .\n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
					secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
					secondmapping.write("target\t\t:attr{" + r + "}{" +c+ "} :typeA \""+ s + "\"^^xsd:string .\n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
					secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
					secondmapping.write("target\t\t:event{" + e + "} :contain3 :attr{" + r + "}{" +c+ "} .\n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
					
					secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
					secondmapping.write("target\t\t:ext{" + r + "}{" +c+ "} :declare2 :attr{" + r + "}{" +c+ "} .\n");
					secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
				}
			
		/*
		 * Write a mapping for multi-value attribute	
		 */
		} else if(key.equals("multiattribute")) {
			
			for(int i = 0; i < listofsql.size(); i++) {	
				
				String[] arr = listofvar.get(i).split(" ");
				String a = arr[0].replace("?", ""); // attribute value
				String r = arr[1].replace("?", ""); // attribute name
				String c = arr[2].replace("?", ""); // instance class containing attribute
				String e = arr[3].replace("?", ""); // event					
				
				String[] temp = listofsql.get(i).split("as "+"`"+r+"`");
				String[] temp2 = temp[0].split(" ");
				String attribute = temp2[temp2.length-1].replaceFirst("'", "<");
				attribute = attribute.replaceFirst("'", ">");
				
				String t = query.getType(attribute); // get attribute type from ontology file
				String s = convertType(t); // convert attribute type for this mapping
				
				int x = 0;
				
				secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
				secondmapping.write("target\t\t:event{" + e + "} a :Event .\n");
				secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
				
				secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
				secondmapping.write("target\t\t:ext{" + r + "}{" +c+ "} a :Extension .\n");
				secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
				
				secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
				secondmapping.write("target\t\t:attr{" + r + "}{" +c+ "} a :Attribute .\n");
				secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
				
				secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
				secondmapping.write("target\t\t:attr{" + r + "}{" +c+ "} :keyA :key{" + r + "}{" +c+ "} .\n");
				secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");	
				
				secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
				secondmapping.write("target\t\t:attr{" + r + "}{" +c+ "} :typeA \"container\"^^xsd:string .\n");
				secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
				
				secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
				secondmapping.write("target\t\t:attr{" + a + "}{" +r+ "}{" +c+ "} a :Attribute .\n");
				secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
				
				secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
				secondmapping.write("target\t\t:attr{" + a + "}{" +r+ "}{" +c+ "} :keyA :key{" + a + "}{" +r+ "}{" +c+ "} .\n");
				secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");	
				
				secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
				secondmapping.write("target\t\t:attr{" + a + "}{" +r+ "}{" +c+ "} :valueA {" + a + "} .\n");
				secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
				
				secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
				secondmapping.write("target\t\t:attr{" + a + "}{" +r+ "}{" +c+ "} :typeA {" + s + "} .\n");
				secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
				
				secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
				secondmapping.write("target\t\t:attr{" + r + "}{" +c+ "} :contain6 :attr{" + a + "}{" +r+ "}{" +c+ "} .\n");
				secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
				
				secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
				secondmapping.write("target\t\t:event{" + e + "} :contain3 :attr{" + r + "}{" +c+ "} .\n");
				secondmapping.write("source\t\t" + listofsql.get(i) + "\n\n");
				
				secondmapping.write("mappingId\t"+ key + i + x + "\n"); x++;
				secondmapping.write("target\t\t:ext{" + r + "}{" +c+ "} :declare2 :attr{" + r + "}{" +c+ "} .\n");
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
			 * Event annotation from attribute
			 */
			} else if(input.equalsIgnoreCase("event-attribute")) {
				String event = "";
				while((input = annotation.readLine()) != null && !input.equals("")) {
					event = event + " " + input;
				}
				processAnnotation(event, "event-attribute");
				
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
			 * Timestamp annotation from event-attribute
			 */
			} else if(input.equalsIgnoreCase("timestamp-attribute")) {
				String timestamp = "";
				while((input = annotation.readLine()) != null && !input.equals("")) {
					timestamp = timestamp + " " + input;
				}
				processAnnotation(timestamp, "timestamp-attribute");
				
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
			 * Activity name annotation
			 */
			} else if(input.equalsIgnoreCase("activityname-attribute")) {
				String activityname = "";
				while((input = annotation.readLine()) != null && !input.equals("")) {
					activityname = activityname + " " + input;
				}
				processAnnotation(activityname, "activityname-attribute");
				
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
		/*
		 * Getting all variables appear in Sparql Query
		 */
		String vars = readVariable(sparql.toLowerCase());
		
		/*
		 * Unfolding Sparql to Sql
		 */
		String sql = query.unfoldQuery(sparql);
		//query.runQuery(sparql); // for testing only, you may erase it
		
		/*
		 * Simplified the Sql 
		 */
		String sqlSimplified = query.simplifySQL(sql);
		
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
