package com.app.campaign.service;

import com.app.campaign.view.BannerCampaignSummaryView;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TopXRevenueStrategy implements BannerQualificationStrategy {

    @Override
    public boolean appliesTo(long x) {
        return x >= 5 && x < 10;
    }

    @Override
    public Set<Long> qualifyBanners(List<BannerCampaignSummaryView> banners, long x) {
        return banners.stream()
                .sorted((b1, b2) -> Double.compare(b2.getTotalRevenue(), b1.getTotalRevenue()))
                .limit(x)
                .map(banner -> banner.getId().getBannerId()) // Map to bannerId
                .collect(Collectors.toSet());
    }
}
