package com.app.campaign.service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class IntervalCalculator {

    /**
     * Calculates the start and end time of the 3-minute interval for a given timestamp.
     *
     * @param timestamp the current timestamp
     * @return an array containing the start and end time of the 3-minute interval
     */
    // Given a timestamp, calculate the rounded-off start date and end date with a 3-minute difference
    public static LocalDateTime[] getIntervalForQuery(LocalDateTime timestamp) {
        // Round down the timestamp to the nearest 3-minute interval
        LocalDateTime startDate = timestamp.truncatedTo(ChronoUnit.HOURS)
                .plusMinutes((timestamp.getMinute() / 15) * 15);

        // End date is 3 minutes after the start date
        LocalDateTime endDate = startDate.plusMinutes(15);

        return new LocalDateTime[]{startDate, endDate};
    }
}
