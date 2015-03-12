package org.processmining.framework.plugin.annotations;

/**
 * Plugin Category
 * @author mleemans
 *
 * Possible categories specifying the 'type' of functionality the plugin provides.
 */
public enum PluginCategory
{
	Discovery("Discovery", "Given an event log, construct a model"),
	ConformanceChecking("Conformance Checking", "Given a model and an event log, do conformance checking"),
	Enhancement("Enhancement", ""),
	Filtering("Filtering", ""),
	Analytics("Analytics", "");
	
	private final String name;
	private final String description;

	private PluginCategory(String name, String description)
	{
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
}
