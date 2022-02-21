package client;
import java.util.Scanner;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;

public class LoginManager {
	
	
	public void validateIP(String ip) throws InvalidIpException {

		int expectedLenght = 4;
		int[] response = new int[expectedLenght];
		String[] ipParts = ip.split("\\.");
		boolean isIncorrectLenght = ipParts.length != expectedLenght;
		
		if (isIncorrectLenght) {
			throw new InvalidIpException("given Ip address does not have correct amount of bytes");
		}
		
		for(int i = 0; i< expectedLenght; i++) {
			response[i] = Integer.parseInt(ipParts[i]);
			boolean invalidInteger = response[i] > 255 || response[i] < 0;
			
			if(invalidInteger) {
				throw new InvalidIpException("given Ip address has a number which isn't a byte");
			}
		}
		
		
	}
	
	
	
	public void validatePort(String port) throws InvalidPortException {
		int givenPort = Integer.parseInt(port);
		boolean invalidPort = givenPort < 5000 || givenPort > 5050;
		
		if(invalidPort) {
			throw new InvalidPortException("given Port is not recognised please check the port number");
		}
	}
}
	
