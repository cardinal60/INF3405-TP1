package client;

public class InvalidIpException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	InvalidIpException(){}
	
	InvalidIpException(String str) {
		super(str);
	}

}
