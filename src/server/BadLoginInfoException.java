package server;

public class BadLoginInfoException extends Exception {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	BadLoginInfoException(){}
	
	BadLoginInfoException(String str) {
		super(str);
}
	
}
