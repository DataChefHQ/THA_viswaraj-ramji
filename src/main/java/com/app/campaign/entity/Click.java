package com.app.campaign.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;  // Timestamp field to track when data is loaded
}
