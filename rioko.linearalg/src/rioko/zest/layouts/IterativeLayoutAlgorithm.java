package rioko.zest.layouts;

import rioko.drawalgorithms.IterateSecondValueAlgorithm;
import rioko.grapht.linear.UndirectedGraph;

public class IterativeLayoutAlgorithm extends DrawingLayoutAlgorithm {

	public IterativeLayoutAlgorithm(int styles, UndirectedGraph graph) {
		super(styles, graph);

		this.algorithm = new IterateSecondValueAlgorithm();
	}

}
