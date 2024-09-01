package com.app.campaign.service;

import com.app.campaign.view.BannerCampaignSummaryView;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TopTenRevenueStrategy implements BannerQualificationStrategy {

    @Override
    public boolean appliesTo(long x) {
        return x >= 10;
    }

    @Override
    public Set<Long> qualifyBanners(List<BannerCampaignSummaryView> banners, long x) {
        return banners.stream()
                .sorted(Comparator.comparingDouble(BannerCampaignSummaryView::getTotalRevenue).reversed()) // Sort by total revenue in descending order
                .limit(10) // Limit to top 10 banners
                .map(banner -> banner.getId().getBannerId()) // Map to bannerId
                .collect(Collectors.toSet()); // Collect as a set to ensure uniqueness
    }
}
