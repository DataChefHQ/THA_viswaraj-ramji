package com.app.campaign.controller;

import com.app.campaign.constants.Constants;
import com.app.campaign.service.CampaignBannerCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@RestController
public class CampaignBannerController {

    private static final Logger logger = LoggerFactory.getLogger(CampaignBannerController.class);

    @Autowired
    private CampaignBannerCache campaignBannerCache;

    @GetMapping("/")
    public String getResponse() {
        logger.info("Received request for root endpoint.");
        return "hello world app working and integrated";
    }

    // Endpoint to get a random banner for a given campaign_id and user_id
    @GetMapping("/api/campaigns/{campaignId}")
    public ResponseEntity<Void> getRandomBanner(
            @PathVariable Long campaignId,
            @RequestParam String userId) {

        logger.info("Fetching random banner for campaignId: {} and userId: {}", campaignId, userId);

        // Fetch a random banner for the user
        Optional<Long> bannerId = campaignBannerCache.getRandomBannerForUser(campaignId, userId);

        if (bannerId.isPresent()) {
            String fileName = "image_" + bannerId.get() + ".png";
            String redirectUrl = Constants.FILE_SERVER_URL + fileName;
            logger.info("Banner found for userId: {}. Redirecting to URL: {}", userId, redirectUrl);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(Constants.HEADER_LOCATION, redirectUrl) // Set the Location header to the NGINX URL
                    .build();
        }

        logger.warn("No banner found for campaignId: {} and userId: {}", campaignId, userId);
        // Return 204 No Content if the banner ID is not present, indicating no content found
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .header(Constants.HEADER_REASON, Constants.REASON_BANNER_NOT_PRESENT) // Include a header to indicate the reason
                .build();
    }
}
