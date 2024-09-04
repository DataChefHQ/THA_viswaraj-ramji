package com.app.campaign.entity;
import com.app.campaign.entity.id.ConversionId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "conversions")
@IdClass(ConversionId.class)
public class Conversion {

    @Id
    @Column(name = "conversion_id", nullable = false)
    private Long conversionId;

    @Id
    @Column(name = "click_id", nullable = false)
    private Long clickId;

    @Id  // Include timestamp as part of the composite primary key
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;  // Timestamp field to track when data is loaded

    @Column(name = "revenue", nullable = false)
    private Double revenue;
}

