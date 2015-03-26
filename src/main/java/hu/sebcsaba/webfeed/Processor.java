package hu.sebcsaba.webfeed;

import java.util.HashSet;
import java.util.Set;

public class Processor {

	public Set<String> process(Config config) {
		HashSet<String> result = new HashSet<>();
		for (String url : config.getUrls()) {
			System.out.println("process "+url);
		}
		return result;
	}

}
