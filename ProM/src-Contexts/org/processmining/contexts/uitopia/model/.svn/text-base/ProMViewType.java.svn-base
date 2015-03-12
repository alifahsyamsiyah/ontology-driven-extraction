package org.processmining.contexts.uitopia.model;

import org.deckfour.uitopia.api.model.Resource;
import org.deckfour.uitopia.api.model.ResourceType;
import org.deckfour.uitopia.api.model.View;
import org.deckfour.uitopia.api.model.ViewType;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.contexts.uitopia.hub.ProMViewManager;
import org.processmining.framework.plugin.PluginParameterBinding;
import org.processmining.framework.util.Pair;

public class ProMViewType extends AbstractAuthored implements ViewType {

	private final ResourceType acceptedType;
	private final Pair<Integer, PluginParameterBinding> binding;
	private final ProMViewManager manager;
	private String name;

	public ProMViewType(ProMViewManager manager, ResourceType acceptedType,
			Pair<Integer, PluginParameterBinding> binding) {
		this.manager = manager;
		this.acceptedType = acceptedType;
		this.binding = binding;
		name = binding.getSecond().getPlugin().getAnnotation(Visualizer.class).name();
		if (name.equals(Visualizer.USEPLUGINNAME)) {
			name = binding.getSecond().getPlugin().getName();
		}
	}

	public View createView(Resource res) throws IllegalArgumentException {
		assert (res instanceof ProMResource);
		ProMResource<?> pr = ((ProMResource<?>) res);
		return new ProMView(manager, this, pr, res.getName(), binding);
	}

	public String getTypeName() {
		return name;
	}

	public ResourceType getViewableType() {
		return acceptedType;
	}

	public String toString() {
		return getTypeName();
	}

	public boolean equals(Object o) {
		if (o instanceof ProMViewType) {
			ProMViewType vt = (ProMViewType) o;
			return vt.binding.equals(binding);
		}
		return false;
	}

	public int hashCode() {
		return binding.hashCode();
	}
}
