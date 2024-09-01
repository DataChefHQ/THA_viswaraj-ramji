package com.app.campaign.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BannerQualificationStrategyFactory {

    private final List<BannerQualificationStrategy> strategies;

    @Autowired
    public BannerQualificationStrategyFactory(List<BannerQualificationStrategy> strategies) {
        this.strategies = strategies;
    }

    public BannerQualificationStrategy getStrategy(long x) {
        return strategies.stream()
                .filter(strategy -> strategy.appliesTo(x))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No strategy found for value x: " + x));
    }
}
