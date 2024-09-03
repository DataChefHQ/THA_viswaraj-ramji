package com.app.campaign.dataimport.strategy;

import com.app.campaign.entity.Impression;
import com.app.campaign.repo.ImpressionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@Component
public class ImpressionCsvProcessor implements CsvProcessor<Impression> {

    private static final Logger LOGGER = Logger.getLogger(ImpressionCsvProcessor.class.getName());

    @Autowired
    private ImpressionRepository impressionRepository;

    @Override
    public Impression process(String[] line, LocalDateTime timestamp) {
        if (line.length < 2) {
            LOGGER.warning("Skipping invalid impression line: " + String.join(",", line));
            return null;
        }
        try {
            Long bannerId = Long.parseLong(line[0]);
            Long campaignId = Long.parseLong(line[1]);
            Impression impression = new Impression();
            impression.setCampaignId(campaignId);
            impression.setBannerId(bannerId);
            impression.setTimestamp(timestamp);
            return impression;
        } catch (NumberFormatException e) {
            LOGGER.warning("Skipping invalid impression data: " + String.join(",", line));
            return null;
        }
    }

    @Override
    public void saveAll(List<Impression> entities) {
        impressionRepository.saveAll(entities);
    }
}
