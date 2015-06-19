package rioko.linearalg;

import rioko.linearalg.exceptions.BadClassArgumentException;
import rioko.linearalg.exceptions.ZeroNumberException;
import rioko.utilities.Copiable;

public interface RNumber extends Copiable {
	
	public RNumber add(RNumber toAdd) throws BadClassArgumentException;
	public RNumber minus(RNumber toRemove) throws BadClassArgumentException;
	public RNumber opposite();
	public RNumber prod(RNumber toMult) throws BadClassArgumentException;
	public RNumber inverse() throws ZeroNumberException;
	public double norm();
	public boolean isZero();
	
	public RNumber getZero();
	public RNumber getUnit();
}
