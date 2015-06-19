package rioko.linearalg.exceptions;

public class LinearAlgebraArgumentException extends Exception {

	private static final long serialVersionUID = 5181250617296529893L;
	
	public LinearAlgebraArgumentException(String string) {
		super("Rioko Lin. Algebra ERROR: " + string);
	}

}
