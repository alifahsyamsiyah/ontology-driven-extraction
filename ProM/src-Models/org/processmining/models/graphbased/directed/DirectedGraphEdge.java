package org.processmining.models.graphbased.directed;

public interface DirectedGraphEdge<S extends DirectedGraphNode, T extends DirectedGraphNode> extends
		DirectedGraphElement {

	S getSource();

	T getTarget();

}
