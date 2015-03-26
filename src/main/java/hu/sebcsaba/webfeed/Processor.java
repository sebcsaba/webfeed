package hu.sebcsaba.webfeed;

import java.util.HashSet;
import java.util.Set;

public class Processor {

	private final Config config;

	public Processor(Config config) {
		this.config = config;
	}

	public Set<String> process() {
		HashSet<String> result = new HashSet<>();
		for (String code : config.getUrls().keySet()) {
			System.out.println("processing "+code);
		}
		return result;
	}

}
