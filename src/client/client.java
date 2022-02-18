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
	private LoginManager loginManager = new LoginManager();
	
	
	
	private String[] initialValidation() {
		try {
		// Création d'un canal sortant pour envoyer des messages au client
			System.out.println("cancre3\\n");
			PrintWriter out = new PrintWriter(System.out);
			Scanner in = new Scanner(System.in);
			String[] loginInfo = new String[4]; 
			// Envoi d'un message au client
			System.out.println("cancre4\\n");
		
			System.out.println("Please Enter the servers ip address :)");
			loginInfo[0] = in.nextLine();
			System.out.println(loginInfo[0]);
			
			this.loginManager.validateIP(loginInfo[0]);
			System.out.println("cancre5");
			System.out.println("Now, Please Enter the servers Port");
			
			loginInfo[1] = in.nextLine();
			System.out.println(loginInfo[1]);
			this.loginManager.validatePort(loginInfo[1]);
		
			return loginInfo;
				
		} catch(Exception error) {
			
			System.out.println(error.toString());
		}
		return null;
		
	}
	/*
	public void sendLoginInfo(Socket socket, String[] loginInfo) throws IOException {
		PrintWriter out = new PrintWriter(socket.getOutputStream());
		
		for (int i = 0; i < loginInfo.length; i++) {
			out.println(loginInfo[i]);
			
		}
	}
	*/
	/* Application Client */
	
	private static void logIn() throws Exception{
		
		DataInputStream in = new DataInputStream(socket.getInputStream());
		System.out.println(in.readUTF());
		String username = inputScanner.nextLine();
		
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		out.writeUTF(username);
		
		System.out.print(in.readUTF());
		String password = inputScanner.nextLine();
		out.writeUTF(password);
		
		String helloMessageFromServer = in.readUTF();
		System.out.println(helloMessageFromServer);
		
	}
	
	public static void main(String[] args) throws Exception {
		client client = new client();
		
		String[] loginInfo = client.initialValidation();
		
		// Addresse et port du serveur
		String serverAddress = loginInfo[0];
		int port = Integer.parseInt(loginInfo[1]);
		
		// Création d'une nouvelle connexion avec le serveur
		socket = new Socket(serverAddress, port);
		
		System.out.format("The server is running on %s:%d%n", serverAddress, port);
		
		//client.sendLoginInfo(socket, loginInfo);
		client.logIn();
		
		//socket.close();
		
		while(true) {
			
		}
	}
}
