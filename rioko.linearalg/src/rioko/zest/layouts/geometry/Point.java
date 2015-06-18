package rioko.zest.layouts.geometry;

import rioko.utilities.Pair;

public class Point extends Pair<Double,Double>{
	public Point(Double x, Double y) {
		super(x,y);
	}
	
	public double getX() {
		return this.getFirst();
	}
	
	public double getY() {
		return this.getLast();
	}
	
	public Point add(Point p) {
		return new Point(p.getX() + this.getX(), p.getY() + this.getY());
	}
	
	public Point opposite() {
		return new Point(-this.getX(), -this.getY());
	}
	
	public Point scalar(double factor) {
		return new Point(this.getX()*factor, this.getY()*factor);
	}
	
	@Override
	public String toString() {
		return "(" + this.getX() +", " + this.getY() + ")";
	}
}
