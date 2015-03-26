package hu.sebcsaba.webfeed;

import java.util.HashSet;
import java.util.Set;

public class App {

	public static void main(String[] args) {
		new App().run();
	}

	public void run() {
		Serializer s = new Serializer();
		Processor p = new Processor();
		Notifier notifier = new Notifier();
		Config config = s.readConfig();

		Set<String> oldEntries = s.readEntries();
		Set<String> allEntries = p.process();
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
