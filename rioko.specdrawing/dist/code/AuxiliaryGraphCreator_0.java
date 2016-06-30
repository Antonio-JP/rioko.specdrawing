import rioko.grapht.linear.UndirectedGraphCreator;
import rioko.grapht.linear.UndirectedGraph;

public class AuxiliaryGraphCreator_0 implements UndirectedGraphCreator {
	public UndirectedGraph create() {
		return join(cycle(3), cycle(3), 0,0);
	}
}
