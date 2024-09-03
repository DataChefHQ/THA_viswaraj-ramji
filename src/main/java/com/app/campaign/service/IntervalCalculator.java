package com.app.campaign.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class IntervalCalculator {

    /**
     * Calculates the start and end time of the 15-minute interval for a given timestamp.
     *
     * @param timestamp the current timestamp
     * @return an array containing the start and end time of the 15-minute interval
     */
    public static LocalDateTime[] getIntervalForQuery(LocalDateTime timestamp) {
        LocalDateTime startOfHour = timestamp.truncatedTo(ChronoUnit.HOURS);
        int minute = timestamp.getMinute();
        LocalDateTime startInterval, endInterval;

        if (minute >= 0 && minute < 15) {
            startInterval = startOfHour;
            endInterval = startOfHour.plusMinutes(14);
        } else if (minute >= 15 && minute < 30) {
            startInterval = startOfHour.plusMinutes(15);
            endInterval = startOfHour.plusMinutes(29);
        } else if (minute >= 30 && minute < 45) {
            startInterval = startOfHour.plusMinutes(30);
            endInterval = startOfHour.plusMinutes(44);
        } else {
            startInterval = startOfHour.plusMinutes(45);
            endInterval = startOfHour.plusMinutes(59);
        }

        return new LocalDateTime[]{startInterval, endInterval};
    }
}
