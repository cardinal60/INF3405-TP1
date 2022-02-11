package server;

public class InvalidIpException extends Exception {
	
	InvalidIpException(){}
	
	InvalidIpException(String str) {
		super(str);
	}

}
