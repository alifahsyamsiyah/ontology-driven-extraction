package test;

import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.providedobjects.ProvidedObjectDeletedException;
import org.processmining.framework.providedobjects.ProvidedObjectID;

public class SerializationTests {
	static int i = 0;

	@Plugin(name = "Generate serialization test object", parameterLabels = {}, returnLabels = { "Test object" }, returnTypes = { RefObject.class }, userAccessible = true)
	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "B.F. van Dongen", email = "b.f.v.dongen@tue.nl")
	public static Object testSerialization(PluginContext context) {
		final String s = "Test object " + i++;
		context.getFutureResult(0).setLabel(s);
		return new RefObject(null, s);
	}

	@Plugin(name = "Generate referencing test object", parameterLabels = { "References Object" }, returnLabels = { "Test object" }, returnTypes = { RefObject.class }, userAccessible = true)
	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "B.F. van Dongen", email = "b.f.v.dongen@tue.nl")
	public static Object testSerialization(PluginContext context, final RefObject toReference) {
		final String s = "Test object " + i++;
		context.getFutureResult(0).setLabel(s + " referencing: " + toReference.toString());
		return new RefObject(toReference, s);
	}

	@Plugin(name = "Generate serialization updating object", parameterLabels = {}, returnLabels = { "Test object" }, returnTypes = { RefObject.class }, userAccessible = true)
	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "B.F. van Dongen", email = "b.f.v.dongen@tue.nl")
	public static Object testUpdating(PluginContext context) throws InterruptedException {
		String s = "Test object " + i++;
		context.getFutureResult(0).setLabel(s);

		RefObject o = new RefObject(null, s);

		ProvidedObjectID id = context.getProvidedObjectManager().createProvidedObject("Updating test object", o,
				context);
		int n = 15;
		context.getProgress().setMaximum(n);
		for (int j = 0; j < n; j++) {
			s = "Test object " + i++;
			try {
				o = new RefObject(new RefObject(o.ref, "Reference to o"), s);
				context.getProvidedObjectManager().changeProvidedObjectObject(id, o);
			} catch (ProvidedObjectDeletedException e) {
				break;
			}
			Thread.sleep(3000);
			context.getProgress().inc();
		}
		s = "Test object " + i++;
		return new RefObject(o, s);

	}

}

class RefObject {

	public final Object ref;
	public final String name;

	public final double[] memory = new double[10 * 1024 / 8];

	public RefObject(Object ref, String name) {
		this.ref = ref;
		this.name = name;

	}

	public String toString() {
		return name + (ref == null ? "" : " referencing: " + ref.toString());
	}

}