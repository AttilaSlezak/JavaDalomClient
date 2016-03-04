package main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.ID3Tag;
import common.Property;
import common.DirectoryScanner;

public class SearchClient {
	
	private String ip;
	private int port;
	private Map<File, ID3Tag> filesAndTagsFromUser = new HashMap<>();
	private String keywordFromUser;
	private List<Property> propertiesFromUser;
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	public SearchClient(String ip, int port, Map<File, ID3Tag> filesAndTagsFromUser, String keywordFromUser, 
			List<Property> propertiesFromUser) {
		this.port = port;
		this.filesAndTagsFromUser = filesAndTagsFromUser;
		this.keywordFromUser = keywordFromUser;
		this.propertiesFromUser = propertiesFromUser;
	}
	
	public SearchClient(String ip, int port, List<File> mp3Files, String keywordFromUser, 
			List<Property> propertiesFromUser) {
		this.port = port;
		this.keywordFromUser = keywordFromUser;
		this.propertiesFromUser = propertiesFromUser;
		for (File oneFile : mp3Files) {
			this.filesAndTagsFromUser.put(oneFile, ID3Tag.parse(oneFile));
		}
	}
		
	public SearchClient(String ip, int port, String path, String keywordFromUser, List<Property> propertiesFromUser) {
		this.port = port;
		this.keywordFromUser = keywordFromUser;
		this.propertiesFromUser = propertiesFromUser;
		File directory = new File(path);
		DirectoryScanner fileScanner = new DirectoryScanner();
		List<File> mp3Files = fileScanner.collect(directory);
		for (File oneFile : mp3Files) {
			this.filesAndTagsFromUser.put(oneFile, ID3Tag.parse(oneFile));
		}
	}
	
	public List<File> communicateWithServer() {
		List<File> resultOfSearch = new ArrayList<>();
		try {
			startConnection();

			oos.writeObject(0);
			oos.writeObject(filesAndTagsFromUser);
			oos.writeObject(0);
			oos.writeObject(keywordFromUser);
			oos.writeObject(0);
			oos.writeObject(propertiesFromUser);
			Object resultFromServer = ois.readObject();
			
			if (resultFromServer instanceof String) {
				String strException = (String) resultFromServer;
				IOException exception = new IOException(strException);
			} else if (resultFromServer instanceof List<?>) {
				resultOfSearch = (List<File>) resultFromServer;
			}
			
			endConnection();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return resultOfSearch;
	}
	
	public void startConnection() {
		try {
			socket = new Socket(ip, port);
			OutputStream os = socket.getOutputStream();
			InputStream is = socket.getInputStream();
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void endConnection() {
		try {
			ois.close();
			oos.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
//		Map<File, ID3Tag> mp3Map = new HashMap<>();
//		new SearchClient("localhost", 10031, );
//		new SearchClient("localhost", 10031);
//		new SearchClient("localhost", 10031);
//		new SearchClient("localhost", 10031);
		List<Property> listOfProperties = new ArrayList<>();
		SearchClient search = new SearchClient("localhost", 10031, "C:/testfiles/", "track", listOfProperties);
		search.communicateWithServer();
		
	}
}
