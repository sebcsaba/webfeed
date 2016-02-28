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

	public static final int DEFAULT_TIMEOUT = 10000;

	private String serializedDataFilename;
	private int timeout;
	private Map<String, Task> tasks = new HashMap<>();

	public String getSerializedDataFilename() {
		return serializedDataFilename;
	}

	public int getTimeout() {
		return timeout;
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
		result.timeout = Integer.parseInt(p.getProperty("connection.timeout_ms", Integer.toString(DEFAULT_TIMEOUT)));
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
				String url = p.getProperty(key);
				Map<String,Set<String>> vars = getTemplateVariables(p, taskName);
				Set<String> urls = replaceTemplateVariables(url, vars);
				result.addAll(urls);
			}
		}
		return result;
	}

	private static Set<String> replaceTemplateVariables(String url, Map<String, Set<String>> vars) {
		Set<String> result = new HashSet<>();
		Pattern expr = Pattern.compile("^(.*)\\{(\\w+)\\}(.*)$");
		Matcher m = expr.matcher(url);
		if (m.matches()) {
			String varName = m.group(2);
			if (!vars.containsKey(varName)) {
				throw new IllegalArgumentException("unknown variable '"+varName+"' for url '"+url+"'");
			}
			String before = m.group(1);
			String after = m.group(3);
			for (String val : vars.get(varName)) {
				String newUrl = before + val + after;
				result.addAll(replaceTemplateVariables(newUrl, vars));
			}
		} else {
			result.add(url);
		}
		return result;
	}

	private static Map<String,Set<String>> getTemplateVariables(Properties p, String taskName) {
		Pattern expr = Pattern.compile("^task\\."+taskName+"\\.variable\\.(\\w+)$");
		Map<String, Set<String>> result = new HashMap<>();
		for (Object keyObject : p.keySet()) {
			String key = (String) keyObject;
			Matcher m = expr.matcher(key);
			Set<String> values = new HashSet<>();
			if (m.matches()) {
				String varName = m.group(1);
				for (String s : p.getProperty(key).split(";")) {
					values.add(s);
				}
				result.put(varName, values);
			}
		}
		return result;
	}

}
