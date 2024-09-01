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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "click_id", nullable = false)
    private Long clickId;

    @Column(name = "banner_id", nullable = false)
    private Long bannerId;

    @Column(name = "campaign_id", nullable = false)
    private Long campaignId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banner_id", referencedColumnName = "banner_id", insertable = false, updatable = false)
    @JoinColumn(name = "campaign_id", referencedColumnName = "campaign_id", insertable = false, updatable = false)
    private Impression impression;

    @OneToMany(mappedBy = "click", fetch = FetchType.LAZY)
    private List<Conversion> conversions;
}
