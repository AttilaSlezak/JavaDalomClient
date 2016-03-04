package common;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class DirectoryScanner {

	static List<File> resultFiles = new ArrayList<File>();
	
	public static List<File> getResultFiles() {
		return resultFiles;
	}

	public List<File> collect(File file) {
		File[] files = file.listFiles(new MP3Filter());
		
		if (files.length > 0) {
			
			for (File f : files) {
				if (f.isDirectory()) {
					collect(f);
				}
				else {
					resultFiles.add(f);
				}
			}
		}
		return resultFiles;
	}

	class MP3Filter implements FileFilter {
		@Override
		public boolean accept(File f) {
			return f.getName().endsWith(".mp3") || f.isDirectory();
		}
	}
}
