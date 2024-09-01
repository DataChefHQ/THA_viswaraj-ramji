package com.app.campaign.repo;

import com.app.campaign.view.BannerCampaignSummaryView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerCampaignSummaryRepository extends JpaRepository<BannerCampaignSummaryView, Long> {
}
