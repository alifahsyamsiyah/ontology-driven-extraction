package ImportPlugin;

import java.io.File;
import java.io.InputStream;

import org.processmining.contexts.uitopia.annotations.UIImportPlugin;
import org.processmining.framework.abstractplugins.AbstractImportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;

import Model.OBDAFile;

@Plugin(name = "Import First Mapping file", parameterLabels = { "Filename" }, returnLabels = { "First Mapping" }, returnTypes = { OBDAFile.class })
@UIImportPlugin(description = "First Mapping files", extensions = { "obda" })
public class ImportFirstMapping extends AbstractImportPlugin {

        protected Object importFromStream(PluginContext context, InputStream input, String filename, long fileSizeInBytes)
                        throws Exception {
        	
        		File f = this.getFile();
        		String path = f.getAbsolutePath();
                
                OBDAFile inputfile = new OBDAFile(path);
                
                return inputfile;
        }
}
