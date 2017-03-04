package hu.sebcsaba.webfeed;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class App {

	private static boolean help = false;
	private static String configfile;
	public static void main(String[] args) {
		processArgs(new LinkedList<String>(Arrays.asList(args)));
		if (help) {
			printHelp();
			System.exit(0);
		}
		try {
			new App().run(configfile);
		} catch (FileNotFoundException e) {
			System.err.println("Unable to find configfile at "+args[0]);
			System.exit(-1);
		} catch (Exception e) {
			System.err.println("An error occured: "+e.getMessage());
			System.exit(-1);
		}
	}

	private static void processArgs(Queue<String> params) {
		if ("--help".equals(params.peek())) {
			help = true;
			params.remove();
		}
		if (params.isEmpty()) {
			help = true;
		} else {
			configfile = params.remove();
		}
	}

	private static void printHelp() {
		System.out.println("Usage: java -jar webfeed.jar [--help] <configfile>");
		System.out.println("config file is a java properties file, check example in jar");
	}

	public void run(String configFile) throws Exception {
		Config config = Config.readConfig(configFile);

		Serializer s = new Serializer(config);
		Processor p = new Processor(config);
		Notifier notifier = new Notifier(config);

		try {
			Set<String> oldEntries = s.readEntries();
			Set<String> allEntries = p.process();
			Set<String> newEntries = calculateNewEntries(oldEntries, allEntries);
			notifier.notify(newEntries);
			s.saveEntries(allEntries);
		} finally {
			p.close();
		}
	}

	private Set<String> calculateNewEntries(Set<String> oldEntries, Set<String> currentEntries) {
		Set<String> result = new HashSet<>();
		result.addAll(currentEntries);
		result.removeAll(oldEntries);
		return result;
	}

}
