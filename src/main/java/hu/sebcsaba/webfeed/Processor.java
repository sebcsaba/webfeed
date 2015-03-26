package hu.sebcsaba.webfeed;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Processor {

	private final Config config;

	public Processor(Config config) {
		this.config = config;
	}

	public Set<String> process() throws IOException {
		HashSet<String> result = new HashSet<>();
		for (String code : config.getUrls().keySet()) {
			Set<String> site = processSite(code);
			result.addAll(site);
		}
		return result;
	}

	private Set<String> processSite(String code) throws IOException {
		System.out.println("processing "+code);
		String siteUrl = config.getUrls().get(code);
		System.out.println("* loading "+siteUrl);
		Document doc = Jsoup.connect(siteUrl).get();
		Elements items = doc.select(config.getSelects().get(code));
		System.out.println("* found items: "+items.size());
		Set<String> result = new HashSet<>();
		for (Element item : items) {
			String href = item.attr("href");
			result.add(getAbsoluteUrl(siteUrl, href));
		}
		return result;
	}

	private String getAbsoluteUrl(String startUrl, String href) {
		if (href.startsWith("http")) {
			return href;
		} else {
			String baseUrl = getBaseUrl(startUrl, href.startsWith("/"));
			return baseUrl + href;
		}
	}

	private String getBaseUrl(String startUrl, boolean absolute) {
		if (absolute) {
			int idx = startUrl.indexOf('/', 7);
			if (idx<0) {
				return startUrl;
			} else {
				return startUrl.substring(0, idx);
			}
		} else {
			int idx = startUrl.lastIndexOf('/');
			if (idx<0) {
				return startUrl+"/";
			} else {
				return startUrl.substring(0, idx+1);
			}
		}
	}

}
