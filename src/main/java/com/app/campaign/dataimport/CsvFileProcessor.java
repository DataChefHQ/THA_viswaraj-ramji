package com.app.campaign.dataimport;

import com.app.campaign.dataimport.strategy.CsvProcessor;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class CsvFileProcessor {

    private static final Logger LOGGER = Logger.getLogger(CsvFileProcessor.class.getName());
    private static final String BASE_PATH = "src/main/resources/dataset/csv/";

    public <T> void processFile(String fileType, int folderNumber, LocalDateTime timestamp, CsvProcessor<T> processor) {
        String filePath = String.format("%s%d/%s_%d.csv", BASE_PATH, folderNumber, fileType, folderNumber);
        File file = new File(filePath);
        if (!file.exists()) {
            LOGGER.warning("File not found: " + filePath);
            return;
        }

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            List<T> entities = new ArrayList<>();
            String[] line;
            reader.readNext(); // Skip header
            while ((line = reader.readNext()) != null) {
                T entity = processor.process(line, timestamp);
                if (entity != null) {
                    entities.add(entity);
                }
            }
            processor.saveAll(entities);
        } catch (IOException | CsvValidationException e) {
            LOGGER.log(Level.SEVERE, "Error reading CSV: " + filePath, e);
        }
    }
}
