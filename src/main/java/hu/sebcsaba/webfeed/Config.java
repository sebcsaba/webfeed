package hu.sebcsaba.webfeed;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Config {

	private String serializedDataFilename;
	private Map<String, String> urls;

	public String getSerializedDataFilename() {
		return serializedDataFilename;
	}

	public Map<String, String> getUrls() {
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

	private static Map<String, String> propGetUrls(Properties p) {
		Map<String, String> result = new HashMap<String, String>();
		for (Object o : p.keySet()) {
			String key = (String) o;
			if (key.startsWith("url.")) {
				String code = key.substring(4);
				result.put(code, p.getProperty(key));
			}
		}
		return result;
	}

}
