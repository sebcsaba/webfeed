package hu.sebcsaba.webfeed;

import java.util.List;

public class Config {

	private String serializedDataFilename;
	private List<String> urls;

	public String getSerializedDataFilename() {
		return serializedDataFilename;
	}

	public void setSerializedDataFilename(String serializedDataFilename) {
		this.serializedDataFilename = serializedDataFilename;
	}

	public List<String> getUrls() {
		return urls;
	}

	public void setUrls(List<String> urls) {
		this.urls = urls;
	}

}
