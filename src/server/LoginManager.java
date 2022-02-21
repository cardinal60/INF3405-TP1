package server;
import java.util.Scanner;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;

public class LoginManager {
	public ServerSocket socket;
	
	LoginManager(){}
	
	
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
		boolean invalidPort = givenPort <= 5000 || givenPort >= 5050;
		
		if(invalidPort) {
			throw new InvalidPortException("given Port is not recognised please check the port number");
		}
	}
	
	public void compareData(String[] clientInfo, InetAddress ip, int port) throws BadLoginInfoException {
		
		boolean invalidIp = clientInfo[0] != ip.getHostAddress();
		if (invalidIp) {
			throw new BadLoginInfoException("the given IP adress is not the servers Ip adress");
		}
		
		boolean invalidPort = Integer.parseInt(clientInfo[1]) != port;
		if (invalidPort) {
			throw new BadLoginInfoException("the given port number is not the servers Port number");
		}
		
		boolean invalidUsername = clientInfo[2] != "walrus";
		if (invalidUsername) {
			throw new BadLoginInfoException("the given username is not recognised by the server");
		}
		
		boolean invalidPassword = clientInfo[3] != "admin";
		if (invalidPassword) {
			throw new BadLoginInfoException("the password does not match the username");
		}
	}
	
	public String validateUserInfos(String[] clientInfo) {
			
			try  {
				
				String userName = clientInfo[0];
				String password = clientInfo[1];
				
				System.out.println("Verifying user: " + userName);
				System.out.println("password given is: " + password);
				
				
				String line;
				Boolean userFound = false;
				/*File file = new File("src");
				for(String fileNames : file.list()) System.out.println(fileNames);
				*/
				File myObj = new File("src/accounts.txt");
			      Scanner myReader = new Scanner(myObj);
				
				while ( myReader.hasNextLine())   {
					line = myReader.nextLine();
					String[] s = line.split(":");
				  System.out.println (line);
				  
				  if(s[0].equals(userName)) {
					  if (s[1].equals(password)) {
						  return "success";
					  }
					  return "wrong password";
					}
				  
				}
				if(!userFound) {
					BufferedWriter writer = new BufferedWriter(new FileWriter("src/accounts.txt"));
				    writer.write(userName + ":" + password);
				    writer.close();
				    return "account created";
				}
				myReader.close();
				
				return "success";
			} catch (IOException e){
				System.out.println(e);
			}
			finally {
				
			}

			return null;
		}

}
