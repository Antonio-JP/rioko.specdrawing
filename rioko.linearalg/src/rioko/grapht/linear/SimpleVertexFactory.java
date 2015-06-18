package rioko.grapht.linear;

import rioko.grapht.VertexFactory;

public class SimpleVertexFactory implements VertexFactory<SimpleVertex> {

	@Override
	public SimpleVertex createVertex(Object... arg0) {
		if(arg0.length == 0) {
			return new SimpleVertex();
		} else if(arg0[0] instanceof Integer) {
			return new SimpleVertex((Integer)arg0[0]);
		}
		
		return null;
	}

}
