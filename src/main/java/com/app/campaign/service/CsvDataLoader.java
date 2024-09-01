package com.app.campaign.service;

import com.app.campaign.model.Click;
import com.app.campaign.model.Conversion;
import com.app.campaign.model.Impression;
import com.app.campaign.repo.ClickRepository;
import com.app.campaign.repo.ConversionRepository;
import com.app.campaign.repo.ImpressionRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class CsvDataLoader {

    private static final Logger LOGGER = Logger.getLogger(CsvDataLoader.class.getName());

    @Autowired
    private ImpressionRepository impressionRepository;

    @Autowired
    private ClickRepository clickRepository;

    @Autowired
    private ConversionRepository conversionRepository;

    // To keep track of valid click IDs
    private Set<Long> validClickIds = new HashSet<>();

    @PostConstruct
    public void loadData() {
        try {
            loadImpressions();
            loadClicks();
            loadConversions();
        } catch (CsvValidationException | IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading data from CSV files", e);
        }
    }

    private void loadImpressions() throws CsvValidationException, IOException {
        String filePath = "src/main/resources/dataset/csv/1/impressions_1.csv"; // Update path as necessary
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<Impression> impressions = new ArrayList<>();
            String[] line;
            reader.readNext(); // Skip header
            while ((line = reader.readNext()) != null) {
                if (line.length < 2) {
                    LOGGER.warning("Skipping invalid impression line: " + String.join(",", line));
                    continue;
                }
                try {
                    Long bannerId = Long.parseLong(line[0]);
                    Long campaignId = Long.parseLong(line[1]);
                    Impression impression = new Impression();
                    impression.setCampaignId(campaignId);
                    impression.setBannerId(bannerId);
                    impressions.add(impression);
                } catch (NumberFormatException e) {
                    LOGGER.warning("Skipping invalid impression data: " + String.join(",", line));
                }
            }
            impressionRepository.saveAll(impressions);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading impressions CSV", e);
            throw e;
        }
    }

    private void loadClicks() throws CsvValidationException, IOException {
        String filePath = "src/main/resources/dataset/csv/1/clicks_1.csv"; // Update path as necessary
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<Click> clicks = new ArrayList<>();
            String[] line;
            reader.readNext(); // Skip header
            while ((line = reader.readNext()) != null) {
                if (line.length < 3) {
                    LOGGER.warning("Skipping invalid click line: " + String.join(",", line));
                    continue;
                }
                try {
                    Long clickId = Long.parseLong(line[0]);
                    Long bannerId = Long.parseLong(line[1]);
                    Long campaignId = Long.parseLong(line[2]);
                    Click click = new Click();
                    click.setClickId(clickId);
                    click.setBannerId(bannerId);
                    click.setCampaignId(campaignId);
                    clicks.add(click);
                    validClickIds.add(clickId); // Track valid click IDs for conversions
                } catch (NumberFormatException e) {
                    LOGGER.warning("Skipping invalid click data: " + String.join(",", line));
                }
            }
            clickRepository.saveAll(clicks);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading clicks CSV", e);
            throw e;
        }
    }

    private void loadConversions() throws CsvValidationException, IOException {
        String filePath = "src/main/resources/dataset/csv/1/conversions_1.csv"; // Update path as necessary
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<Conversion> conversions = new ArrayList<>();
            String[] line;
            reader.readNext(); // Skip header
            while ((line = reader.readNext()) != null) {
                if (line.length < 3) {
                    LOGGER.warning("Skipping invalid conversion line: " + String.join(",", line));
                    continue;
                }
                try {
                    Long conversionId = Long.parseLong(line[0]);
                    Long clickId = Long.parseLong(line[1]);
                    Double revenue = Double.parseDouble(line[2]);

                    // Ensure that the clickId exists in the validClickIds set
                    if (!validClickIds.contains(clickId)) {
                        LOGGER.warning("Skipping conversion with invalid click_id: " + clickId);
                        continue;
                    }

                    Conversion conversion = new Conversion();
                    conversion.setConversionId(conversionId);
                    conversion.setClickId(clickId);
                    conversion.setRevenue(revenue);
                    conversions.add(conversion);
                } catch (NumberFormatException e) {
                    LOGGER.warning("Skipping invalid conversion data: " + String.join(",", line));
                }
            }
            conversionRepository.saveAll(conversions);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading conversions CSV", e);
            throw e;
        }
    }
}
