package com.app.campaign.view;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class BannerCampaignSummaryKey implements Serializable {

    @Column(name = "banner_id")
    private Long bannerId;

    @Column(name = "campaign_id")
    private Long campaignId;
}