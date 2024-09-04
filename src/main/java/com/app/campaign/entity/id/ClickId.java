package com.app.campaign.entity.id;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
public class ClickId implements Serializable {

    private Long clickId;
    private Long bannerId;
    private Long campaignId;
    private LocalDateTime timestamp;  // Include timestamp as part of the primary key

    // Parameterized constructor
    public ClickId(Long clickId, Long bannerId, Long campaignId, LocalDateTime timestamp) {
        this.clickId = clickId;
        this.bannerId = bannerId;
        this.campaignId = campaignId;
        this.timestamp = timestamp;
    }

    // Override equals method for composite key comparisons
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClickId clickId = (ClickId) o;
        return Objects.equals(this.clickId, clickId.clickId) &&
                Objects.equals(bannerId, clickId.bannerId) &&
                Objects.equals(campaignId, clickId.campaignId) &&
                Objects.equals(timestamp, clickId.timestamp);
    }

    // Override hashCode method for composite key comparisons
    @Override
    public int hashCode() {
        return Objects.hash(clickId, bannerId, campaignId, timestamp);
    }
}
