package com.app.campaign.view;

import com.app.campaign.view.id.BannerCampaignSummaryViewId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@Table(name = "banner_campaign_summary_view")  // This maps to the view
@IdClass(BannerCampaignSummaryViewId.class)    // Specifies the composite key class
public class BannerCampaignSummaryView {

    @Id
    @Column(name = "banner_id")
    private Long bannerId;

    @Id
    @Column(name = "campaign_id")
    private Long campaignId;

    @Id
    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "click_count")
    private Long clickCount;

    @Column(name = "conversion_count")
    private Long conversionCount;

    @Column(name = "total_revenue")
    private Double totalRevenue;
}
