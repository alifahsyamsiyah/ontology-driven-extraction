package Controller;


import java.io.BufferedReader;
import java.io.FileReader;
import Process.Annotation;

public class InputFrameController {
	String path = "src/main/resources/example/";
	public void createSecondMapping(String owlFile, String obdaFile, String annotationFile) throws Exception{
		Annotation annotation = new Annotation(path+owlFile, path+obdaFile, path+annotationFile, path+"secondmapping.obda");		
		
		/*
		 * Read annotation from user and retrieve automatic mapping to event ontology
		 */
		annotation.readAnnotation();
		
		System.out.println("Well done!");
		
	}
}
