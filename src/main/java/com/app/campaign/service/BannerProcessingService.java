package com.app.campaign.service;

import com.app.campaign.repo.BannerCampaignSummaryViewRepository;
import com.app.campaign.service.rules.BannerQualificationStrategy;
import com.app.campaign.service.rules.BannerQualificationStrategyFactory;
import com.app.campaign.view.BannerCampaignSummaryView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class BannerProcessingService {

    @Autowired
    private BannerCampaignSummaryViewRepository bannerCampaignSummaryViewRepository;

    @Autowired
    private BannerQualificationStrategyFactory strategyFactory;

    @Autowired
    private CampaignBannerCache campaignBannerCache;

    // Initial processing at startup
    public void processBannersAtStartup() {
        Map<Long, Set<Long>> activeMap=new ConcurrentHashMap<>();
        LocalDateTime localDateTime=LocalDateTime.now();
        LocalDateTime[] activeInterval = IntervalCalculator.getIntervalForQuery(LocalDateTime.now());
        List<BannerCampaignSummaryView> activeSummaries = bannerCampaignSummaryViewRepository
                .findByTimestampBetween(activeInterval[0], activeInterval[1]);
        processAndLoadToCache(activeSummaries,activeMap);
        campaignBannerCache.setActiveCache(activeMap);
        campaignBannerCache.setStagedCache(activeMap);
        processNextIntervalAsync(localDateTime); // Asynchronously process the next interval for the staged cache
    }

    // Scheduled method to switch caches and compute new staged values every 15 minutes
    @Scheduled(cron = "0 */3 * * * *") // Runs at every 0, 15, 30, 45th minute of the hour
    public void refreshCaches() {
        // Switch staged cache to active
        campaignBannerCache.switchActiveAndStaged();
        // Process the next interval to prepare a new staged cache
        processNextIntervalAsync(LocalDateTime.now());
    }

    // Async method to process the next interval (staged)
    @Async
    public void processNextIntervalAsync(LocalDateTime localDateTime) {
        Map<Long, Set<Long>> stagedMap=new ConcurrentHashMap<>();
        LocalDateTime[] stagedInterval = IntervalCalculator.getIntervalForQuery(localDateTime.plusMinutes(15));
        List<BannerCampaignSummaryView> stagedSummaries = bannerCampaignSummaryViewRepository
                .findByTimestampBetween(stagedInterval[0], stagedInterval[1]);
        campaignBannerCache.setStagedCache(stagedMap);
        processAndLoadToCache(stagedSummaries, campaignBannerCache.getStagedCache());
    }

    private void processAndLoadToCache(List<BannerCampaignSummaryView> summaries, Map<Long, Set<Long>> cache) {
        Map<Long, List<BannerCampaignSummaryView>> groupedByCampaignId = summaries.stream()
                .collect(Collectors.groupingBy(BannerCampaignSummaryView::getCampaignId));

        groupedByCampaignId.forEach((campaignId, campaignBanners) -> {
            long x = campaignBanners.stream()
                    .filter(banner -> banner.getConversionCount() > 0)
                    .count();

            BannerQualificationStrategy strategy = strategyFactory.getStrategy(x);
            Set<Long> qualifiedBanners = strategy.qualifyBanners(campaignBanners, x);

            cache.put(campaignId, qualifiedBanners);
            System.out.println("Qualified Banners for Campaign " + campaignId + ": " + qualifiedBanners);
        });
    }
}
