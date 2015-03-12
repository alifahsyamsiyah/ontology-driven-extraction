package org.processmining.models.jgraph.elements;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.GraphConstants;
import org.processmining.framework.util.Cleanable;
import org.processmining.models.connections.GraphLayoutConnection;
import org.processmining.models.graphbased.AttributeMap;
import org.processmining.models.graphbased.AttributeMap.ArrowType;
import org.processmining.models.graphbased.directed.DirectedGraphEdge;
import org.processmining.models.jgraph.ModelOwner;
import org.processmining.models.jgraph.ProMGraphModel;
import org.processmining.models.jgraph.ProMLoopRouting;
import org.processmining.models.jgraph.views.JGraphEdgeView;

public class ProMGraphEdge extends DefaultEdge implements Cleanable, ModelOwner, ProMGraphElement {

	private static final long serialVersionUID = 663907031594522244L;
	private ProMGraphModel model;
	private JGraphEdgeView view;
	private final List<Point2D> internalPoints = new ArrayList<Point2D>(0);
	private DirectedGraphEdge<?, ?> edge;
	private final GraphLayoutConnection layoutConnection;

	public ProMGraphEdge(DirectedGraphEdge<?, ?> edge, ProMGraphModel model, GraphLayoutConnection layoutConnection) {
		super(edge.getLabel());
		this.layoutConnection = layoutConnection;

		GraphConstants.setRouting(getAttributes(), ProMLoopRouting.ROUTER);
		GraphConstants.setLabelPosition(getAttributes(), new Point2D.Double(GraphConstants.PERMILLE / 2, 0));
		GraphConstants.setLabelAlongEdge(getAttributes(), edge.getAttributeMap()
				.get(AttributeMap.LABELALONGEDGE, false));
		GraphConstants.setLineStyle(getAttributes(),
				edge.getAttributeMap().get(AttributeMap.STYLE, GraphConstants.STYLE_SPLINE));
		//GraphConstants.setLineStyle(getAttributes(), GraphConstants.STYLE_SPLINE);
		GraphConstants.setLineWidth(
				getAttributes(),
				new Float(edge.getAttributeMap().get(AttributeMap.LINEWIDTH,
						GraphConstants.getLineWidth(getAttributes()))));

		float[] pattern = edge.getAttributeMap().get(AttributeMap.DASHPATTERN, new float[0]);
		if (pattern.length > 0f) {

			GraphConstants.setDashPattern(getAttributes(), pattern);
			GraphConstants.setDashOffset(getAttributes(), edge.getAttributeMap().get(AttributeMap.DASHOFFSET, 0f));
		}

		GraphConstants.setForeground(getAttributes(), edge.getAttributeMap().get(AttributeMap.LABELCOLOR, Color.black));

		Integer arrow = null;
		ArrowType type = edge.getAttributeMap().get(AttributeMap.EDGESTART, ArrowType.ARROWTYPE_NONE);
		if (type == ArrowType.ARROWTYPE_CLASSIC) {
			arrow = GraphConstants.ARROW_CLASSIC;
		} else if (type == ArrowType.ARROWTYPE_CIRCLE) {
			arrow = GraphConstants.ARROW_CIRCLE;
		} else if (type == ArrowType.ARROWTYPE_DIAMOND) {
			arrow = GraphConstants.ARROW_DIAMOND;
		} else if (type == ArrowType.ARROWTYPE_DOUBLELINE) {
			arrow = GraphConstants.ARROW_DOUBLELINE;
		} else if (type == ArrowType.ARROWTYPE_LINE) {
			arrow = GraphConstants.ARROW_LINE;
		} else if (type == ArrowType.ARROWTYPE_SIMPLE) {
			arrow = GraphConstants.ARROW_SIMPLE;
		} else if (type == ArrowType.ARROWTYPE_TECHNICAL) {
			arrow = GraphConstants.ARROW_TECHNICAL;
		}

		if (arrow != null) {
			GraphConstants.setLineBegin(getAttributes(), arrow);
			GraphConstants
					.setBeginFill(getAttributes(), edge.getAttributeMap().get(AttributeMap.EDGESTARTFILLED, true));
		}

		arrow = null;
		type = edge.getAttributeMap().get(AttributeMap.EDGEEND, ArrowType.ARROWTYPE_NONE);
		if (type == ArrowType.ARROWTYPE_CLASSIC) {
			arrow = GraphConstants.ARROW_CLASSIC;
		} else if (type == ArrowType.ARROWTYPE_CIRCLE) {
			arrow = GraphConstants.ARROW_CIRCLE;
		} else if (type == ArrowType.ARROWTYPE_DIAMOND) {
			arrow = GraphConstants.ARROW_DIAMOND;
		} else if (type == ArrowType.ARROWTYPE_DOUBLELINE) {
			arrow = GraphConstants.ARROW_DOUBLELINE;
		} else if (type == ArrowType.ARROWTYPE_LINE) {
			arrow = GraphConstants.ARROW_LINE;
		} else if (type == ArrowType.ARROWTYPE_SIMPLE) {
			arrow = GraphConstants.ARROW_SIMPLE;
		} else if (type == ArrowType.ARROWTYPE_TECHNICAL) {
			arrow = GraphConstants.ARROW_TECHNICAL;
		}

		if (arrow != null) {
			GraphConstants.setLineEnd(getAttributes(), arrow);
			GraphConstants.setEndFill(getAttributes(), edge.getAttributeMap().get(AttributeMap.EDGEENDFILLED, true));
		}

		if (edge.getAttributeMap().containsKey(AttributeMap.EXTRALABELPOSITIONS)) {
			assert ((Point2D[]) edge.getAttributeMap().get(AttributeMap.EXTRALABELPOSITIONS)).length == ((String[]) edge
					.getAttributeMap().get(AttributeMap.EXTRALABELS)).length;
			Point2D[] points = (Point2D[]) edge.getAttributeMap().get(AttributeMap.EXTRALABELPOSITIONS);
			String[] labels = (String[]) edge.getAttributeMap().get(AttributeMap.EXTRALABELS);
			GraphConstants.setExtraLabelPositions(getAttributes(), points);
			GraphConstants.setExtraLabels(getAttributes(), labels);

		}

		this.edge = edge;
		this.model = model;

		internalPoints.addAll(layoutConnection.getEdgePoints(edge));

	}

	@SuppressWarnings("unchecked")
	public void updateViewsFromMap() {
		assert (view != null);
		// Update should not be called before any view is created,

		// Note that List is of type List<Point2D>, until list.add is called
		// later
		List list = new ArrayList(layoutConnection.getEdgePoints(edge));
		boolean pointsChanged = !internalPoints.equals(list);
		if (pointsChanged) {
			internalPoints.clear();
			List<Point2D> points = view.getPoints();
			internalPoints.addAll(list);
			list.add(0, points.get(0));
			list.add(points.get(points.size() - 1));
			view.setPoints(list);
		}
	}

	public List<Point2D> getInternalPoints() {
		return internalPoints;
	}

	public String toString() {
		return edge.toString();
	}

	public DirectedGraphEdge<?, ?> getEdge() {
		return edge;
	}

	public String getUserObject() {
		return (String) super.getUserObject();
	}

	public ProMGraphPort getSource() {
		return (ProMGraphPort) super.getSource();
	}

	public ProMGraphPort getTarget() {
		return (ProMGraphPort) super.getTarget();
	}

	public void setView(JGraphEdgeView view) {
		this.view = view;
	}

	public JGraphEdgeView getView() {
		return view;
	}

	public void cleanUp() {
		if (view != null) {
			view.cleanUp();
		}
		internalPoints.clear();
		model = null;
		view = null;
		edge = null;
	}

	public String getLabel() {
		return edge.getLabel();
	}

	public int hashCode() {
		return edge.hashCode();
	}

	/**
	 * This implementation of equals seems to be required by JGraph. Changing it
	 * to anything more meaningful will introduce very strange results.
	 */
	public boolean equals(Object o) {
		return o == this;
	}

	public ProMGraphModel getModel() {
		return model;
	}

}
