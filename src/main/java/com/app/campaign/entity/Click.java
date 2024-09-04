package com.app.campaign.entity;

import com.app.campaign.entity.id.ClickId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "clicks")
@IdClass(ClickId.class)
public class Click {

    @Id
    @Column(name = "click_id", nullable = false)
    private Long clickId;

    @Id
    @Column(name = "banner_id", nullable = false)
    private Long bannerId;

    @Id
    @Column(name = "campaign_id", nullable = false)
    private Long campaignId;

    @Id  // Include timestamp as part of the composite primary key
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;  // Timestamp field to track when data is loaded
}
