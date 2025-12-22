// Video.java
package com.YouTubeTools.Model;

import java.util.List;

public class Video {
	private String id;
	private String title;
	private String channelTitle;
	private List<String> tags;

	public Video(String id, String title, String channelTitle, List<String> tags) {
		this.id = id;
		this.title = title;
		this.channelTitle = channelTitle;
		this.tags = tags;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getChannelTitle() {
		return channelTitle;
	}

	public List<String> getTags() {
		return tags;
	}
}
