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
@Table(name = "conversions")
public class Conversion {

    @Id
    @Column(name = "conversion_id", nullable = false)
    private Long conversionId;

    @Column(name = "click_id", nullable = false)
    private Long clickId;

    @Column(name = "revenue", nullable = false)
    private Double revenue;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;  // Timestamp field to track when data is loaded
}
