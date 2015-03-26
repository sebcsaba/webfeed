package hu.sebcsaba.webfeed;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Config {

	private String serializedDataFilename;
	private List<String> urls;

	public String getSerializedDataFilename() {
		return serializedDataFilename;
	}

	public List<String> getUrls() {
		return urls;
	}

	public static Config readConfig(String configFile) throws FileNotFoundException, IOException {
		Properties p = new Properties();
		p.load(new FileInputStream(configFile));

		Config result = new Config();
		result.serializedDataFilename = p.getProperty("data.serialized");
		result.urls = propGetUrls(p);
		return result;
	}

	private static List<String> propGetUrls(Properties p) {
		List<String> urls = new ArrayList<>();
		for (Object o : p.keySet()) {
			String key = (String) o;
			if (key.startsWith("url.")) {
				urls.add(p.getProperty(key));
			}
		}
		return urls;
	}

}
