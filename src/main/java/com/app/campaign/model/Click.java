package com.app.campaign.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "clicks")
public class Click {

    @Id
    @Column(name = "click_id", nullable = false)
    private Long clickId;

    @Column(name = "banner_id", nullable = false)
    private Long bannerId;

    @Column(name = "campaign_id", nullable = false)
    private Long campaignId;

}
