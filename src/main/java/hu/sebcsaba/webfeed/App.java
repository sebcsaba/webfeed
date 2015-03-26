package hu.sebcsaba.webfeed;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

public class App {

	public static void main(String[] args) {
		if ((args.length != 1) || "--help".equals(args[0])) {
			printHelp();
			System.exit(0);
		}
		try {
			new App().run(args[0]);
		} catch (FileNotFoundException e) {
			System.err.println("Unable to find configfile at "+args[0]);
			System.exit(-1);
		} catch (Exception e) {
			System.err.println("An error occured: "+e.getMessage());
			System.exit(-1);
		}
	}

	private static void printHelp() {
		System.out.println("Usage: java -jar webfeed.jar <configfile>");
		System.out.println("config file is a java properties file, check example in jar");
	}

	public void run(String configFile) throws Exception {
		Config config = Config.readConfig(configFile);

		Serializer s = new Serializer(config);
		Processor p = new Processor();
		Notifier notifier = new Notifier();

		Set<String> oldEntries = s.readEntries();
		Set<String> allEntries = p.process(config);
		Set<String> newEntries = calculateNewEntries(oldEntries, allEntries);
		notifier.notify(newEntries);
		s.saveEntries(allEntries);
	}

	private Set<String> calculateNewEntries(Set<String> oldEntries, Set<String> currentEntries) {
		Set<String> result = new HashSet<>();
		result.addAll(currentEntries);
		result.removeAll(oldEntries);
		return result;
	}

}
