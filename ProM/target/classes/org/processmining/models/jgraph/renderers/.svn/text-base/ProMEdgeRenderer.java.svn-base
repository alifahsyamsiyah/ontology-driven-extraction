package org.processmining.models.jgraph.renderers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Point2D;

import org.jgraph.graph.EdgeRenderer;
import org.jgraph.graph.EdgeView;
import org.processmining.framework.util.Cleanable;
import org.processmining.models.graphbased.AttributeMap;
import org.processmining.models.graphbased.ViewSpecificAttributeMap;
import org.processmining.models.graphbased.directed.DirectedGraphEdge;
import org.processmining.models.jgraph.views.JGraphEdgeView;

public class ProMEdgeRenderer extends EdgeRenderer implements Cleanable {

	private static final long serialVersionUID = 4470395577059556630L;

	public void cleanUp() {
		view = null;
	}

	protected void paintLabel(Graphics g, String label, Point2D p, boolean mainLabel) {
		ViewSpecificAttributeMap map = ((JGraphEdgeView) view).getViewSpecificAttributeMap();
		DirectedGraphEdge<?, ?> edge = ((JGraphEdgeView) view).getEdge();
		if (map.get(edge, AttributeMap.SHOWLABEL, false) && !((JGraphEdgeView) view).isPIP()) {
			super.paintLabel(g, mainLabel ? map.get(edge, AttributeMap.LABEL, label) : label, p, mainLabel);
		}
	}

	/**
	 * Returns the label size of the specified view in the given graph.
	 */
	public Dimension getLabelSize(EdgeView view, String label) {
		ViewSpecificAttributeMap map = ((JGraphEdgeView) view).getViewSpecificAttributeMap();
		DirectedGraphEdge<?, ?> edge = ((JGraphEdgeView) view).getEdge();
		if (map.get(edge, AttributeMap.SHOWLABEL, false) && !((JGraphEdgeView) view).isPIP()) {
			return super.getLabelSize(view, map.get(edge, AttributeMap.LABEL, ""));
		} else {
			return new Dimension(0, 0);
		}
	}

	public void paint(Graphics g) {
		ViewSpecificAttributeMap map = ((JGraphEdgeView) view).getViewSpecificAttributeMap();
		DirectedGraphEdge<?, ?> edge = ((JGraphEdgeView) view).getEdge();
		if (map.get(edge, AttributeMap.EDGECOLOR, Color.BLACK) != null) {
			setForeground(map.get(edge, AttributeMap.EDGECOLOR, Color.BLACK));
		}
		if (map.get(edge, AttributeMap.LINEWIDTH, null) != null) {
			lineWidth = map.<Float>get(edge, AttributeMap.LINEWIDTH, null);
		}
		super.paint(g);
	}
}
