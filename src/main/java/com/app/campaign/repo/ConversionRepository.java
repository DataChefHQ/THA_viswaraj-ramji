package com.app.campaign.repo;


import com.app.campaign.entity.Conversion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversionRepository extends JpaRepository<Conversion, Long> {
}
