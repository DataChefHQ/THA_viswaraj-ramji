package com.app.campaign.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class CampaignController {


    @GetMapping("campaigns/{campaign_id}")
    public ResponseEntity<String> getCampaignBanner(@PathVariable String campaign_id) {
        return ResponseEntity.ok("application started");
    }
}
