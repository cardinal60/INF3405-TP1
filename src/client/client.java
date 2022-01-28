package client;

import java.io.DataInputStream;
import java.net.Socket;

public class Client {
	
	public static Socket socket;
	
	/* Application Client */
	public static void main(String[] args) throws Exception {
		
		// Addresse et port du serveur
		String serverAddress = "127.0.0.3";
		int port = 5000;
		
		// Cr�ation d'une nouvelle connexion avec le serveur
		socket = new Socket(serverAddress, port);
		
		System.out.format("The server is running on %s:%d%n", serverAddress, port);
		
		// Cr�ation d'un canal entrant pour recevoir les messages envoy�s par le serveur
		DataInputStream in = new DataInputStream(socket.getInputStream());
		
		// Attente de la r�ception d'un message envoy� par le serveur sur le canal
		String helloMessageFromServer = in.readUTF();
		System.out.println(helloMessageFromServer);
		
		// Fermeture de la connexion avec le serveur
		socket.close();
	}
}
