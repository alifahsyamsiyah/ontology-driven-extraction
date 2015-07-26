package ImportPlugin;

import java.io.File;
import java.io.InputStream;

import org.processmining.contexts.uitopia.annotations.UIImportPlugin;
import org.processmining.framework.abstractplugins.AbstractImportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;

import Model.BootstrapFile;

@Plugin(name = "Import Bootstrap file", parameterLabels = { "Filename" }, returnLabels = { "Bootstrap File" }, returnTypes = { BootstrapFile.class })
@UIImportPlugin(description = "Bootstrap files", extensions = { "txt" })
public class ImportBootstrap extends AbstractImportPlugin {

        protected Object importFromStream(PluginContext context, InputStream input, String filename, long fileSizeInBytes)
                        throws Exception {
        		File f = this.getFile();
        		String path = f.getAbsolutePath();
        	
        		BootstrapFile inputfile = new BootstrapFile(path);
                
                return inputfile;
        }
}
