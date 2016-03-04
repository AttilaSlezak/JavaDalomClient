package main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import common.Property;

public class Main {

	public static void main(String[] args) {
//		new SearchClient("localhost", 10031);
//		new SearchClient("localhost", 10032);
//		new SearchClient("localhost", 10033);
//		new SearchClient("localhost", 10034);
//		Map<File, ID3Tag> mp3Map = new HashMap<File, ID3Tag>();
		List<Property> listOfProperties = new ArrayList<>();
		listOfProperties.add(Property.TITLE);
		listOfProperties.add(Property.ARTIST);
		//SearchClient search = new SearchClient("192.168.150.38", 10031, "C:/testfiles/", "track", listOfProperties);
		SearchClient search = new SearchClient("localhost", 10031, "C:/testfiles/", "Pussy", listOfProperties);
		List<File> result = search.communicateWithServer();
		for (File f : result) {
			System.out.println(f.getAbsolutePath());
		}
		search.endConnection();

	}
}
