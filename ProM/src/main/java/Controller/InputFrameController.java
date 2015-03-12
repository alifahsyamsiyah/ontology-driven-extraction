package Controller;


import java.io.BufferedReader;
import java.io.FileReader;
import Process.Annotation;

public class InputFrameController {
	String path = "src/main/resources/example/";
	public void createSecondMapping(String owlFile, String obdaFile, String annotationFile) throws Exception{
		Annotation annotation = new Annotation(path+owlFile, path+obdaFile);		
		
		/*
		 * Read annotation from user and retrieve automatic mapping to event ontology
		 */
		annotation.readAnnotation(new BufferedReader(new FileReader(path+annotationFile)));
		
		/*
		 * Write the second mapping based on user annotation
		 */
		annotation.writeAnnotation(path+obdaFile,path+"secondmapping.obda");
		
		System.out.println("Well done!");
		
	}
}
