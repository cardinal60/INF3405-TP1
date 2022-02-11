package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	
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
		
		System.out.println("Now, Please Enter your Username");
		
		loginInfo[2] = in.nextLine();
		System.out.println(loginInfo[2]);
		
		System.out.println("Finaly, Please Enter your Password");
		
		loginInfo[3] = in.nextLine();
		System.out.println(loginInfo[3]);
	
		return loginInfo;
			
		} catch(Exception error) {
			
			System.out.println(error.toString());
		}
		return null;
		
	}
	
	public void sendLoginInfo(Socket socket, String[] loginInfo) throws IOException {
		PrintWriter out = new PrintWriter(socket.getOutputStream());
		
		for (int i = 0; i < loginInfo.length; i++) {
			out.println(loginInfo[i]);
			
		}
		
		out.close();
	}
	
	/* Application Client */
	public static void main(String[] args) throws Exception {
		Client client = new Client();
		System.out.println("cancre");
		String[] loginInfo = client.initialValidation();
		System.out.println("cancre2");
		// Addresse et port du serveur
		String serverAddress = "127.0.0.3";
		int port = 5000;
		System.out.print("HEllo WOrld");
		
		// Création d'une nouvelle connexion avec le serveur
		socket = new Socket(serverAddress, port);
		
		
		
		System.out.format("The server is running on %s:%d%n", serverAddress, port);
		System.out.print("HEllo WOrld");
		
		client.sendLoginInfo(socket, loginInfo);
		
		DataInputStream in = new DataInputStream(socket.getInputStream());
		
		// Attente de la réception d'un message envoyé par le serveur sur le canal
		String helloMessageFromServer = in.readUTF();
		System.out.println(helloMessageFromServer);
		
		// Fermeture de la connexion avec le serveur
		socket.close();
	}
}
