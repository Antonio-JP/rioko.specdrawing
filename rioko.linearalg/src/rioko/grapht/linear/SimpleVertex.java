package rioko.grapht.linear;

import rioko.grapht.Vertex;

public class SimpleVertex implements Vertex {
	
	private Integer id = null;

	//Builders
	public SimpleVertex() { 
		this.id = -1;
	}

	public SimpleVertex(Integer id) {
		this.id = id;
	}
	
	//Getters
	public Integer getId() {
		return this.id;
	}
	
	//Vertex methods
	@Override
	public SimpleVertex copy() {
		return new SimpleVertex(this.id);
	}

	@Override
	public SimpleVertexFactory getVertexFactory() {
		return new SimpleVertexFactory();
	}
	
	//Equality methods
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SimpleVertex) {
			return this.id.equals(((SimpleVertex) obj).getId());
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

}
