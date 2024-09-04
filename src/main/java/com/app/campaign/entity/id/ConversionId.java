package com.app.campaign.entity.id;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
public class ConversionId implements Serializable {

    private Long conversionId;
    private Long clickId;
    private LocalDateTime timestamp;  // Include timestamp as part of the primary key

    // Parameterized constructor
    public ConversionId(Long conversionId, Long clickId, LocalDateTime timestamp) {
        this.conversionId = conversionId;
        this.clickId = clickId;
        this.timestamp = timestamp;
    }

    // Override equals method for composite key comparisons
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversionId that = (ConversionId) o;
        return Objects.equals(conversionId, that.conversionId) &&
                Objects.equals(clickId, that.clickId) &&
                Objects.equals(timestamp, that.timestamp);
    }

    // Override hashCode method for composite key comparisons
    @Override
    public int hashCode() {
        return Objects.hash(conversionId, clickId, timestamp);
    }
}

