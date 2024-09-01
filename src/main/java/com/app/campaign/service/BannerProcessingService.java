package com.app.campaign.service;
import com.app.campaign.repo.BannerCampaignSummaryViewRepository;
import com.app.campaign.view.BannerCampaignSummaryView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void processBanners() {
        // Fetch all data from the view
        List<BannerCampaignSummaryView> summaries = bannerCampaignSummaryViewRepository.findAll();

        // Print summaries for debugging
        summaries.forEach(summary ->
                System.out.println("CampaignId: " + summary.getId().getCampaignId() + ", BannerId: " + summary.getId().getBannerId())
        );

        // Group data by campaignId and apply strategies
        Map<Long, List<BannerCampaignSummaryView>> groupedByCampaignId = summaries.stream()
                .collect(Collectors.groupingBy(summary -> summary.getId().getCampaignId())); // Correct grouping by campaignId

        // Process each group by campaignId
        groupedByCampaignId.forEach((campaignId, campaignBanners) -> {
            // Determine x: count of banners with conversionCount > 0
            long x = campaignBanners.stream()
                    .filter(banner -> banner.getConversionCount() > 0)
                    .count();

            // Get the appropriate strategy based on x
            BannerQualificationStrategy strategy = strategyFactory.getStrategy(x);

            // Process banners for the current campaign using the strategy
            Set<Long> qualifiedBanners = strategy.qualifyBanners(campaignBanners, x);

            campaignBannerCache.loadBanners(campaignId, qualifiedBanners);
            // Further processing (e.g., store, display)
            System.out.println("Qualified Banners for Campaign " + campaignId + ": " + qualifiedBanners);
        });
    }
}
