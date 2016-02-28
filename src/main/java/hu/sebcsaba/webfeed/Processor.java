package hu.sebcsaba.webfeed;

import hu.sebcsaba.webfeed.Config.Task;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Processor implements Closeable {

	private final Config config;
	private final PrintWriter log;

	public Processor(Config config) throws FileNotFoundException {
		this.config = config;
		this.log = new PrintWriter("webfeed.log");
	}

	public void close() {
		log.close();
	}

	public Set<String> process() throws IOException {
		HashSet<String> result = new HashSet<>();
		for (Task task : config.getTasks().values()) {
			Set<String> site = processSite(task);
			result.addAll(site);
		}
		return result;
	}

	private Set<String> processSite(Task task) throws IOException {
		Set<String> result = new HashSet<>();
		int urlIndex = 0;
		for (String siteUrl : task.getUrls()) {
			++urlIndex;
			String nextPageUrl = siteUrl;
			int pageIndex = 0;
			do {
				nextPageUrl = processSitePage(result, task, nextPageUrl, urlIndex, ++pageIndex);
			} while (nextPageUrl != null);
		}
		return result;
	}

	private String processSitePage(Set<String> result, Task task, String siteUrl, int urlIndex, int pageIndex) throws IOException {
		log.println("processing "+task.getName()+", url #"+urlIndex+", page #"+pageIndex);
		log.println("* loading "+siteUrl);
		Document doc = Jsoup.connect(siteUrl).get();
		Elements items = doc.select(task.getSelector());
		log.println("* found items: "+items.size());
		for (Element item : items) {
			String href = item.attr("href");
			result.add(getAbsoluteUrl(siteUrl, href));
		}
		String pager = task.getPager();
		if (pager != null) {
			Element nextLink = doc.select(pager).first();
			if (nextLink!=null) {
				String href = nextLink.attr("href");
				return getAbsoluteUrl(siteUrl, href);
			}
		}
		return null;
	}

	private String getAbsoluteUrl(String startUrl, String href) {
		int hash = href.indexOf('#');
		if (hash>=0) {
			href = href.substring(0, hash);
		}
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
