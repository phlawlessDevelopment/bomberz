package com.aegis.autochess.repository;

import com.aegis.autochess.model.LevelTierProbability;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LevelTierProbabilityRepository extends JpaRepository<LevelTierProbability, Long> {

    List<LevelTierProbability> findByPlayerLevel(int playerLevel);
}