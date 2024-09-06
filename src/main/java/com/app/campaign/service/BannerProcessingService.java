package com.app.campaign.service;

import com.app.campaign.repo.BannerCampaignSummaryViewRepository;
import com.app.campaign.service.rules.BannerQualificationStrategy;
import com.app.campaign.service.rules.BannerQualificationStrategyFactory;
import com.app.campaign.view.BannerCampaignSummaryView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BannerProcessingService {

    private static final Logger logger = LoggerFactory.getLogger(BannerProcessingService.class);

    private final BannerCampaignSummaryViewRepository bannerCampaignSummaryViewRepository;
    private final BannerQualificationStrategyFactory strategyFactory;
    private final CampaignBannerCache campaignBannerCache;

    @Autowired
    public BannerProcessingService(BannerCampaignSummaryViewRepository bannerCampaignSummaryViewRepository,
                                   BannerQualificationStrategyFactory strategyFactory,
                                   CampaignBannerCache campaignBannerCache) {
        this.bannerCampaignSummaryViewRepository = bannerCampaignSummaryViewRepository;
        this.strategyFactory = strategyFactory;
        this.campaignBannerCache = campaignBannerCache;
    }

    // Initial processing before the first request is served
    @EventListener(ContextRefreshedEvent.class)
    public void processBannersAtStartup() {
        logger.info("Processing banners at application startup.");
        LocalDateTime[] activeInterval = IntervalCalculator.getIntervalForQuery(LocalDateTime.now());
        List<BannerCampaignSummaryView> activeSummaries = fetchActiveSummaries(activeInterval);
        processAndLoadToCache(activeSummaries, activeInterval[0]);
        processNextIntervalAsyncAfterCleanUp();
    }

    // Scheduled task to refresh caches every 3 minutes
    @Scheduled(cron = "0 */3 * * * *")
    @Async("taskExecutor")
    public void refreshCaches() {
        logger.info("Scheduled cache refresh running at: {}", java.time.LocalTime.now());
        processNextIntervalAsyncAfterCleanUp();
    }

    // Process the next interval asynchronously after cleaning up old entries
    private void processNextIntervalAsyncAfterCleanUp() {
        LocalDateTime localTimestamp = LocalDateTime.now();
        LocalDateTime[] currentInterval = IntervalCalculator.getIntervalForQuery(localTimestamp);
        LocalDateTime startTime = currentInterval[0];

        cleanUpOldCacheEntries(startTime);
        processNextInterval(localTimestamp.plusMinutes(3));
    }

    // Fetch active summaries within a given interval
    private List<BannerCampaignSummaryView> fetchActiveSummaries(LocalDateTime[] interval) {
        List<BannerCampaignSummaryView> summaries = bannerCampaignSummaryViewRepository
                .findByTimestampBetween(interval[0], interval[1]);
        logger.info("Fetched {} active summaries.", summaries.size());
        return summaries;
    }

    // Clean up old cache entries
    private void cleanUpOldCacheEntries(LocalDateTime startTime) {
        Iterator<LocalDateTime> iterator = campaignBannerCache.getLocalCache().keySet().iterator();
        while (iterator.hasNext()) {
            LocalDateTime key = iterator.next();
            if (key.isBefore(startTime)) {
                iterator.remove();
                logger.debug("Removed cache entry for time: {}", key);
            }
        }
    }

    // Process the next interval for banner qualification and caching
    private void processNextInterval(LocalDateTime nextIntervalStartTime) {
        LocalDateTime[] stagedInterval = IntervalCalculator.getIntervalForQuery(nextIntervalStartTime);
        List<BannerCampaignSummaryView> stagedSummaries = bannerCampaignSummaryViewRepository
                .findByTimestampBetween(stagedInterval[0], stagedInterval[1]);
        logger.info("Processing {} staged summaries.", stagedSummaries.size());
        processAndLoadToCache(stagedSummaries, stagedInterval[0]);
    }

    // Process summaries and load qualified banners into cache
    private void processAndLoadToCache(List<BannerCampaignSummaryView> summaries, LocalDateTime localDateTime) {
        Map<Long, List<BannerCampaignSummaryView>> groupedByCampaignId = summaries.stream()
                .collect(Collectors.groupingBy(BannerCampaignSummaryView::getCampaignId));

        groupedByCampaignId.forEach((campaignId, campaignBanners) -> {
            long conversionCount = campaignBanners.stream()
                    .filter(banner -> banner.getConversionCount() > 0)
                    .count();

            BannerQualificationStrategy strategy = strategyFactory.getStrategy(conversionCount);
            Set<Long> qualifiedBanners = strategy.qualifyBanners(campaignBanners, conversionCount);
            campaignBannerCache.loadBanners(localDateTime, campaignId, qualifiedBanners);
            logger.info("{} Qualified Banners for Campaign {}: {}", localDateTime, campaignId, qualifiedBanners);
        });
    }
}
