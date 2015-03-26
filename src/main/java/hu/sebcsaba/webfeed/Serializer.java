package hu.sebcsaba.webfeed;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class Serializer {

	public Config readConfig(String configFile) throws FileNotFoundException, IOException {
		Properties p = new Properties();
		p.load(new FileInputStream(configFile));

		Config result = new Config();
		result.setSerializedDataFilename(p.getProperty("data.serialized"));
		return result;
	}

	@SuppressWarnings("unchecked")
	public Set<String> readEntries(Config config) throws IOException, ClassNotFoundException {
		File f = new File(config.getSerializedDataFilename());
		if (!f.canRead()) {
			System.err.println("Unable to access serialized data file: "+f);
			return new HashSet<>();
		}
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
		try {
			return (Set<String>) ois.readObject();
		} finally {
			ois.close();
		}
	}

	public void saveEntries(Config config, Set<String> allEntries) throws IOException {
		File f = new File(config.getSerializedDataFilename());
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
		try {
			oos.writeObject(allEntries);
		} finally {
			oos.close();
		}
	}

}
