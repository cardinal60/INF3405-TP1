package client;

public class InvalidPortException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	InvalidPortException(){}
	
	InvalidPortException(String str) {
		super(str);
	}
}
