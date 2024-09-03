package com.app.campaign.service.rules;

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
                .sorted(Comparator.comparingDouble(BannerCampaignSummaryView::getTotalRevenue).reversed())
                .limit(10)
                .map(banner -> banner.getBannerId())
                .collect(Collectors.toSet());
    }
}
