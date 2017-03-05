package hu.sebcsaba.webfeed;

import java.io.IOException;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HtmlLoader {

	private final Config config;

	public HtmlLoader(Config config) {
		this.config = config;
	}

	public Document load(String siteUrl) throws IOException {
		Connection connection = Jsoup.connect(siteUrl);
		connection.timeout(config.getTimeout());
		Map<String, String> headers = config.getHttpHeaders();
		for (String key : headers.keySet()) {
			connection.header(key, headers.get(key));
		}
		return connection.get();
	}

}
