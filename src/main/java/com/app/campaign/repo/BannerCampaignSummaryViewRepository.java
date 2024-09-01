package com.app.campaign.repo;

import com.app.campaign.view.BannerCampaignSummaryKey;
import com.app.campaign.view.BannerCampaignSummaryView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerCampaignSummaryViewRepository extends JpaRepository<BannerCampaignSummaryView, BannerCampaignSummaryKey> {
}
