


import java.io.File;

import javax.swing.JOptionPane;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;

import EventLog.Extract;


public class HelloWorld {
	
	
	 @Plugin(
             name = "My Hello World Plugin", 
             parameterLabels = {}, 
             returnLabels = { "Hello world string" }, 
             returnTypes = { Integer.class }, 
             userAccessible = true, 
             help = "Produces the string: 'Hello world'"
     )
        @UITopiaVariant(
                affiliation = "My company", 
                author = "My name", 
                email = "My e-mail address"
        )
        public static Integer helloWorld(PluginContext context) {
      
                return new Integer(3);
        }
        
        @Plugin(
                name = "My Hello World Plugin0", 
                parameterLabels = {"first", "second", "third"}, 
                returnLabels = { "Hello world string","Hello world string2" }, 
                returnTypes = { String.class, String.class }, 
                userAccessible = true, 
                
                help = "Produces the string: 'Hello world'"
        )
        @PluginVariant(variantLabel = "Your plug-in name, parameters", requiredParameterLabels = { 0, 1, 2 })
        @UITopiaVariant(
                affiliation = "My company", 
                author = "My name", 
                email = "My e-mail address"
        )
        public static String[] helloWorld0(UIPluginContext context) {
      
                return new String[] { "Hello", "Worlds" };
        }
        
        @Plugin(
                name = "My 5th Combine Worlds Plug-in", 
                parameterLabels = {}, 
                returnLabels = { "First string several second strings" }, 
                returnTypes = { String.class }, 
                userAccessible = true, 
                help = "Produces one string consisting of the first and a number of times a string given as input in a dialog."
        )
        @UITopiaVariant(
                affiliation = "My company", 
                author = "My name", 
                email = "My e-mail address"
        )
        public static Object helloWorlds(UIPluginContext context) {
                // Ask the user for his world
                String w = JOptionPane.showInputDialog(null, "What's the name of your world?",
                                "Enter your world", JOptionPane.QUESTION_MESSAGE);
                // change your result label
                context.getFutureResult(0).setLabel("Hello " + w + " string");
                // return the combined string
                return helloWorlds(context, "first", 3, w);
        }
        
        @Plugin(
                name = "My 4th Hello World Plug-in", 
                parameterLabels = {}, 
                returnLabels = { "Hello string", "Number", "Worlds string" }, 
                returnTypes = { String.class, Integer.class, String.class }, 
                userAccessible = true, 
                help = "Produces three objects: 'Hello', number, 'world'"
        )
        @UITopiaVariant(
                affiliation = "My company", 
                author = "My name", 
                email = "My e-mail address"
        )
        public static Object helloWorlds(PluginContext context, String first, Integer number, String second) {
                String s = first;
                for (int i = 0; i < number; i++) {
                        s += "," + second;
                }
                return s;
        }
        
        @Plugin(
                name = "My 5th Hello World Plug-in", 
                parameterLabels = {}, 
                returnLabels = {"Hello string", "Number", "Worlds string" }, 
                returnTypes = {Integer.class, String.class, String.class }, 
                userAccessible = true, 
                help = "Produces three objects: 'Hello', number, 'world'"
        )
        @UITopiaVariant(
                affiliation = "My company", 
                author = "My name", 
                email = "My e-mail address"
        )
        public static Object[] helloWorlds(PluginContext context) {
                return new Object[] { new Integer(6), "Hello",  "Worlds" };
        }
        
}