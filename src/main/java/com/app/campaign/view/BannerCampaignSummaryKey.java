package com.app.campaign.view;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BannerCampaignSummaryKey implements Serializable {

    @Column(name = "banner_id")
    private Long bannerId;

    @Column(name = "campaign_id")
    private Long campaignId;

    // Constructors, getters, setters, equals, and hashCode methods

    public BannerCampaignSummaryKey() {}

    public BannerCampaignSummaryKey(Long bannerId, Long campaignId) {
        this.bannerId = bannerId;
        this.campaignId = campaignId;
    }

    public Long getBannerId() {
        return bannerId;
    }

    public void setBannerId(Long bannerId) {
        this.bannerId = bannerId;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BannerCampaignSummaryKey that = (BannerCampaignSummaryKey) o;
        return Objects.equals(bannerId, that.bannerId) &&
                Objects.equals(campaignId, that.campaignId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bannerId, campaignId);
    }
}
