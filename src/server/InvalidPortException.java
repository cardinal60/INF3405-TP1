package server;

public class InvalidPortException extends Exception {
	InvalidPortException(){}
	
	InvalidPortException(String str) {
		super(str);
	}
}
