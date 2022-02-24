package client;


import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;  
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	private Scanner inputScanner;
	public static Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	String[] loginInfo = new String[4];
	private LoginManager loginManager = new LoginManager();
	
	
	Client() {
		this.inputScanner = new Scanner(System.in);
	}
	
	
	
	private String[] initialValidation() {
		try {
		
			System.out.println("Please Enter the servers ip address :)");
			this.loginInfo[0] = this.inputScanner.nextLine();
			
			this.loginManager.validateIP(loginInfo[0]);
			System.out.println("Now, Please Enter the servers Port");
			
			this.loginInfo[1] = this.inputScanner.nextLine();
			this.loginManager.validatePort(loginInfo[1]);
			// in.close();
		
			return loginInfo;
				
		} catch(Exception error) {
			
			System.out.println(error.toString());
		}
		return null;
		
	}
	
	/* Application Client */
	
	private void logIn() throws Exception{

		this.in = new DataInputStream(socket.getInputStream());
		System.out.println(this.in.readUTF());
		this.loginInfo[2] = inputScanner.nextLine();
		
		this.out = new DataOutputStream(socket.getOutputStream());
		this.out.writeUTF(this.loginInfo[2]);
		
		System.out.print(this.in.readUTF());
		String password = inputScanner.nextLine();
		this.out.writeUTF(password);
		
		String helloMessageFromServer = this.in.readUTF();
		System.out.println(helloMessageFromServer);
		
		String loginResult = this.in.readUTF();
		System.out.println(loginResult);
		
		String line;
		while(this.in.available() > 0) {
			line = this.in.readUTF();
			System.out.println(line);
		}
		
		enterRoom();
		
	}
	
	private String addHeader() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd @ HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String loginHeader =  "[" + this.loginInfo[2] + " - " + loginInfo[0] + ":" + String.valueOf(socket.getLocalPort()) + " - ";
		return loginHeader + dtf.format(now) + "]: ";
	}
	
	private void sendMessage() {
		try {
			String message;
			while(socket.isConnected()) {
				message = inputScanner.nextLine();
				int maxLenght = 200;
				if (message.length() > maxLenght) {
					System.out.println("Your message is too long, please try again with a shorter message");
					this.sendMessage();
				}
				message = this.addHeader() + message;
				System.out.println("\r" + message);
				out.writeUTF(message);
				out.flush();
			}
		} catch (IOException e) {
			endConnection();
		}
		
	}
	private void enterRoom() throws Exception{
		this.in = new DataInputStream(socket.getInputStream());
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(socket.isConnected()) {
					try {
						String message = in.readUTF();
						System.out.println(message);
					} catch (IOException e) {
						endConnection();
					}
				}
				
			}
		}).start();
		
		sendMessage();
		
	}

	public void endConnection() {
		try {
			if(this.in != null) this.in.close();
			if( this.out != null) this.out.close();
			if (socket != null) socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws Exception {
		Client client = new Client();
		
		String[] validationInfo = client.initialValidation();
		
		String serverAddress = validationInfo[0];
		int port = Integer.parseInt(validationInfo[1]);
		socket = new Socket(serverAddress, port);
		
		System.out.format("The server is running on %s:%d%n", serverAddress, port);
		
		client.logIn();
		
		client.inputScanner.close();
		
	}
}

