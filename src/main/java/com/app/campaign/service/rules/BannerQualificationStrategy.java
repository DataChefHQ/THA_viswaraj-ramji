package com.app.campaign.service.rules;
import com.app.campaign.view.BannerCampaignSummaryView;

import java.util.List;
import java.util.Set;

public interface BannerQualificationStrategy {
    boolean appliesTo(long x);  // Checks if the strategy applies to the given value of x
    Set<Long> qualifyBanners(List<BannerCampaignSummaryView> banners, long x);
}
