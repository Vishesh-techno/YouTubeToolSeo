package com.YouTubeTools.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.YouTubeTools.Model.SearchVideo;
import com.YouTubeTools.Model.Video;

@Service
public class YouTubeService {

    private final WebClient.Builder webClientBuilder;

    public YouTubeService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Value("${youtube.api.key}")
    private String apiKey;

    @Value("${youtube.api.base.url}")
    private String baseUrl;

    @Value("${youtube.api.max.related.videos}")
    private int maxRelatedVideos;

    public SearchVideo searchVideos(String videoTitle) {
        List<String> videoIds = searchForVideoId(videoTitle);

        if (videoIds.isEmpty()) {
            return new SearchVideo(null, Collections.emptyList());
        }

        String primaryVideoId = videoIds.get(0);
        List<String> relatedVideoIds = videoIds.subList(1, Math.min(videoIds.size(), maxRelatedVideos + 5));

        Video primaryVideo = getVideoById(primaryVideoId);
        List<Video> relatedVideos = new ArrayList<>();

        for (String id : relatedVideoIds) {
            Video video = getVideoById(id);
            if (video != null) {
                relatedVideos.add(video);
            }
        }

        return new SearchVideo(primaryVideo, relatedVideos); 
    }

    private List<String> searchForVideoId(String videoTitle) {
        SearchApiResponse response = webClientBuilder.baseUrl(baseUrl).build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("part", "snippet")
                        .queryParam("q", videoTitle)
                        .queryParam("type", "video")
                        .queryParam("maxResults", maxRelatedVideos)
                        .queryParam("key", apiKey)
                        .build()
                )
                .retrieve()
                .bodyToMono(SearchApiResponse.class)
                .block();

        if (response == null || response.getItems() == null) {
            return Collections.emptyList();
        }

        List<String> videoIds = new ArrayList<>();
        for (SearchItem item : response.getItems()) {
            videoIds.add(item.getId().getVideoId());
        }
        return videoIds;
    }

    private Video getVideoById(String videoId) {
        VideoApiResponse response = webClientBuilder.baseUrl(baseUrl).build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/videos")
                        .queryParam("part", "snippet")
                        .queryParam("id", videoId)
                        .queryParam("key", apiKey)
                        .build()
                )
                .retrieve()
                .bodyToMono(VideoApiResponse.class)
                .block();

        if (response == null || response.getItems() == null || response.getItems().isEmpty()) {
            return null;
        }

        Snippet snippet = response.getItems().get(0).getSnippet();
        return new Video(
                videoId,
                snippet.getTitle(),
                snippet.getChannelTitle(),
                snippet.getTags() == null ? Collections.emptyList() : snippet.getTags()
        );
    }

//    -------DTO-------

    public static class SearchApiResponse {
        private List<SearchItem> items;

        public List<SearchItem> getItems() {
            return items;
        }

        public void setItems(List<SearchItem> items) {
            this.items = items;
        }
    }

    public static class SearchItem {
        private Id id;

        public Id getId() {
            return id;
        }

        public void setId(Id id) {
            this.id = id;
        }
    }

    public static class Id {
        private String videoId;

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }
    }

    public static class VideoApiResponse {
        private List<VideoItem> items;

        public List<VideoItem> getItems() {
            return items;
        }

        public void setItems(List<VideoItem> items) {
            this.items = items;
        }
    }

    public static class VideoItem {
        private Snippet snippet;

        public Snippet getSnippet() {
            return snippet;
        }

        public void setSnippet(Snippet snippet) {
            this.snippet = snippet;
        }
    }

    public static class Snippet {
        private Thumbnails thumbnails;
        private String title;
        private String description;
        private String channelTitle;
        private List<String> tags;

        public Thumbnails getThumbnails() {
            return thumbnails;
        }

        public void setThumbnails(Thumbnails thumbnails) {
            this.thumbnails = thumbnails;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getChannelTitle() {
            return channelTitle;
        }

        public void setChannelTitle(String channelTitle) {
            this.channelTitle = channelTitle;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }
    }

    public static class Thumbnails {
        private Thumbnail maxres;
        private Thumbnail high;
        private Thumbnail medium;
        private Thumbnail _default;

        public Thumbnail getMaxres() {
            return maxres;
        }

        public void setMaxres(Thumbnail maxres) {
            this.maxres = maxres;
        }

        public Thumbnail getHigh() {
            return high;
        }

        public void setHigh(Thumbnail high) {
            this.high = high;
        }

        public Thumbnail getMedium() {
            return medium;
        }

        public void setMedium(Thumbnail medium) {
            this.medium = medium;
        }

        public Thumbnail getDefault() {
            return _default;
        }

        public void setDefault(Thumbnail _default) {
            this._default = _default;
        }

        public String getBestThumbnailUrl() {
            if (maxres != null) return maxres.getUrl();
            if (high != null) return high.getUrl();
            if (medium != null) return medium.getUrl();
            return _default != null ? _default.getUrl() : "";
        }
    }

    public static class Thumbnail {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
