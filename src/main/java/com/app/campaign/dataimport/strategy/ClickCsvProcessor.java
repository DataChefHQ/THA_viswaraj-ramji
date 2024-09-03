package com.app.campaign.dataimport.strategy;


import com.app.campaign.entity.Click;
import com.app.campaign.repo.ClickRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Component
public class ClickCsvProcessor implements CsvProcessor<Click> {

    private static final Logger LOGGER = Logger.getLogger(ClickCsvProcessor.class.getName());

    @Autowired
    private ClickRepository clickRepository;

    private Set<Long> validClickIds = new HashSet<>();

    @Override
    public Click process(String[] line, LocalDateTime timestamp) {
        if (line.length < 3) {
            LOGGER.warning("Skipping invalid click line: " + String.join(",", line));
            return null;
        }
        try {
            Long clickId = Long.parseLong(line[0]);
            Long bannerId = Long.parseLong(line[1]);
            Long campaignId = Long.parseLong(line[2]);
            Click click = new Click();
            click.setClickId(clickId);
            click.setBannerId(bannerId);
            click.setCampaignId(campaignId);
            click.setTimestamp(timestamp);
            validClickIds.add(clickId);
            return click;
        } catch (NumberFormatException e) {
            LOGGER.warning("Skipping invalid click data: " + String.join(",", line));
            return null;
        }
    }

    @Override
    public void saveAll(List<Click> entities) {
        clickRepository.saveAll(entities);
    }

    public Set<Long> getValidClickIds() {
        return validClickIds;
    }
}
