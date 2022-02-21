package server;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class server {
	private LoginManager loginManager = new LoginManager();
	public static ServerSocket listener;
	private static Scanner userInput = new Scanner(System.in);
	
	private String[] initialValidation() {
		try {
		// Création d'un canal sortant pour envoyer des messages au client

		String[] serverInfo = new String [2];
		Scanner input = new Scanner(System.in);
		
		System.out.println("Please Enter the servers ip address");
		String serverIP = input.nextLine();
		
		serverInfo[0] = serverIP;
		
		this.loginManager.validateIP(serverIP);
		
		System.out.println("Now, Please Enter the servers Port");
		
		String serverPort = input.nextLine();
		
		serverInfo[1] = serverPort;
		
		this.loginManager.validatePort(serverPort);
		
		return serverInfo;
			
		} catch(Exception error) {
			
			System.out.println(error.toString());
		}
		return null;
		
		
	}

	
	public static void main(String[] args) throws Exception {
		// Compteur incrémenté à chaque connexion d'un client au serveur 
		server server = new server();
		int clientNumber = 0;
		String[] serverInfo = server.initialValidation();
		listener = new ServerSocket();
		listener.setReuseAddress(true);
		listener.bind(new InetSocketAddress(InetAddress.getByName(serverInfo[0]), Integer.parseInt(serverInfo[1])));		
		System.out.format("The server is running on %s:%d%n", serverInfo[0], Integer.parseInt(serverInfo[1]));
		
		try {
			/* À chaque fois qu'un client se connecte, on exécute la fonction Run() de l'objet ClientHandler */ 
			while(!listener.isClosed()) {
				ClientHandler clientHandler = new ClientHandler(listener.accept(), ++clientNumber);
				Thread thread = new Thread(clientHandler);
				thread.start();
			}
		}
		finally {
			// Fermeture de la connexion
			listener.close();
		}
	}
	
}



		
		