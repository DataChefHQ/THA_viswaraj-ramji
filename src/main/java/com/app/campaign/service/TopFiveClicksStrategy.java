package com.app.campaign.service;

import com.app.campaign.view.BannerCampaignSummaryView;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TopFiveClicksStrategy implements BannerQualificationStrategy {

    @Override
    public boolean appliesTo(long x) {
        return x == 0;
    }

    @Override
    public Set<Long> qualifyBanners(List<BannerCampaignSummaryView> banners, long x) {
        List<Long> topClicksBanners = banners.stream()
                .sorted((b1, b2) -> Long.compare(b2.getClickCount(), b1.getClickCount()))
                .limit(5)
                .map(banner -> banner.getId().getBannerId()) // Map to bannerId
                .collect(Collectors.toList());

        if (topClicksBanners.size() < 5) {
            List<Long> remainingBanners = banners.stream()
                    .map(banner -> banner.getId().getBannerId()) // Map to bannerId
                    .filter(bannerId -> !topClicksBanners.contains(bannerId))
                    .collect(Collectors.toList());

            Random random = new Random();
            while (topClicksBanners.size() < 5 && !remainingBanners.isEmpty()) {
                topClicksBanners.add(remainingBanners.remove(random.nextInt(remainingBanners.size())));
            }
        }

        return Set.copyOf(topClicksBanners);
    }
}
