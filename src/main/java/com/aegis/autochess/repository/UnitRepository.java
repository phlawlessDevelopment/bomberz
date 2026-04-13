package com.aegis.autochess.repository;

import com.aegis.autochess.model.Unit;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitRepository extends JpaRepository<Unit, Long> {

    List<Unit> findByTier(int tier);
}