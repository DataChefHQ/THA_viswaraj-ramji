package com.app.campaign.view;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "banner_campaign_summary_view")
public class BannerCampaignSummaryView {

    @Id
    @Column(name = "banner_id")
    private Long bannerId;

    @Column(name = "campaign_id")
    private Long campaignId;

    @Column(name = "click_count")
    private Long clickCount;

    @Column(name = "conversion_count")
    private Long conversionCount;

    @Column(name = "total_revenue")
    private Double totalRevenue;

}
