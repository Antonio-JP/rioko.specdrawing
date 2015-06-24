import rioko.grapht.linear.UndirectedGraphCreator;
import rioko.grapht.linear.UndirectedGraph;

public class AuxiliaryGraphCreator_0 implements UndirectedGraphCreator {
	public UndirectedGraph create() {
		return cycle(10);
	}
}
