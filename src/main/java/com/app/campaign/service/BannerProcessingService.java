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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        System.out.println("On app Start");
        LocalDateTime[] activeInterval = IntervalCalculator.getIntervalForQuery(LocalDateTime.now());
        List<BannerCampaignSummaryView> activeSummaries = bannerCampaignSummaryViewRepository
                .findByTimestampBetween(activeInterval[0], activeInterval[1]);
        System.out.println("activeSummaries size:" + activeSummaries.size());
        processAndLoadToCache(activeSummaries, activeInterval[0]);
        processNextIntervalAsyncAfterCleanUp();
    }


    // Scheduled to run every 3 minutes as per cron
    @Scheduled(cron = "0 */3 * * * *")
    @Async("taskExecutor")
    public void refreshCaches() {
            System.out.println("Scheduled task running at: " + java.time.LocalTime.now());
            processNextIntervalAsyncAfterCleanUp();
    }


    // Async method to process the next interval (staged)

    public void processNextIntervalAsyncAfterCleanUp() {
        LocalDateTime localTimestamp = LocalDateTime.now();

        // Use IntervalCalculator to get the rounded interval for the current time
        LocalDateTime[] currentInterval = IntervalCalculator.getIntervalForQuery(localTimestamp);

        // The start time is the beginning of the staged interval
        LocalDateTime startTime = currentInterval[0];

        // Remove entries from the map that are before the start time
        Iterator<LocalDateTime> iterator = campaignBannerCache.getLocalCache().keySet().iterator();
        while (iterator.hasNext()) {
            LocalDateTime key = iterator.next();
            if (key.isBefore(startTime)) {
                iterator.remove();
            }
        }

        // Use the next interval (3 minutes later) for processing
        LocalDateTime[] stagedInterval = IntervalCalculator.getIntervalForQuery(localTimestamp.plusMinutes(3));
        List<BannerCampaignSummaryView> stagedSummaries = bannerCampaignSummaryViewRepository
                .findByTimestampBetween(stagedInterval[0], stagedInterval[1]);
        System.out.println("stagedSummaries size:" + stagedSummaries.size());
        processAndLoadToCache(stagedSummaries, stagedInterval[0]);
    }

    private void processAndLoadToCache(List<BannerCampaignSummaryView> summaries, LocalDateTime localDateTime) {
        Map<Long, List<BannerCampaignSummaryView>> groupedByCampaignId = summaries.stream()
                .collect(Collectors.groupingBy(BannerCampaignSummaryView::getCampaignId));

        groupedByCampaignId.forEach((campaignId, campaignBanners) -> {
            long x = campaignBanners.stream()
                    .filter(banner -> banner.getConversionCount() > 0)
                    .count();

            BannerQualificationStrategy strategy = strategyFactory.getStrategy(x);
            Set<Long> qualifiedBanners = strategy.qualifyBanners(campaignBanners, x);
            campaignBannerCache.loadBanners(localDateTime, campaignId, qualifiedBanners);
            System.out.println(localDateTime + " Qualified Banners for Campaign " + campaignId + ": " + qualifiedBanners);
        });
    }
}
