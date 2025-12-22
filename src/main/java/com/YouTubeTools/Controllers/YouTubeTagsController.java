package com.YouTubeTools.Controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.YouTubeTools.Model.SearchVideo;
import com.YouTubeTools.Service.YouTubeService;

@Controller
@RequestMapping("/youtube")
public class YouTubeTagsController {

    @Value("${youtube.api.key}")
    private String apiKey;

    private final YouTubeService youTubeService;

    public YouTubeTagsController(YouTubeService youTubeService) {
        this.youTubeService = youTubeService;
    }

    private boolean isApiKeyPresent() {
        return apiKey != null && !apiKey.isEmpty();
    }

    @PostMapping("/search")
    public String videoTags(@RequestParam("videoTitle") String videoTitle, Model model) {

        if (!isApiKeyPresent()) {
            model.addAttribute("error", "API Key is not configured");
            return "home";
        }

        if (videoTitle == null || videoTitle.isEmpty()) {
            model.addAttribute("error", "Video Title is required");
            return "home";
        }

        try {
            SearchVideo result = youTubeService.searchVideos(videoTitle);
            model.addAttribute("primaryVideo", result.getPrimaryVideo());
            model.addAttribute("relatedVideos", result.getRelatedVideos());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        return "home";
    }
}
