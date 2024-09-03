package com.app.campaign.repo;


import com.app.campaign.entity.Click;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClickRepository extends JpaRepository<Click, Long> {
}