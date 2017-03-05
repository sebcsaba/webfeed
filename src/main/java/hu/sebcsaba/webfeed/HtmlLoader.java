package hu.sebcsaba.webfeed;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HtmlLoader {

	private final Config config;

	public HtmlLoader(Config config) {
		this.config = config;
	}

	public Document load(String siteUrl, String baseUrl) throws IOException {
		String httpLoader = config.getHttpLoader();
		if ("wget".equals(httpLoader)) {
			return loadWget(siteUrl, baseUrl);
		}
		else if ("builtin".equals(httpLoader) || httpLoader == null) {
			return loadBuiltin(siteUrl);
		}
		else {
			throw new IOException("Unknown http loader: "+httpLoader);
		}
	}

	private Document loadWget(String siteUrl, String baseUrl) throws IOException {
		Path tempFile = Files.createTempFile("webfeed-", ".html");
		List<String> command = new ArrayList<>();
		command.add("wget");
		command.add("-O");
		command.add(tempFile.toString());
		Map<String, String> headers = config.getHttpHeaders();
		for (String key : headers.keySet()) {
			command.add("--header='"+key+": "+headers.get(key)+"'");
		}
		command.add(siteUrl);
		Runtime rt = Runtime.getRuntime();
		Process p = rt.exec(command.toArray(new String[command.size()]));
		if (p.exitValue() != 0) {
			Scanner scanner = new Scanner(p.getErrorStream());
			try {
				String stderr = scanner.useDelimiter("\\A").next();
				throw new IOException("process returned "+p.exitValue()+", stderr:\n"+stderr);
			} finally {
				scanner.close();
			}
		}
		Document doc = Jsoup.parse(tempFile.toFile(), "UTF-8", baseUrl);
		Files.delete(tempFile);
		return doc;
	}

	private Document loadBuiltin(String siteUrl) throws IOException {
		Connection connection = Jsoup.connect(siteUrl);
		connection.timeout(config.getTimeout());
		Map<String, String> headers = config.getHttpHeaders();
		for (String key : headers.keySet()) {
			connection.header(key, headers.get(key));
		}
		return connection.get();
	}

}
