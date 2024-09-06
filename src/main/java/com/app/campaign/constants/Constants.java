package com.app.campaign.constants;


import org.springframework.http.HttpHeaders;

public class Constants {
    // URL of the file server
    public static final String FILE_SERVER_URL = "http://localhost/files/";

    // HTTP header names
    public static final String HEADER_LOCATION = HttpHeaders.LOCATION; // Using Spring's HttpHeaders for standard headers
    public static final String HEADER_REASON = "X-Reason"; // Custom header for specific messages

    // Reasons or messages used in the application
    public static final String REASON_BANNER_NOT_PRESENT = "Banner not present";
}
