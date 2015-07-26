package ImportPlugin;

import java.io.File;
import java.io.InputStream;

import org.processmining.contexts.uitopia.annotations.UIImportPlugin;
import org.processmining.framework.abstractplugins.AbstractImportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;

import Model.OWLFile;

@Plugin(name = "Import Domain Ontology file", parameterLabels = { "Filename" }, returnLabels = { "Domain Ontology" }, returnTypes = { OWLFile.class })
@UIImportPlugin(description = "Domain Ontology files", extensions = { "owl" })
public class ImportDomainOntology extends AbstractImportPlugin {

        protected Object importFromStream(PluginContext context, InputStream input, String filename, long fileSizeInBytes)
                        throws Exception {
        	
        		File f = this.getFile();
        		String path = f.getAbsolutePath();
                
                OWLFile inputfile = new OWLFile(path);
                
                return inputfile;
        }
}
