package hu.sebcsaba.webfeed;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config {

	private String serializedDataFilename;
	private Map<String, Task> tasks = new HashMap<>();

	public String getSerializedDataFilename() {
		return serializedDataFilename;
	}

	public Map<String, Task> getTasks() {
		return tasks;
	}

	public static class Task {
		private String name;
		private Set<String> urls;
		private String selector;
		private String pager;

		public String getName() {
			return name;
		}

		public Set<String> getUrls() {
			return urls;
		}

		public String getSelector() {
			return selector;
		}

		public String getPager() {
			return pager;
		}

	}

	public static Config readConfig(String configFile) throws FileNotFoundException, IOException {
		Properties p = new Properties();
		p.load(new FileInputStream(configFile));
		return readConfig(p);
	}

	public static Config readConfig(Properties p) {
		Config result = new Config();
		result.serializedDataFilename = p.getProperty("data.serialized");
		for (String taskName : getTaskNames(p)) {
			Task task = new Task();
			task.name = taskName;
			task.urls = getUrls(p, taskName);
			task.selector = p.getProperty("task."+taskName+".selector");
			task.pager = p.getProperty("task."+taskName+".pager");
			result.tasks.put(taskName, task);
		}
		return result;
	}

	private static Set<String> getTaskNames(Properties p) {
		Pattern expr = Pattern.compile("^task\\.([^\\.]+)\\..*");
		Set<String> result = new HashSet<>();
		for (Object keyObject : p.keySet()) {
			String key = (String) keyObject;
			Matcher m = expr.matcher(key);
			if (m.matches()) {
				result.add(m.group(1));
			}
		}
		return result;
	}

	private static Set<String> getUrls(Properties p, String taskName) {
		Pattern expr = Pattern.compile("^task\\."+taskName+"\\.url($|\\..*)");
		Set<String> result = new HashSet<>();
		for (Object keyObject : p.keySet()) {
			String key = (String) keyObject;
			Matcher m = expr.matcher(key);
			if (m.matches()) {
				result.add(p.getProperty(key));
			}
		}
		return result;
	}

}
