package rioko.zest.layouts;

import rioko.drawalgorithms.KorenDrawAlgorithm;
import rioko.grapht.linear.UndirectedGraph;

public class KorenLayoutAlgorithm extends DrawingLayoutAlgorithm{

	public KorenLayoutAlgorithm(int styles, UndirectedGraph graph) {
		super(styles, graph);

		this.algorithm = new KorenDrawAlgorithm();
	}

}
