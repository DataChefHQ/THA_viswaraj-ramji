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
    public static LocalDateTime[] getIntervalForQuery(LocalDateTime timestamp) {
        LocalDateTime startOfHour = timestamp.truncatedTo(ChronoUnit.HOURS);
        int minute = timestamp.getMinute();
        int intervalStartMinute = (minute / 3) * 3;  // Calculate the start minute of the 3-minute interval
        LocalDateTime startInterval = startOfHour.plusMinutes(intervalStartMinute);
        LocalDateTime endInterval = startInterval.plusMinutes(2);  // End of the interval is 2 minutes after the start

        return new LocalDateTime[]{startInterval, endInterval};
    }
}
