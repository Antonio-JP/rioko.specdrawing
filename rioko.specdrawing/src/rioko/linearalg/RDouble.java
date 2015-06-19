package rioko.linearalg;

import rioko.linearalg.exceptions.BadClassArgumentException;
import rioko.linearalg.exceptions.ZeroNumberException;
import rioko.utilities.Copiable;

public class RDouble implements RNumber {
	
	private static final double epsilon = 1.0/1000000000000.0;
	
	private Double value;
	
	public RDouble(Double value) {
		this.value = value;
	}
	
	public Double getValue() {
		return this.value;
	}
	
	@Override
	public RDouble add(RNumber toAdd) throws BadClassArgumentException {
		if(toAdd instanceof RDouble) {
			return new RDouble(this.value + ((RDouble)toAdd).value);
		} else {
			throw new BadClassArgumentException(toAdd.getClass(), RDouble.class);
		}
	}

	@Override
	public RDouble minus(RNumber toRemove) throws BadClassArgumentException {
		if(toRemove instanceof RDouble) {
			return new RDouble(this.value - ((RDouble)toRemove).value);
		} else {
			throw new BadClassArgumentException(toRemove.getClass(), RDouble.class);
		}
	}

	@Override
	public RDouble opposite() {
		return new RDouble(-this.value);
	}

	@Override
	public RDouble prod(RNumber toMult) throws BadClassArgumentException {
		if(toMult instanceof RDouble) {
			return new RDouble(this.value * ((RDouble)toMult).value);
		} else {
			throw new BadClassArgumentException(toMult.getClass(), RDouble.class);
		}
	}

	@Override
	public RDouble inverse() throws ZeroNumberException {
		if(this.equals(RDouble.zero())) {
			throw new ZeroNumberException("Impossible divide by zero");
		}
		return new RDouble(1/this.value);
	}

	@Override
	public double norm() {
		return this.value.doubleValue();
	}

	@Override
	public boolean isZero() {
		return this.equals(RDouble.zero());
	}

	@Override
	public RDouble getZero() {
		return RDouble.zero();
	}

	@Override
	public RDouble getUnit() {
		return RDouble.unit();
	}
	
	//Equality methods
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof RDouble) {
			return (Math.abs(this.value - ((RDouble) obj).getValue())) < epsilon;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

	//Copiable methods
	@Override
	public Copiable copy() {
		return new RDouble(this.value);
	}
	
	//Other methods
	public RDouble square() {
		try {
			return this.prod(this);
		} catch (BadClassArgumentException e) {
			// Impossible Exception
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		return this.value.toString();
	}

	//Static methods
	public static RDouble zero() {
		return new RDouble(0.0);
	}
	
	public static RDouble unit() {
		return new RDouble(1.0);
	}
}
