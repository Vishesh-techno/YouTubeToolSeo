package com.YouTubeTools.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.YouTubeTools.Service.ThumbnailService;

import org.springframework.ui.Model;

@Controller
public class ThumbnailController {

	@Autowired
	private ThumbnailService service;

	@GetMapping("/thumbnail")
	public String getThumbnail() {
		return "thumbnails";
	}

	@PostMapping("/get-thumbnail")
	public String showThumbnail(@RequestParam("videoUrlOrId") String videoUrlOrId, Model model) {
		String videoId = service.extractVideoId(videoUrlOrId);
		if (videoId == null) {
			model.addAttribute("error", "InValid URL");
			return "thumbnails";
		}

		String thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/maxresdefault.jpg";
		model.addAttribute("thumbnailUrl", thumbnailUrl);
		model.addAttribute("videoId", videoId);
		return "thumbnails";
	}

}
