package com.app.campaign.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@Table(name = "conversions")
public class Conversion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conversion_id", nullable = false)
    private Long conversionId;

    @Column(name = "click_id", nullable = false)
    private Long clickId;

    @Column(name = "revenue", nullable = false)
    private Double revenue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "click_id", referencedColumnName = "click_id", insertable = false, updatable = false)
    private Click click;
}
