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
	private Map<String, String> selects;
	private Map<String, String> pagers;

	public String getSerializedDataFilename() {
		return serializedDataFilename;
	}

	public Map<String, String> getUrls() {
		return urls;
	}

	public Map<String, String> getSelects() {
		return selects;
	}

	public Map<String, String> getPagers() {
		return pagers;
	}

	public static Config readConfig(String configFile) throws FileNotFoundException, IOException {
		Properties p = new Properties();
		p.load(new FileInputStream(configFile));

		Config result = new Config();
		result.serializedDataFilename = p.getProperty("data.serialized");
		result.urls = getPropertyBlock(p, "url.");
		result.selects = getPropertyBlock(p, "select.");
		result.pagers = getPropertyBlock(p, "pager.");
		return result;
	}

	private static Map<String, String> getPropertyBlock(Properties p, String prefix) {
		Map<String, String> result = new HashMap<String, String>();
		for (Object o : p.keySet()) {
			String key = (String) o;
			if (key.startsWith(prefix)) {
				String code = key.substring(prefix.length());
				result.put(code, p.getProperty(key));
			}
		}
		return result;
	}

}
