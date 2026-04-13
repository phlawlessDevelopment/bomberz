package com.aegis.autochess.repository;

import com.aegis.autochess.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}