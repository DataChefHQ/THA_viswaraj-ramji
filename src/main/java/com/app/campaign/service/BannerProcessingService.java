package com.app.campaign.service;

import com.app.campaign.repo.BannerCampaignSummaryViewRepository;
import com.app.campaign.service.rules.BannerQualificationStrategy;
import com.app.campaign.service.rules.BannerQualificationStrategyFactory;
import com.app.campaign.view.BannerCampaignSummaryView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class BannerProcessingService {

    @Autowired
    private BannerCampaignSummaryViewRepository bannerCampaignSummaryViewRepository;

    @Autowired
    private BannerQualificationStrategyFactory strategyFactory;

    @Autowired
    private CampaignBannerCache campaignBannerCache;

    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    // Initial processing at startup
    public void processBannersAtStartup() {
        System.out.println("On app Start");
        Map<Long, Set<Long>> activeMap=new ConcurrentHashMap<>();
        LocalDateTime localDateTime=LocalDateTime.now();
        LocalDateTime[] activeInterval = IntervalCalculator.getIntervalForQuery(LocalDateTime.now());
        List<BannerCampaignSummaryView> activeSummaries = bannerCampaignSummaryViewRepository
                .findByTimestampBetween(activeInterval[0], activeInterval[1]);
        System.out.println("activeSummaries size:"+activeSummaries.size());
        processAndLoadToCache(activeSummaries,activeMap);
        campaignBannerCache.setActiveCache(activeMap);
        campaignBannerCache.setStagedCache(activeMap);
//        processNextIntervalAsync(localDateTime); // Asynchronously process the next interval for the staged cache
    }




    // Run once when the application context is fully initialized
    @EventListener(ApplicationReadyEvent.class)
    public void runOnceAfterStartup() {
        if (isRunning.compareAndSet(false, true)) {
            try {
                System.out.println("Running refreshCaches immediately after application start");
                refreshCachesLogic(); // Call the method directly
            } finally {
                isRunning.set(false);
            }
        }
    }

    // Scheduled to run every 3 minutes as per cron
    @Scheduled(cron = "0 */3 * * * *")  // Runs every 3 minutes at the 0th second
    public void refreshCaches() {
        if (isRunning.compareAndSet(false, true)) {
            try {
                System.out.println("Scheduled task running at: " + java.time.LocalTime.now());
                refreshCachesLogic();
            } finally {
                isRunning.set(false);
            }
        } else {
            System.out.println("Skipping refreshCaches due to overlap at: " + java.time.LocalTime.now());
        }
    }

    public void refreshCachesLogic() {
        System.out.println("On App refresh");
        // Switch staged cache to active
        campaignBannerCache.switchActiveAndStaged();
        // Process the next interval to prepare a new staged cache
        processNextIntervalAsync(LocalDateTime.now());
    }

    // Async method to process the next interval (staged)
    @Async("taskExecutor")
    public void processNextIntervalAsync(LocalDateTime localDateTime) {
        Map<Long, Set<Long>> stagedMap=new ConcurrentHashMap<>();
        LocalDateTime[] stagedInterval = IntervalCalculator.getIntervalForQuery(localDateTime.plusMinutes(3));
        List<BannerCampaignSummaryView> stagedSummaries = bannerCampaignSummaryViewRepository
                .findByTimestampBetween(stagedInterval[0], stagedInterval[1]);
        campaignBannerCache.setStagedCache(stagedMap);
        System.out.println("stagedSummaries size:"+stagedSummaries.size());
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
