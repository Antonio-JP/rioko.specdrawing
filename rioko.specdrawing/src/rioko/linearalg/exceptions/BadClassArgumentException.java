package rioko.linearalg.exceptions;


public class BadClassArgumentException extends LinearAlgebraArgumentException {

	private static final long serialVersionUID = -2457338379560513127L;
	
	private Class<?> badClass;
	private Class<?> desiredClass;

	public BadClassArgumentException(String string, Class<?> badClass, Class<?> desiredClass) {
		super(string);

		this.badClass = badClass;
		this.desiredClass = desiredClass;
	}

	public BadClassArgumentException(Class<?> badClass, Class<?> desiredClass) {
		this("bad class in the argument of the method", badClass, desiredClass);
	}

	@Override
	public String getMessage() {
		return super.getMessage() + " -- (" + this.badClass.getSimpleName() + " != " + this.desiredClass.getSimpleName() + ")";
	}
}
