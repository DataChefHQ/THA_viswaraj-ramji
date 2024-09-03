package com.app.campaign.dataimport.strategy;


import java.time.LocalDateTime;
import java.util.List;

public interface CsvProcessor<T> {
    T process(String[] line, LocalDateTime timestamp);

    void saveAll(List<T> entities);
}
