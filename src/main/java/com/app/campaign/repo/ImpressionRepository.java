package com.app.campaign.repo;


import com.app.campaign.entity.Impression;
import com.app.campaign.entity.id.ImpressionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImpressionRepository extends JpaRepository<Impression, ImpressionId> {
}

