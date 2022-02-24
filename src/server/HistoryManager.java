package server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class HistoryManager {
	private String[] historyList;
	
	
	HistoryManager(){
		this.historyList = new String[15];
	}
	
	public void insertMessage(String message) {
		File history = new File("src/history.txt");
		try {
			Scanner in = new Scanner(history);
			String line;
			int i = 0;
			while(in.hasNextLine()) {
				line = in.nextLine();
				this.historyList[i] = line;
				i++;
			}
			in.close();
			
			
			if(this.historyList[14] != null) {
				for(int k = 1; k < this.historyList.length; k++){
				      this.historyList[k - 1] = this.historyList[k];
				   }
				this.historyList[14] = null;
				}
			this.writeMessage(message);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
		
	public void writeMessage(String message) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("src/history.txt"));
			for(int i = 0; i< this.historyList.length; i++) {
				if( historyList[i] != null) {
					writer.write(this.historyList[i]);
					writer.newLine();
				}
			}
			writer.close();
			
			BufferedWriter messageWriter = new BufferedWriter(new FileWriter("src/history.txt", true));
			messageWriter.write(message);
			messageWriter.newLine();
			messageWriter.close();
			

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public String[] loadMessages() {
		File history = new File("src/history.txt");
		try {
			Scanner in = new Scanner(history);
			String line;
			int i = 0;
			while(in.hasNextLine()) {
				line = in.nextLine();
				if(!line.isBlank()) {
					this.historyList[i] = line;
				}
				i++;
			}
			in.close();
		
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		    }
		return this.historyList;
	}	
		
}
