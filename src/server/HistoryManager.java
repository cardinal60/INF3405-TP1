package server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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
			
			
			if( this.historyList.length >= 15) {
				String[] newArray = new String[15];
				for(int j = 1; j < this.historyList.length; i++) {
					newArray[i - 1] = this.historyList[i];
				}
				
				this.historyList = Arrays.copyOf(newArray, 15);
			}
			
			this.writeMessage(message);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
		
	private void writeMessage(String message) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("src/history.txt", true));
			for(int i = 0; i< this.historyList.length; i++) {
				writer.write(this.historyList[i]  + "\n");
				writer.newLine();
			}
			writer.write(message);
			writer.newLine();
			writer.close();

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
