package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class client {
	private static Scanner inputScanner = new Scanner(System.in);
	public static Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	String[] loginInfo = new String[4];;
	private LoginManager loginManager = new LoginManager();
	
	
	
	private String[] initialValidation() {
		try {
			PrintWriter out = new PrintWriter(System.out);
			Scanner in = new Scanner(System.in);
		
			System.out.println("Please Enter the servers ip address :)");
			this.loginInfo[0] = in.nextLine();
			System.out.println(loginInfo[0]);
			
			this.loginManager.validateIP(loginInfo[0]);
			System.out.println("Now, Please Enter the servers Port");
			
			this.loginInfo[1] = in.nextLine();
			System.out.println(loginInfo[1]);
			this.loginManager.validatePort(loginInfo[1]);
		
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
		String username = inputScanner.nextLine();
		
		this.out = new DataOutputStream(socket.getOutputStream());
		this.out.writeUTF(username);
		
		System.out.print(this.in.readUTF());
		String password = inputScanner.nextLine();
		this.out.writeUTF(password);
		
		String helloMessageFromServer = this.in.readUTF();
		System.out.println(helloMessageFromServer);
		
		String loginResult = this.in.readUTF();
		System.out.println(loginResult);
		
		enterRoom();
		
	}
	
	private void sendMessage() {
		try {
			String message;
			while(socket.isConnected()) {
				message = inputScanner.nextLine();
				out.writeUTF(message);
				out.flush();
			}
		} catch (IOException e) {
			endConnection();
		}
		
	}
	private void enterRoom() throws Exception{
		
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
		client client = new client();
		
		String[] validationInfo = client.initialValidation();
		
		String serverAddress = validationInfo[0];
		int port = Integer.parseInt(validationInfo[1]);
		socket = new Socket(serverAddress, port);
		
		System.out.format("The server is running on %s:%d%n", serverAddress, port);
		
		client.logIn();
		
	}
}
