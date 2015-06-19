package rioko.linearalg;

import rioko.linearalg.exceptions.BadClassArgumentException;
import rioko.linearalg.exceptions.ZeroNumberException;
import rioko.utilities.Copiable;

public class RComplex implements RNumber {

	private RDouble real, imag;
	
	//Builders
	public RComplex(RDouble real, RDouble imag) {
		this.real = real;
		this.imag = imag;
	}
	
	public RComplex(Double real, Double imag) {
		this(new RDouble(real), new RDouble(imag));
	}
	
	public RComplex(Double real, RDouble imag) {
		this(new RDouble(real), imag);
	}
	
	public RComplex(RDouble real, Double imag) {
		this(real, new RDouble(imag));
	}
	
	//Getters
	public RDouble getReal() {
		return this.real;
	}
	
	public RDouble getImag() {
		return this.imag;
	}

	//RNumber methods
	@Override
	public RComplex add(RNumber toAdd) throws BadClassArgumentException {
		if(toAdd instanceof RDouble) {
			return new RComplex(this.real.add(toAdd), this.imag);
		} else if(toAdd instanceof RComplex) {
			return new RComplex(this.real.add(((RComplex) toAdd).getReal()), this.imag.add(((RComplex) toAdd).getImag()));
		} else {
			throw new BadClassArgumentException(toAdd.getClass(), RComplex.class);
		}
	}

	@Override
	public RComplex minus(RNumber toRemove) throws BadClassArgumentException {
		if(toRemove instanceof RDouble) {
			return new RComplex(this.real.minus(toRemove), this.imag);
		} else if(toRemove instanceof RComplex) {
			return new RComplex(this.real.minus(((RComplex) toRemove).getReal()), this.imag.minus(((RComplex) toRemove).getImag()));
		} else {
			throw new BadClassArgumentException(toRemove.getClass(), RComplex.class);
		}
	}

	@Override
	public RComplex opposite() {
		return new RComplex(this.real.opposite(), this.imag.opposite());
	}

	@Override
	public RComplex prod(RNumber toMult) throws BadClassArgumentException {
		if(toMult instanceof RDouble) {
			return new RComplex(this.real.prod(toMult), this.imag.prod(toMult));
		} else if(toMult instanceof RComplex) {
			RComplex mult = (RComplex)toMult;
			
			RDouble fReal = this.real.prod(mult.real).add(this.imag.prod(mult.imag).opposite());
			RDouble fImag = this.real.prod(mult.imag).add(this.imag.prod(mult.real));
			
			return new RComplex(fReal, fImag);
		} else {
			throw new BadClassArgumentException(toMult.getClass(), RComplex.class);
		}
	}

	@Override
	public RNumber inverse() throws ZeroNumberException {
		if(this.isZero()) {
			throw new ZeroNumberException("Imposible divide by zero");
		} else if(this.real.isZero()) {
			try {
				return new RComplex(RDouble.zero(),RDouble.unit().opposite().prod(this.imag.inverse()));
			} catch (BadClassArgumentException e) {
				//Impossible Exception
				e.printStackTrace();
			}
		} else {
			try {
				RDouble imag = this.imag.opposite().prod(this.real.square().add(this.imag).inverse());
			
				RDouble real = RDouble.unit().add(imag.prod(this.imag).prod(this.real.inverse()));
				
				return new RComplex(real,imag);
			} catch (BadClassArgumentException e) {
				// Impossible exception
				e.printStackTrace();
			}
		}

		return null;
	}

	@Override
	public double norm() {
		return Math.sqrt(this.real.square().getValue() + this.imag.square().getValue());
	}
	
	@Override
	public boolean isZero() {
		return this.equals(RComplex.zero());
	}

	@Override
	public RComplex getZero() {
		return RComplex.zero();
	}

	@Override
	public RComplex getUnit() {
		return RComplex.unit();
	}
	
	//Other methods
	@Override
	public String toString() {
		return this.real.toString() + " + " + this.imag.toString() + "i";
	}
	
	//Equality methods
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof RComplex) {
			return this.real.equals(((RComplex) obj).getReal()) && this.imag.equals(((RComplex) obj).getImag());
		}
		
		return false;
	}

	//Copiable methods
	@Override
	public Copiable copy() {
		return new RComplex(this.real, this.imag);
	}
	
	@Override
	public int hashCode() {
		return this.real.hashCode() + this.imag.hashCode();
	}
	
	//Static methods
	public static RComplex zero() {
		return new RComplex(0.0,0.0);
	}
	
	public static RComplex unit() {
		return new RComplex(1.0,0.0);
	}
	
	public static RComplex i() {
		return new RComplex(0.0,1.0);
	}

}
