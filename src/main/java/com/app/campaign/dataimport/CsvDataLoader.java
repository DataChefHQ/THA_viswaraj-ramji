package com.app.campaign.dataimport;


import com.app.campaign.dataimport.strategy.ClickCsvProcessor;
import com.app.campaign.dataimport.strategy.ConversionCsvProcessor;
import com.app.campaign.dataimport.strategy.ImpressionCsvProcessor;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class CsvDataLoader {

    private static final Logger LOGGER = Logger.getLogger(CsvDataLoader.class.getName());
    private static final String BASE_PATH = "src/main/resources/dataset/csv/";

    @Autowired
    private CsvFileProcessor fileProcessor;

    @Autowired
    private ImpressionCsvProcessor impressionCsvProcessor;

    @Autowired
    private ClickCsvProcessor clickCsvProcessor;

    @Autowired
    private ConversionCsvProcessor conversionCsvProcessor;

    @PostConstruct
    public void loadData() {
        try {
            // Initialize the starting timestamp
            LocalDateTime initialTimestamp = LocalDateTime.now();

            // Atomic reference to hold the timestamp for increment within lambda
            final AtomicReference<LocalDateTime> timestamp = new AtomicReference<>(initialTimestamp);

            // Use lambda to process each directory and increment timestamp by 15 minutes
            Files.list(Paths.get(BASE_PATH))
                    .filter(Files::isDirectory)
                    .forEach(directory -> {
                        String folderName = directory.getFileName().toString();
                        try {
                            int folderNumber = Integer.parseInt(folderName);
                            // Load folder data with the current timestamp
                            loadFolderData(folderNumber, timestamp.get());
                            // Increment the timestamp by 15 minutes
                            timestamp.set(timestamp.get().plusMinutes(3));
                        } catch (NumberFormatException e) {
                            LOGGER.warning("Skipping invalid folder name: " + folderName);
                        }
                    });
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading data from CSV directories", e);
        }
    }



    private void loadFolderData(int folderNumber, LocalDateTime timestamp) {
        fileProcessor.processFile("impressions", folderNumber, timestamp, impressionCsvProcessor);
        fileProcessor.processFile("clicks", folderNumber, timestamp, clickCsvProcessor);
        fileProcessor.processFile("conversions", folderNumber, timestamp, conversionCsvProcessor);
    }
}
