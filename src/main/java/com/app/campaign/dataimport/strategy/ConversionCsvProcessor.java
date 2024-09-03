package com.app.campaign.dataimport.strategy;


import com.app.campaign.entity.Conversion;
import com.app.campaign.repo.ConversionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@Component
public class ConversionCsvProcessor implements CsvProcessor<Conversion> {

    private static final Logger LOGGER = Logger.getLogger(ConversionCsvProcessor.class.getName());

    @Autowired
    private ConversionRepository conversionRepository;

    @Autowired
    private ClickCsvProcessor clickCsvProcessor;

    @Override
    public Conversion process(String[] line, LocalDateTime timestamp) {
        if (line.length < 3) {
            LOGGER.warning("Skipping invalid conversion line: " + String.join(",", line));
            return null;
        }
        try {
            Long conversionId = Long.parseLong(line[0]);
            Long clickId = Long.parseLong(line[1]);
            Double revenue = Double.parseDouble(line[2]);

            if (!clickCsvProcessor.getValidClickIds().contains(clickId)) {
                LOGGER.warning("Skipping conversion with invalid click_id: " + clickId);
                return null;
            }

            Conversion conversion = new Conversion();
            conversion.setConversionId(conversionId);
            conversion.setClickId(clickId);
            conversion.setRevenue(revenue);
            conversion.setTimestamp(timestamp);
            return conversion;
        } catch (NumberFormatException e) {
            LOGGER.warning("Skipping invalid conversion data: " + String.join(",", line));
            return null;
        }
    }

    @Override
    public void saveAll(List<Conversion> entities) {
        conversionRepository.saveAll(entities);
    }
}

