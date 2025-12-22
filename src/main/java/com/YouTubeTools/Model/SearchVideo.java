
package com.YouTubeTools.Model;

import java.util.List;

public class SearchVideo {
	private Video primaryVideo;
	private List<Video> relatedVideos;

	public SearchVideo(Video primaryVideo, List<Video> relatedVideos) {
		this.primaryVideo = primaryVideo;
		this.relatedVideos = relatedVideos;
	}

	public Video getPrimaryVideo() {
		return primaryVideo;
	}

	public List<Video> getRelatedVideos() {
		return relatedVideos;
	}
}
