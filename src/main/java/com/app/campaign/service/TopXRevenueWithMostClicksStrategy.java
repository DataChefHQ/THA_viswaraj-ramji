package com.app.campaign.service;

import com.app.campaign.view.BannerCampaignSummaryView;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TopXRevenueWithMostClicksStrategy implements BannerQualificationStrategy {

    @Override
    public boolean appliesTo(long x) {
        return x >= 1 && x < 5;
    }

    @Override
    public Set<Long> qualifyBanners(List<BannerCampaignSummaryView> banners, long x) {
        List<Long> topRevenueBanners = banners.stream()
                .sorted((b1, b2) -> Double.compare(b2.getTotalRevenue(), b1.getTotalRevenue()))
                .limit(x)
                .map(banner -> banner.getId().getBannerId()) // Map to bannerId
                .collect(Collectors.toList());

        List<Long> mostClicksBanners = banners.stream()
                .sorted((b1, b2) -> Long.compare(b2.getClickCount(), b1.getClickCount()))
                .map(banner -> banner.getId().getBannerId()) // Map to bannerId
                .filter(bannerId -> !topRevenueBanners.contains(bannerId))
                .limit(5 - x)
                .collect(Collectors.toList());

        topRevenueBanners.addAll(mostClicksBanners);
        return Set.copyOf(topRevenueBanners);
    }
}
