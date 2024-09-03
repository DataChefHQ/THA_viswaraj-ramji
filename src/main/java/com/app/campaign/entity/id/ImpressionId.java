package com.app.campaign.entity.id;

import jakarta.persistence.Column;
import java.io.Serializable;
import java.util.Objects;

public class ImpressionId implements Serializable {

    @Column(name = "banner_id")
    private Long bannerId;

    @Column(name = "campaign_id")
    private Long campaignId;

    // Constructors
    public ImpressionId() {
    }

    public ImpressionId(Long bannerId, Long campaignId) {
        this.bannerId = bannerId;
        this.campaignId = campaignId;
    }

    // Getters and Setters
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

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImpressionId that = (ImpressionId) o;
        return Objects.equals(bannerId, that.bannerId) && Objects.equals(campaignId, that.campaignId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bannerId, campaignId);
    }
}
