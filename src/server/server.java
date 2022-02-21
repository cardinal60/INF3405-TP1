package server;

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
			
			while(true) {
				
				new ClientHandler(listener.accept(), ++clientNumber).start();
			}
		}
		finally {
			// Fermeture de la connexion
			listener.close();
		}
	}
	
	
	/* Une thread qui se charge de traiter la demainde de chaque client sur un socket particulier */
	private static class ClientHandler extends Thread {
		private Socket socket;
		private int clientNumber;
		private LoginManager loginManager;
		
		public ClientHandler(Socket socket, int clientNumber) {
			this.socket = socket;
			this.clientNumber = clientNumber;
			this.loginManager = new LoginManager();
			System.out.println("New connection with client#" + clientNumber + "at" + socket);
			
			//this.loginManager.validateUserInfos(socket.getInputStream());
			
			this.run();
		}
		
		/* Une thread se charge d'envoyer au client un message de bienvenue */
		public void run() {
			try {
				// Création d'un canal sortant pour envoyer des messages au client
				System.out.print("cancer");
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				out.writeUTF("Hello from server - you are client#" + clientNumber + "\nPlase enter your userName: ");
				
				DataInputStream in = new DataInputStream(socket.getInputStream());
				String userName = in.readUTF();
				System.out.println(userName);
				
				
				out.writeUTF("Please enter your password:");
				String password = in.readUTF();
				System.out.println(password);
				
				String[] clientInfo = new String[2];
				clientInfo[0] = userName;
				clientInfo[1] = password;
				
				String valitationResult = loginManager.validateUserInfos(clientInfo);
				
				if(valitationResult == "success" || valitationResult == "account created") {
					out.writeUTF("Successfully connected to chat-room !");
				}
				else {
					out.writeUTF("Wrong password !");
				}
				
				//this.loginManager.compareData(clientInfo, socket.getInetAddress(), socket.getPort());
				System.out.print("cancer");

				
			}
			catch (IOException e){
				
				System.out.println("Error handling client#" + clientNumber + ":" + e);
			}
			finally {
				try {
					socket.close();
				}
				catch(IOException e) {
					System.out.println("Couldn't close a socket, what's going on?");
				}
				System.out.println("Connection with client#" + clientNumber + "closed");
			}
		}
		
	}
}

		
		