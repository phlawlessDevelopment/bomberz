package com.aegis.autochess.service;

import com.aegis.autochess.dto.UnitDto;
import com.aegis.autochess.exception.InsufficientGoldException;
import com.aegis.autochess.exception.UnitNotFoundException;
import com.aegis.autochess.exception.UnitPoolExhaustedException;
import com.aegis.autochess.model.Player;
import com.aegis.autochess.model.Unit;
import com.aegis.autochess.repository.PlayerRepository;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PurchaseService {

    private final PlayerRepository playerRepository;
    private final GlobalUnitPool globalUnitPool;

    public PurchaseService(PlayerRepository playerRepository, GlobalUnitPool globalUnitPool) {
        this.playerRepository = playerRepository;
        this.globalUnitPool = globalUnitPool;
    }

    public UnitDto purchaseUnit(Long playerId, Long unitId) {
        Player player = playerRepository.findById(playerId)
            .orElseThrow(() -> new UnitNotFoundException("Player not found: " + playerId));

        Unit unit = globalUnitPool.getUnit(unitId);
        if (unit == null) {
            throw new UnitNotFoundException("Unit not found: " + unitId);
        }

        if (player.getGold() < unit.getCost()) {
            throw new InsufficientGoldException("Not enough gold. Need " + unit.getCost() 
                + ", have " + player.getGold());
        }

        if (!globalUnitPool.acquire(unitId)) {
            throw new UnitPoolExhaustedException("Unit no longer available: " + unit.getName());
        }

        player.setGold(player.getGold() - unit.getCost());
        playerRepository.save(player);

        return mapToDto(unit);
    }

    private UnitDto mapToDto(Unit unit) {
        List<String> traitsList = Arrays.asList(unit.getTraits().split(","));
        return new UnitDto(unit.getId(), unit.getName(), unit.getTier(), unit.getCost(), traitsList);
    }
}