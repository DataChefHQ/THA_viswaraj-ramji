package com.app.campaign.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class CampaignBannerCache {

    private Map<LocalDateTime, Map<Long, Set<Long>>> localCache = new ConcurrentHashMap<>();
    private Map<String, Long> userLastShownBanner = new ConcurrentHashMap<>();

    public Map<LocalDateTime, Map<Long, Set<Long>>> getLocalCache() {
        return localCache;
    }

    public void setLocalCache(Map<LocalDateTime, Map<Long, Set<Long>>> localCache) {
        this.localCache = localCache;
    }

    public void setUserLastShownBanner(Map<String, Long> userLastShownBanner) {
        this.userLastShownBanner = userLastShownBanner;
    }

    public Map<String, Long> getUserLastShownBanner() {
        return userLastShownBanner;
    }

    // Get a random banner_id for a user ensuring it's not the last shown one
    public Optional<Long> getRandomBannerForUser(Long campaignId, String userId) {
        // Get the current timestamp and round down to the nearest 15-minute interval
        LocalDateTime[] currentRanges = IntervalCalculator.getIntervalForQuery(LocalDateTime.now());

        // Query the cache using the calculated start time
        Map<Long, Set<Long>> campaignBanners = getLocalCache().getOrDefault(currentRanges[0], Collections.emptyMap());
        Set<Long> banners = campaignBanners.getOrDefault(campaignId, Collections.emptySet());

        // Return empty if no banners are available
        if (banners.isEmpty()) {
            return Optional.empty();
        }

        // Retrieve the last shown banner for the user, if any
        Long lastShownBanner = getUserLastShownBanner().get(userId);

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



    public void loadBanners(LocalDateTime timestamp, Long campaignId, Set<Long> bannerIds) {
        localCache.computeIfAbsent(timestamp, k -> new ConcurrentHashMap<>())
                .put(campaignId, bannerIds);
    }
}
