package rioko.zest.layouts.geometry;

import java.io.Serializable;

public class DoubleRectangle implements Serializable {

	private static final long serialVersionUID = -8865696229457055434L;
	
	private double x,y,width,height;
	
	public DoubleRectangle()
	{
		this.x = 0;
		this.y = 0;
		this.width = 0;
		this.height = 0;
	}
	
	public DoubleRectangle(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	//Getters y Setters
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public void setHeight(double height) {
		this.height = height;
	}

}
