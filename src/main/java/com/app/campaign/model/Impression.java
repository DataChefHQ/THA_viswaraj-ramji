package com.app.campaign.model;

import com.app.campaign.model.idmodel.ImpressionId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


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

    @OneToMany(mappedBy = "impression")
    private List<Click> clicks;
}

