package hu.sebcsaba.webfeed;

import java.util.Set;

public class Notifier {

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
