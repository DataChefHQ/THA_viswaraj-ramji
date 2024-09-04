package com.app.campaign.service;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class CampaignBannerCache {

    private  Map<Long, Set<Long>> activeCache;
    private  Map<Long, Set<Long>> stagedCache;
    private  Map<String, Long> userLastShownBanner=new ConcurrentHashMap<>();

    public Map<Long, Set<Long>> getActiveCache() {
        return activeCache;
    }

    public void setActiveCache(Map<Long, Set<Long>> activeCache) {
        this.activeCache = activeCache;
    }

    public Map<Long, Set<Long>> getStagedCache() {
        return stagedCache;
    }

    public void setStagedCache(Map<Long, Set<Long>> stagedCache) {
        this.stagedCache = stagedCache;
    }

    public Map<String, Long> getUserLastShownBanner() {
        return userLastShownBanner;
    }

    public void loadBanners(Long campaignId, Set<Long> bannerIds, boolean toActiveCache) {
        if (toActiveCache) {
            activeCache.put(campaignId, bannerIds);
        } else {
            stagedCache.put(campaignId, bannerIds);
        }
    }

    // Method to switch staged cache to active cache
    public void switchActiveAndStaged() {
        activeCache=stagedCache;
        System.out.println("Switched staged cache to active cache.");
    }

    // Get a random banner_id for a user ensuring it's not the last shown one
    public Optional<Long> getRandomBannerForUser(Long campaignId, String userId) {
        Set<Long> banners = getActiveCache().getOrDefault(campaignId, Collections.emptySet());

        // Return empty if no banners are available
        if (banners.isEmpty()) {
            return Optional.empty();
        }

        // Retrieve the last shown banner for the user, if any
        Long lastShownBanner = userLastShownBanner.get(userId);

        // Filter available banners to exclude the last shown one
        List<Long> availableBanners = banners.stream()
                .filter(banner -> !Objects.equals(banner, lastShownBanner))
                .collect(Collectors.toList());

        // If all banners were shown or only available is the last one, reset to include all
        if (availableBanners.isEmpty()) {
            availableBanners = new ArrayList<>(banners);
        }

        // Select a random banner from the available list
        Long selectedBanner = availableBanners.get(new Random().nextInt(availableBanners.size()));

        // Update the last shown banner for the user
        userLastShownBanner.put(userId, selectedBanner);

        return Optional.of(selectedBanner);
    }

}
