package com.app.campaign.service;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class CampaignBannerCache {

    // Cache to store campaign_id -> set of banner_id
    private final Map<Long, Set<Long>> campaignBannersCache = new ConcurrentHashMap<>();

    // Map to track the last shown banner_id for each user
    private final Map<String, Long> userLastShownBanner = new ConcurrentHashMap<>();

    // Load banners into the cache
    public void loadBanners(Long campaignId, Set<Long> bannerIds) {
        campaignBannersCache.put(campaignId, new HashSet<>(bannerIds));
    }

    // Get a random banner_id for a user ensuring it's not the last shown one
    public Optional<Long> getRandomBannerForUser(Long campaignId, String userId) {
        Set<Long> banners = campaignBannersCache.getOrDefault(campaignId, Collections.emptySet());

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
