package com.app.campaign.repo;


import com.app.campaign.model.Impression;
import com.app.campaign.model.idmodel.ImpressionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImpressionRepository extends JpaRepository<Impression, ImpressionId> {
}

