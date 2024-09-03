package com.app.campaign.repo;

import com.app.campaign.view.BannerCampaignSummaryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BannerCampaignSummaryViewRepository extends JpaRepository<BannerCampaignSummaryView, Long> {
    List<BannerCampaignSummaryView> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
