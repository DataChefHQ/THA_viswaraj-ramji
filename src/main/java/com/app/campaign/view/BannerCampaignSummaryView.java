package com.app.campaign.view;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "banner_campaign_summary_view")
public class BannerCampaignSummaryView {

    @EmbeddedId
    private BannerCampaignSummaryKey id;  // Composite key

    @Column(name = "click_count")
    private Long clickCount;

    @Column(name = "conversion_count")
    private Long conversionCount;

    @Column(name = "total_revenue")
    private Double totalRevenue;

    // Getters and Setters (if not using Lombok)

    public BannerCampaignSummaryKey getId() {
        return id;
    }

    public void setId(BannerCampaignSummaryKey id) {
        this.id = id;
    }

    public Long getClickCount() {
        return clickCount;
    }

    public void setClickCount(Long clickCount) {
        this.clickCount = clickCount;
    }

    public Long getConversionCount() {
        return conversionCount;
    }

    public void setConversionCount(Long conversionCount) {
        this.conversionCount = conversionCount;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
