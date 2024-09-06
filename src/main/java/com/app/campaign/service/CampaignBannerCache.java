package com.app.campaign.service;


import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class CampaignBannerCache {

    // Cache mapping intervals to campaign banners
    private Map<LocalDateTime, Map<Long, Set<Long>>> localCache = new ConcurrentHashMap<>();
    // Cache to track the last shown banner per user
    private Map<String, Long> userLastShownBanner = new ConcurrentHashMap<>();

    // Accessor for local cache
    public Map<LocalDateTime, Map<Long, Set<Long>>> getLocalCache() {
        return localCache;
    }

    // Setter for local cache
    public void setLocalCache(Map<LocalDateTime, Map<Long, Set<Long>>> localCache) {
        this.localCache = localCache;
    }

    // Accessor for user last shown banner cache
    public Map<String, Long> getUserLastShownBanner() {
        return userLastShownBanner;
    }

    // Setter for user last shown banner cache
    public void setUserLastShownBanner(Map<String, Long> userLastShownBanner) {
        this.userLastShownBanner = userLastShownBanner;
    }

    // Retrieves a random banner for the user, avoiding the last shown banner if possible
    public Optional<Long> getRandomBannerForUser(Long campaignId, String userId) {
        LocalDateTime currentTimestamp = getCurrentIntervalStartTime();
        Set<Long> banners = getBannersForCampaign(campaignId, currentTimestamp);

        if (banners.isEmpty()) {
            return Optional.empty();
        }

        List<Long> availableBanners = filterOutLastShownBanner(banners, userId);
        Long selectedBanner = selectRandomBanner(availableBanners);

        updateUserLastShownBanner(userId, selectedBanner);
        return Optional.of(selectedBanner);
    }

    // Load banners into the cache for a specific campaign and interval
    public void loadBanners(LocalDateTime timestamp, Long campaignId, Set<Long> bannerIds) {
        localCache.computeIfAbsent(timestamp, k -> new ConcurrentHashMap<>())
                .put(campaignId, bannerIds);
    }

    // Get the start time of the current interval
    private LocalDateTime getCurrentIntervalStartTime() {
        LocalDateTime[] currentRanges = IntervalCalculator.getIntervalForQuery(LocalDateTime.now());
        return currentRanges[0];
    }

    // Retrieve banners for a specific campaign at a given timestamp
    private Set<Long> getBannersForCampaign(Long campaignId, LocalDateTime timestamp) {
        return localCache.getOrDefault(timestamp, Collections.emptyMap())
                .getOrDefault(campaignId, Collections.emptySet());
    }

    // Filter out the last shown banner for the user from the available banners
    private List<Long> filterOutLastShownBanner(Set<Long> banners, String userId) {
        Long lastShownBanner = userLastShownBanner.get(userId);
        List<Long> availableBanners = banners.stream()
                .filter(banner -> !Objects.equals(banner, lastShownBanner))
                .collect(Collectors.toList());

        // Reset if no banners are left after filtering
        if (availableBanners.isEmpty()) {
            availableBanners = new ArrayList<>(banners);
        }
        return availableBanners;
    }

    // Select a random banner from the list of available banners
    private Long selectRandomBanner(List<Long> availableBanners) {
        Random random = new Random();
        return availableBanners.get(random.nextInt(availableBanners.size()));
    }

    // Update the last shown banner for a user
    private void updateUserLastShownBanner(String userId, Long selectedBanner) {
        userLastShownBanner.put(userId, selectedBanner);
    }
}
