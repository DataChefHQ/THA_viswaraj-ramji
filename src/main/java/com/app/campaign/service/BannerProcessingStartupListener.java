package com.app.campaign.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class BannerProcessingStartupListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private BannerProcessingService bannerProcessingService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Perform processing before application is ready to accept requests
        System.out.println("Starting banner processing before application is ready to accept requests...");
        bannerProcessingService.processBanners();
        System.out.println("Banner processing completed.");
    }
}
