package com.app.campaign.controller;

import com.app.campaign.service.CampaignBannerCache;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*") // Allow requests from any origin
public class CampaignBannerController {

    @Autowired
    private CampaignBannerCache campaignBannerCache;

    private final String fileServerUrl = "http://localhost/files/";

    @GetMapping("/")
    public String getResponse(){
        return "hello world app working and integrated";
    }
    // Endpoint to get a random banner for a given campaign_id and user_id
    @GetMapping("/api/campaigns/{campaignId}/banner")
    public ResponseEntity<Void>  getRandomBanner(
            @PathVariable Long campaignId,
            @RequestParam String userId) {

        // Fetch a random banner for the user
        Optional<Long> bannerId = campaignBannerCache.getRandomBannerForUser(campaignId, userId);

        if (bannerId.isPresent()) {
            String fileName = "image_" + bannerId.get()+".png";
            String redirectUrl = fileServerUrl + fileName;
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, redirectUrl) // Set the Location header to the NGINX URL
                    .build();
        }
        throw new RuntimeException("No bannerId");
    }
}
