package hu.sebcsaba.webfeed;

import java.util.Set;

public class Notifier {

	private final Config config;

	public Notifier(Config config) {
		this.config = config;
	}

	public void notify(Set<String> newEntries) {
		if (newEntries.isEmpty()) {
			System.out.println("no new entries were found");
		} else {
			System.out.println("new entries were found:");
			for (String s : newEntries) {
				System.out.println(s);
			}
		}
	}

}
