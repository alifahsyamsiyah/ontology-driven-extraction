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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;

public class Annotation {
	
	private String ontologyFile;
	private String annotationFile;
	private String firstmappingFile;
	private String secondmappingFile;
	
	private Query query;
	
	
	private String flag;
	private String vars;
	private LinkedList<String> simplifiedSql;
	private Hashtable<String, String> constants;
	
	private BufferedReader firstmapping;
	private BufferedWriter secondmapping;
	private BufferedReader annotationReader;
	
	private Integer counter;
	
	static String prefix;
	
	public Annotation(String ontologyFile, String firstmappingFile, String annotationFile, String secondmappingFile) throws OWLOntologyCreationException, IOException, InvalidPredicateDeclarationException, InvalidMappingException {
		this.ontologyFile = ontologyFile;
		this.firstmappingFile = firstmappingFile;
		this.annotationFile = annotationFile;
		this.secondmappingFile = secondmappingFile;
		this.firstmapping = new BufferedReader(new FileReader(firstmappingFile));
		this.secondmapping = new BufferedWriter(new FileWriter(secondmappingFile));
		this.annotationReader = new BufferedReader(new FileReader(annotationFile));
		this.counter = 0;
		
		this.query = new Query(ontologyFile, firstmappingFile);
	}
	
	public void readAnnotation() throws Exception {
		
		String input;
		while(!((input = annotationReader.readLine()).equalsIgnoreCase("trace"))) {
			if(input.contains(" : ")) {
				String[] tmp = input.split(" : ");
				prefix = tmp[1].replace("<", "").replace(">", "");
			}
		}
		
		writeHeader();
		
		while(input != null) {
			if(input.equalsIgnoreCase("trace")) {
				String trace = "PREFIX : <"+prefix+">\n";
				while((input = annotationReader.readLine()) != null && !input.equals("")) {
					trace = trace + " " + input;
				}
				flag = "trace";
				processAnnotation(trace);
				writeAnnotation();
			} else if(input.equalsIgnoreCase("event")) {
				String event = "PREFIX : <"+prefix+">\n";
				while((input = annotationReader.readLine()) != null && !input.equals("")) {
					event = event + " " + input;
				}
				flag = "event";
				processAnnotation(event);
				writeAnnotation();
			} else if(input.equalsIgnoreCase("timestamp")) {
				String timestamp = "PREFIX : <"+prefix+">\n";
				while((input = annotationReader.readLine()) != null && !input.equals("")) {
					timestamp = timestamp + " " + input;
				}
				flag = "timestamp";
				processAnnotation(timestamp);
				writeAnnotation();
			} else if(input.equalsIgnoreCase("activityname")) {
				String activityname = "PREFIX : <"+prefix+">\n";
				while((input = annotationReader.readLine()) != null && !input.equals("")) {
					activityname = activityname + " " + input;
				}
				flag = "activityname";
				processAnnotation(activityname);
				writeAnnotation();
			} else if(input.equalsIgnoreCase("resource")) {
				String resource = "PREFIX : <"+prefix+">\n";
				while((input = annotationReader.readLine()) != null && !input.equals("")) {
					resource = resource + " " + input;
				}
				flag = "resource";
				processAnnotation(resource);
				writeAnnotation();
			} else if(input.equalsIgnoreCase("lifecycle")) {
				String lifecycle = "PREFIX : <"+prefix+">\n";
				while((input = annotationReader.readLine()) != null && !input.equals("")) {
					lifecycle = lifecycle + " " + input;
				}
				flag = "lifecycle";
				processAnnotation(lifecycle);
				writeAnnotation();
			} else if(input.equalsIgnoreCase("attribute")) {
				String attribute = "PREFIX : <"+prefix+">\n";
				while((input = annotationReader.readLine()) != null && !input.equals("")) {
					attribute = attribute + " " + input;
				}
				flag = "attribute";
				processAnnotation(attribute);
				writeAnnotation();
			} else if(input.equalsIgnoreCase("multiattribute")) {
				String multiattribute = "PREFIX : <"+prefix+">\n";
				while((input = annotationReader.readLine()) != null && !input.equals("")) {
					multiattribute = multiattribute + " " + input;
				}
				flag = "multiattribute";
				processAnnotation(multiattribute);
				writeAnnotation();
			}
			
			input = annotationReader.readLine();
		}
		
		secondmapping.write("]]\n\n");
		secondmapping.flush();
		secondmapping.close();
		firstmapping.close();
		annotationReader.close();
		
	}
	
	private void writeHeader() throws IOException {
		secondmapping.write("[PrefixDeclaration]\n");
		secondmapping.write(":\t\t"+prefix+"\n");
		String line = firstmapping.readLine();
		while(!(line = firstmapping.readLine()).contains("[MappingDeclaration]")) {
			secondmapping.write(line + "\n");
		}
		secondmapping.write("[MappingDeclaration] @collection [[\n");
	}
	
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
	
	private String readVariable(String vars) {
		vars = vars.replaceAll("[\\s\\n\\w\\d\\W]*select\\s+distinct\\s+", ""); // delete select distinct
		vars = vars.replaceAll("[\\s\\n\\w\\d\\W]*select\\s+", ""); // delete select
		vars = vars.replaceAll("where[\\s\\n\\w\\d\\W]*", ""); // delete the rest except the variables
		
		return vars;
	}
	
	private void processAnnotation(String sparql) throws Exception {
		vars = readVariable(sparql.toLowerCase());
		
		String sql = query.unfoldQuery(sparql);
		
		simplifiedSql = query.simplifySQL(sql); 
		
		constants = query.hashconstant;
	}
	
	private void writeAnnotation() throws IOException, OWLOntologyCreationException {
		for(int i = 0; i < simplifiedSql.size(); i++) {
			if(flag.equals("trace")) {
				String[] arr = vars.split(" ");
				String t = arr[0].replace("?", ""); // trace
				String l = arr[1].replace("?", "");  // log 
				
				t = constants.get(t).replace(" ","");
				l = constants.get(l).replace(" ","");
				
				String valuet = ":"; // value trace

				Pattern pattern = Pattern.compile("\\{(.+?)\\}"); // pattern {...}
			    Matcher matcher = pattern.matcher(t);
			    int start;
			    int end;
			    while (matcher.find()) {
			        start = matcher.start();
			        end = matcher.end();
			        valuet = valuet + t.substring(start, end);
			    }
				
				secondmapping.write("mappingId\t"+ flag + counter + "\n"); counter++;
				secondmapping.write("target\t\t:log/" + l + " :LcontainsT :trace/" + t + " . \n");
				secondmapping.write("source\t\t" + simplifiedSql.get(i) + "\n\n");
				
				secondmapping.write("mappingId\t"+ flag + counter + "\n"); counter++;
				secondmapping.write("target\t\t:trace/" + t + " :TcontainsA :attr/" + t + " . \n");
				secondmapping.write("source\t\t" + simplifiedSql.get(i) + "\n\n");
				
				secondmapping.write("mappingId\t"+ flag + counter + "\n"); counter++;
				secondmapping.write("target\t\t:attr/" + t + " :keyA \"concept:name\"^^xsd:string ;"
										+ " :valueA " + valuet + " ;"
												+ " :typeA \"literal\"^^xsd:string . \n");
				secondmapping.write("source\t\t" + simplifiedSql.get(i) + "\n\n");
				
			} else if(flag.equals("event")) {
				String[] arr = vars.split(" ");
				String event = ":event";
				for(int j = 0; j < arr.length-1; j++) {
					String e = arr[j].replace("?", "");
					event = event + "/" + constants.get(e).replace(" ","");
				}

				String t = arr[arr.length-1].replace("?", ""); // trace
				t = constants.get(t).replace(" ","");
				
				secondmapping.write("mappingId\t"+ flag + counter+ "\n"); counter++; 
				secondmapping.write("target\t\t:trace/" + t + " :TcontainsE "+ event + " . \n");
				secondmapping.write("source\t\t" + simplifiedSql.get(i) + "\n\n");
				
			} else if(flag.equals("timestamp")) {
				String[] arr = vars.split(" ");
				String n = arr[0].replace("?", ""); // variable timestamp
				String valuen = ""; // value timestamp
				String objectn = constants.get(n).replace(" ",""); // object timestamp
				
				if(objectn.contains("{")) {
					valuen = ":";
					Pattern pattern = Pattern.compile("\\{(.+?)\\}"); // pattern {...}
				    Matcher matcher = pattern.matcher(objectn);
				    int start;
				    int end;
				    while (matcher.find()) {
				        start = matcher.start();
				        end = matcher.end();
				        valuen = valuen + objectn.substring(start, end);
				    }
				} else {
					valuen = "\""+constants.get(n)+"\"^^xsd:string"; // if value is a constant
				}
				
				String event = ":event"; //event
				for(int j = 1; j < arr.length; j++) {
					String e = arr[j].replace("?", "");
					String ev = constants.get(e).replace(" ","");
					event = event + "/" + ev;
					objectn = objectn + "/" + ev;
				}
				
				secondmapping.write("mappingId\t"+ flag + counter + "\n"); counter++;
				secondmapping.write("target\t\t"+ event + " :EcontainsA :attr/" + objectn + " . :attr/" + objectn + " :keyA \"time:timestamp\"^^xsd:string ; "
						+ ":typeA \"timestamp\"^^xsd:string ; "
						+ ":valueA " + valuen + " . \n");
				secondmapping.write("source\t\t" + simplifiedSql.get(i) + "\n\n");
				
			} else if(flag.equals("activityname")) {
				String[] arr = vars.split(" ");
				String n = arr[0].replace("?", ""); // variable activityname
				String valuen = ""; // value activityname
				String objectn = constants.get(n).replace(" ",""); // object activityname
				
				if(objectn.contains("{")) {
					valuen = ":";
					Pattern pattern = Pattern.compile("\\{(.+?)\\}"); // pattern {...}
				    Matcher matcher = pattern.matcher(objectn);
				    int start;
				    int end;
				    while (matcher.find()) {
				        start = matcher.start();
				        end = matcher.end();
				        valuen = valuen + objectn.substring(start, end);
				    }
				} else {
					valuen = "\""+constants.get(n)+"\"^^xsd:string"; // if value is a constant
				}
				
				String event = ":event"; //event
				for(int j = 1; j < arr.length; j++) {
					String e = arr[j].replace("?", "");
					String ev = constants.get(e).replace(" ","");
					event = event + "/" + ev;
					objectn = objectn + "/" + ev;
				}
				
				secondmapping.write("mappingId\t"+ flag + counter + "\n"); counter++;
				secondmapping.write("target\t\t"+ event + " :EcontainsA :attr/" + objectn + " . :attr/" + objectn + " :keyA \"concept:name\"^^xsd:string ; "
						+ ":typeA \"literal\"^^xsd:string ; "
						+ ":valueA " + valuen + " . \n");
				secondmapping.write("source\t\t" + simplifiedSql.get(i) + "\n\n");
				
				
			} else if(flag.equals("resource")) {
				String[] arr = vars.split(" ");
				String n = arr[0].replace("?", ""); // variable resource
				String valuen = ""; // value resource
				String objectn = constants.get(n).replace(" ",""); // object resource
				
				if(objectn.contains("{")) {
					valuen = ":";
					Pattern pattern = Pattern.compile("\\{(.+?)\\}"); // pattern {...}
				    Matcher matcher = pattern.matcher(objectn);
				    int start;
				    int end;
				    while (matcher.find()) {
				        start = matcher.start();
				        end = matcher.end();
				        valuen = valuen + objectn.substring(start, end);
				    }
				} else {
					valuen = "\""+constants.get(n)+"\"^^xsd:string"; // if value is a constant
				}
				
				String event = ":event"; //event
				for(int j = 1; j < arr.length; j++) {
					String e = arr[j].replace("?", "");
					String ev = constants.get(e).replace(" ","");
					event = event + "/" + ev;
					objectn = objectn + "/" + ev;
				}
				
				secondmapping.write("mappingId\t"+ flag + counter + "\n"); counter++;
				secondmapping.write("target\t\t"+ event + " :EcontainsA :attr/" + objectn + " . :attr/" + objectn + " :keyA \"org:resource\"^^xsd:string ; "
						+ ":typeA \"literal\"^^xsd:string ; "
						+ ":valueA " + valuen + " . \n");
				secondmapping.write("source\t\t" + simplifiedSql.get(i) + "\n\n");
				
			} else if(flag.equals("lifecycle")) {
				String[] arr = vars.split(" ");
				String n = arr[0].replace("?", ""); // variable lifecycle
				String valuen = ""; // value lifecycle
				String objectn = constants.get(n).replace(" ",""); // object lifecycle
				
				if(objectn.contains("{")) {
					valuen = ":";
					Pattern pattern = Pattern.compile("\\{(.+?)\\}"); // pattern {...}
				    Matcher matcher = pattern.matcher(objectn);
				    int start;
				    int end;
				    while (matcher.find()) {
				        start = matcher.start();
				        end = matcher.end();
				        valuen = valuen + objectn.substring(start, end);
				    }
				} else {
					valuen = "\""+constants.get(n)+"\"^^xsd:string"; // if value is a constant
				}
				
				String event = ":event"; //event
				for(int j = 1; j < arr.length; j++) {
					String e = arr[j].replace("?", "");
					String ev = constants.get(e).replace(" ","");
					event = event + "/" + ev;
					objectn = objectn + "/" + ev;
				}
				
				secondmapping.write("mappingId\t"+ flag + counter + "\n"); counter++;
				secondmapping.write("target\t\t"+ event + " :EcontainsA :attr/" + objectn + " . :attr/" + objectn + " :keyA \"lifecycle:transition\"^^xsd:string ; "
						+ ":typeA \"literal\"^^xsd:string ; "
						+ ":valueA " + valuen + " . \n");
				secondmapping.write("source\t\t" + simplifiedSql.get(i) + "\n\n");
				
			} else if(flag.equals("attribute")) {
				String[] arr = vars.split(" ");
				
				// variable attribute in query (v) and value attribute (valuev)
				String v = arr[0].replace("?", ""); 
				v = constants.get(v).replace(" ",""); 
				String valuev = ""; 
				
				if(v.contains("{")) {
					valuev = ":";
					Pattern pattern = Pattern.compile("\\{(.+?)\\}"); // pattern {...}
				    Matcher matcher = pattern.matcher(v);
				    int start;
				    int end;
				    while (matcher.find()) {
				        start = matcher.start();
				        end = matcher.end();
				        valuev = valuev + v.substring(start, end);
				    }
				} else {
					valuev = "\""+constants.get(v)+"\"^^xsd:string"; // if value is a constant
				}
				
				// attribute key -must be the name of attribute of the class (a) and key attribute (keyattr)
				String a = arr[1].replace("?", ""); 
				a = constants.get(a).replace(" ","");
				String attribute = a;
				a = a.replaceAll("http[\\p{Punct}\\w\\d]+#", "").replaceAll(" ", "");
				String keyattr = "\""+a+"\"^^xsd:string";
				
				// instance class containing attribute to create object attribute (c)
				String c = arr[2].replace("?", ""); 
				c = constants.get(c).replace(" ","");
				
				//event
				String event = ":event"; 
				for(int j = 3; j < arr.length; j++) {
					String e = arr[j].replace("?", "");
					String ev = constants.get(e).replace(" ","");
					event = event + "/" + ev;
				}
				
				// to get the type of attribute
				String t = query.getType(attribute); // get attribute type from ontology file
				String s = convertType(t); // convert attribute type for this mapping
				
				secondmapping.write("mappingId\t"+ flag + counter + "\n"); counter++;
				secondmapping.write("target\t\t" + event + " :EcontainsA :attr/" + a + "/" + c + " .\n");
				secondmapping.write("source\t\t" + simplifiedSql.get(i) + "\n\n");
				
				secondmapping.write("mappingId\t"+ flag + counter + "\n"); counter++;
				secondmapping.write("target\t\t:attr/" + a + "/" + c + " :keyA " + keyattr + " ; "
						+ ":valueA " + valuev + " ; "
								+ ":typeA \""+ s + "\"^^xsd:string . \n");
				secondmapping.write("source\t\t" + simplifiedSql.get(i) + "\n\n");
				
			} else if(flag.equals("multiattribute")) {
				String[] arr = vars.split(" ");
				
				// variable attribute in query (v) and value attribute (valuev)
				String v = arr[0].replace("?", ""); 
				v = constants.get(v).replace(" ",""); 
				String valuev = ""; 
				
				if(v.contains("{")) {
					valuev = ":";
					Pattern pattern = Pattern.compile("\\{(.+?)\\}"); // pattern {...}
				    Matcher matcher = pattern.matcher(v);
				    int start;
				    int end;
				    while (matcher.find()) {
				        start = matcher.start();
				        end = matcher.end();
				        valuev = valuev + v.substring(start, end);
				    }
				} else {
					valuev = "\""+constants.get(v)+"\"^^xsd:string"; // if value is a constant
				}
				
				// attribute key -must be the name of attribute of the class (a) and key attribute (keyattr)
				String a = arr[1].replace("?", ""); 
				a = constants.get(a).replace(" ","");
				String attribute = a;
				a = a.replaceAll("http[\\p{Punct}\\w\\d]+#", "").replaceAll(" ", "");
				String keyattr = "\""+a+"\"^^xsd:string";
				
				// instance class containing attribute to create object attribute (c)
				String c = arr[2].replace("?", ""); 
				c = constants.get(c).replace(" ","");
				
				//event
				String event = ":event"; 
				for(int j = 3; j < arr.length; j++) {
					String e = arr[j].replace("?", "");
					String ev = constants.get(e).replace(" ","");
					event = event + "/" + ev;
				}
				
				// to get the type of attribute
				String t = query.getType(attribute); // get attribute type from ontology file
				String s = convertType(t); // convert attribute type for this mapping
				
				secondmapping.write("mappingId\t"+ flag + counter + "\n"); counter++;
				secondmapping.write("target\t\t" + event +" :EcontainsA :attr/" + a + "/" + c + " .\n");
				secondmapping.write("source\t\t" + simplifiedSql.get(i) + "\n\n");
				
				secondmapping.write("mappingId\t"+ flag + counter + "\n"); counter++;
				secondmapping.write("target\t\t:attr/" + a + "/" + c + " :keyA :key/" + a + "/" + c + " ; "
						+ ":typeA \"container\"^^xsd:string ; "
							+ ":AcontainsA :attr/" + a + "/" + c + "/" + v + " . \n");
				secondmapping.write("source\t\t" + simplifiedSql.get(i) + "\n\n");
				
				secondmapping.write("mappingId\t"+ flag + counter + "\n"); counter++;
				secondmapping.write("target\t\t:attr/" + a + "/" + c + "/" + v + " :keyA :key/" + a + "/" + c + "/" + v + " ; "
						+ ":valueA " + valuev + " ; "
								+ ":typeA \""+ s + "\"^^xsd:string  . \n");
				secondmapping.write("source\t\t" + simplifiedSql.get(i) + "\n\n");
			}
		}
	}
}
