package server;

import java.util.Scanner;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	public static ServerSocket listener;
	
	
	
	public static void main(String[] args) throws Exception {
		// Compteur incrémenté à chaque connexion d'un client au serveur 
		int clientNumber = 0;
		
		// Addresse er port du serveur
		String serverAddress = "127.0.0.3";
		int serverPort = 5000;
		
		// Création de la connexion pour communiquer avec les clients
		listener = new ServerSocket();
		listener.setReuseAddress(true);
		InetAddress serverIP = InetAddress.getByName(serverAddress);
		
		// Association de l'adresse et du port à la connexion
		listener.bind(new InetSocketAddress(serverIP, serverPort));
		
		System.out.format("The server is running on %s:%d%n", serverAddress, serverPort);
		
		try {
			/* À chaque fois qu'un client se connecte, on exécute la fonction Run() de l'objet ClientHandler */ 
			
			while(true) {
				// Important : la fonction accept() est bloquante : attend qu'un prochain client se connecte 
				// Une nouvelle connection : on incrémente le compteur clientNumber
				new ClientHandler(listener.accept(), clientNumber++).start();
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
		}
		
		/* Une thread se charge d'envoyer au client un message de bienvenue */
		public void run() {
			try {
				// Création d'un canal sortant pour envoyer des messages au client
				System.out.print("cancer");
				var out = new PrintWriter(socket.getOutputStream());
				var in = new Scanner(socket.getInputStream());
				out.println("Hello from server - you are client#" + clientNumber);
				initialValidation();
				
				String[] clientInfo = new String[4];
				
				int i = 0;
				while(in.hasNextLine()) {
					clientInfo[i] = in.nextLine();
					i++;
				}
				
				this.loginManager.compareData(clientInfo, socket.getInetAddress(), socket.getPort());
				System.out.print("cancer");
				
				
				
				
				
			}
			catch (IOException | BadLoginInfoException e){
				
				System.out.println("Error handling client#" + clientNumber + ":" + e);
			}
			finally {
				try {
					// Fermeture de la connexion avec le client
					socket.close();
				}
				catch(IOException e) {
					System.out.println("Couldn't close a socket, what's going on?");
				}
				System.out.println("Connection with client#" + clientNumber + "closed");
			}
		}
		
		private void initialValidation() {
			try {
			// Création d'un canal sortant pour envoyer des messages au client
			Scanner input = new Scanner(socket.getInputStream());
			PrintWriter output = new PrintWriter(socket.getOutputStream());
			
			
	
			output.println("Please Enter the servers ip address");
			String serverIP = input.nextLine();
			
			this.loginManager.validateIP(serverIP);
			
			output.println("Now, Please Enter the servers Port");
			
			String serverPort = input.nextLine();
			
			this.loginManager.validatePort(serverPort);
				
			} catch(Exception error) {
				
				System.out.println(error.toString());
			}
			
			
			
			
		}
	}
}

		
		