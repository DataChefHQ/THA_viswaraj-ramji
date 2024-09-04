package com.app.campaign.entity;

import com.app.campaign.entity.id.ImpressionId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@Table(name = "impressions")
@IdClass(ImpressionId.class)
public class Impression {

    @Id
    @Column(name = "banner_id", nullable = false)
    private Long bannerId;

    @Id
    @Column(name = "campaign_id", nullable = false)
    private Long campaignId;

    @Id  // Add @Id annotation here
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;  // Timestamp field to track when data is loaded
}
