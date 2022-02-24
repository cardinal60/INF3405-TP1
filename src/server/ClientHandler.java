package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;


public class ClientHandler implements Runnable {
	public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
	private Socket socket;
	private int clientNumber;
	private String[] clientInfo = new String[2];
	private LoginManager loginManager;
	private DataOutputStream out;
	private DataInputStream in;
	private HistoryManager historyManager;
	
	public ClientHandler(Socket socket, int clientNumber) {
		this.socket = socket;
		this.clientNumber = clientNumber;
		this.loginManager = new LoginManager();
		this.historyManager = new HistoryManager();
		clientHandlers.add(this);
		System.out.println("New connection with client#" + clientNumber + "at" + socket);
		///this.run();
	}
	
	/* Une thread se charge d'envoyer au client un message de bienvenue */
	public void run() {
		try {
			// Création d'un canal sortant pour envoyer des messages au client
			this.out = new DataOutputStream(socket.getOutputStream());
			this.out.writeUTF("Hello from server - you are client#" + clientNumber + "\nPlease enter your userName: ");
			
			this.in = new DataInputStream(socket.getInputStream());
			String userName = in.readUTF();
			System.out.println(userName);
			
			
			this.out.writeUTF("Please enter your password:\n");
			String password = in.readUTF();
			System.out.println(password);
			
			
			this.clientInfo[0] = userName;
			this.clientInfo[1] = password;
			
			String valitationResult = loginManager.validateUserInfos(this.clientInfo);
			
			if(valitationResult == "success" || valitationResult == "account created") {
				out.writeUTF(valitationResult);
				broadcastMessage(this.clientInfo[0] + " has successfully entered the chat-room !");
				this.loadHistory();
				listenToNewMessages();
			}
			else {
				this.out.writeUTF("Password not recognised for this account, terminatting session... !");
				deleteClientHandler();
			}
			
			//this.loginManager.compareData(clientInfo, socket.getInetAddress(), socket.getPort());

		}
		catch (IOException e){
			System.out.println("Error handling client#" + clientNumber + ":" + e);
			deleteClientHandler();
		}
		finally {
			try {
				deleteClientHandler();
				socket.close();
				System.out.println("Connection with client#" + clientNumber + "closed");
			}
			catch(IOException e) {
				System.out.println("Couldn't close a socket, what's going on?");
			}
		}
	}
	
	private void listenToNewMessages() {
		String newMessage;
		while(this.socket.isConnected()) {
			try {
				newMessage = in.readUTF();
				this.historyManager.insertMessage(newMessage);
				broadcastMessage(newMessage);
			} catch (IOException e) {
				System.out.println(e);
				break;
			}
		}
	}
	
	private void broadcastMessage(String message) {
		for( ClientHandler handler: clientHandlers) {
			try {
				if(handler.clientNumber != this.clientNumber) {
					handler.out.writeUTF(message);
					handler.out.flush();
				}
			} catch (IOException e) {
				deleteClientHandler();
			}
		}
	}
	
	private void loadHistory() {
		String[] history = this.historyManager.loadMessages();
		for( ClientHandler handler: clientHandlers) {
			try {
				if(handler.clientNumber == this.clientNumber) {
					for( int i = 0; i < history.length; i++) {
						if(history[i] != null) {
							handler.out.writeUTF(history[i] + "\n");
						}
						handler.out.flush();
					}
					
				}
			} catch (IOException e) {
				deleteClientHandler();
			}
		}
	}
	
	private void deleteClientHandler() {
		broadcastMessage(this.clientInfo[0] + " has left the chat");
		clientHandlers.remove(this);
		try {
			if(this.in != null) in.close();
			if( this.out != null) out.close();
			if (this.socket != null) this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}