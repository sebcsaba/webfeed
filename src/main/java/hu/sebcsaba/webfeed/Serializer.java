package hu.sebcsaba.webfeed;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class Serializer {

	public Config readConfig(String configFile) throws FileNotFoundException, IOException {
		Properties p = new Properties();
		p.load(new FileInputStream(configFile));
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> readEntries() {
		// TODO Auto-generated method stub
		return new HashSet<>();
	}

	public void saveEntries(Set<String> allEntries) {
		// TODO Auto-generated method stub
		
	}

}
