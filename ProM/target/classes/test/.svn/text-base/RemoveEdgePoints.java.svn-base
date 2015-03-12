package test;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.connections.ConnectionCannotBeObtained;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.models.connections.GraphLayoutConnection;
import org.processmining.models.graphbased.directed.DirectedGraph;
import org.processmining.models.graphbased.directed.DirectedGraphEdge;
import org.processmining.models.graphbased.undirected.UndirectedGraph;
import org.processmining.models.graphbased.undirected.UndirectedGraphEdge;

public class RemoveEdgePoints {

	@Plugin(name = "Remove edgepoints", parameterLabels = { "graph" }, returnLabels = {}, returnTypes = { }, userAccessible = true, mostSignificantResult = -1)
	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "B.F. van Dongen", email = "b.f.v.dongen@tue.nl")
	@SuppressWarnings("rawtypes")
	public static void removeEdgePoints(PluginContext context, Object graph) throws ConnectionCannotBeObtained {
		GraphLayoutConnection conn = context.getConnectionManager().getFirstConnection(GraphLayoutConnection.class,
				context, graph);

		if (graph instanceof DirectedGraph) {
			DirectedGraph<?, ?> g = (DirectedGraph) graph;
			for (DirectedGraphEdge edge : g.getEdges()) {
				conn.setEdgePoints(edge, new ArrayList<Point2D>(0));
			}

			conn.updated();
		} else if (graph instanceof UndirectedGraph) {
			UndirectedGraph<?, ?> g = (UndirectedGraph) graph;
			for (UndirectedGraphEdge edge : g.getEdges()) {
				conn.setEdgePoints(edge, new ArrayList<Point2D>(0));
			}

			conn.updated();
		}
	}

}
