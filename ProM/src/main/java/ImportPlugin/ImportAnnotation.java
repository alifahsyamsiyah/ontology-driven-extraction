package ImportPlugin;

import java.io.File;
import java.io.InputStream;

import org.processmining.contexts.uitopia.annotations.UIImportPlugin;
import org.processmining.framework.abstractplugins.AbstractImportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;

import Model.AnnotationFile;

@Plugin(name = "Import Annotation file", parameterLabels = { "Filename" }, returnLabels = { "Annotation" }, returnTypes = { AnnotationFile.class })
@UIImportPlugin(description = "Annotation files", extensions = { "annotation" })
public class ImportAnnotation extends AbstractImportPlugin {

        protected Object importFromStream(PluginContext context, InputStream input, String filename, long fileSizeInBytes)
                        throws Exception {
                
                File f = this.getFile();
                String path = f.getAbsolutePath();
               
                AnnotationFile inputfile = new AnnotationFile(path);           
                
                return inputfile;
        }
}
