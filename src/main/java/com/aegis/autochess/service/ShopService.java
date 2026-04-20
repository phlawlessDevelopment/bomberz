package com.aegis.autochess.service;

import com.aegis.autochess.config.GameConstants;
import com.aegis.autochess.dto.ShopOfferDto;
import com.aegis.autochess.dto.UnitDto;
import com.aegis.autochess.exception.InsufficientGoldException;
import com.aegis.autochess.exception.UnitNotFoundException;
import com.aegis.autochess.model.LevelTierProbability;
import com.aegis.autochess.model.Player;
import com.aegis.autochess.model.Unit;
import com.aegis.autochess.repository.LevelTierProbabilityRepository;
import com.aegis.autochess.repository.PlayerRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ShopService {

    private final PlayerRepository playerRepository;
    private final LevelTierProbabilityRepository probabilityRepository;
    private final GlobalUnitPool globalUnitPool;
    private final Random random = new Random();

    public ShopService(PlayerRepository playerRepository,
                       LevelTierProbabilityRepository probabilityRepository,
                       GlobalUnitPool globalUnitPool) {
        this.playerRepository = playerRepository;
        this.probabilityRepository = probabilityRepository;
        this.globalUnitPool = globalUnitPool;
    }

    public List<ShopOfferDto> rollShop(Long playerId) {
        Player player = playerRepository.findById(playerId)
            .orElseThrow(() -> new UnitNotFoundException("Player not found: " + playerId));

        if (player.getGold() < GameConstants.ROLL_COST) {
            throw new InsufficientGoldException("Not enough gold to roll. Need " 
                + GameConstants.ROLL_COST + ", have " + player.getGold());
        }

        player.setGold(player.getGold() - GameConstants.ROLL_COST);
        playerRepository.save(player);

        List<LevelTierProbability> probabilities = 
            probabilityRepository.findByPlayerLevel(player.getLevel());

        return generateOffers(probabilities);
    }

    private List<ShopOfferDto> generateOffers(List<LevelTierProbability> probabilities) {
        List<ShopOfferDto> offers = new java.util.ArrayList<>();

        for (int slotIndex = 0; slotIndex < GameConstants.SHOP_SIZE; slotIndex++) {
            int tier = rollTier(probabilities);
            List<Unit> availableUnits = globalUnitPool.getAvailableUnitsByTier(tier);

            if (availableUnits.isEmpty()) {
                if (tier > 1) {
                    availableUnits = globalUnitPool.getAvailableUnitsByTier(tier - 1);
                }
                if (availableUnits.isEmpty() && tier < 5) {
                    availableUnits = globalUnitPool.getAvailableUnitsByTier(tier + 1);
                }
            }

            if (!availableUnits.isEmpty()) {
                Unit unit = availableUnits.get(random.nextInt(availableUnits.size()));
                offers.add(new ShopOfferDto(slotIndex, mapToDto(unit), unit.getCost(), false));
            }
        }

        return offers;
    }

    private int rollTier(List<LevelTierProbability> probabilities) {
        int totalWeight = probabilities.stream()
            .mapToInt(LevelTierProbability::getWeight)
            .sum();

        int roll = random.nextInt(totalWeight);

        for (LevelTierProbability prob : probabilities) {
            roll -= prob.getWeight();
            if (roll < 0) {
                return prob.getTier();
            }
        }

        return probabilities.get(0).getTier();
    }

    private UnitDto mapToDto(Unit unit) {
        List<String> traitsList = Arrays.asList(unit.getTraits().split(","));
        return new UnitDto(unit.getId(), unit.getName(), unit.getTier(), unit.getCost(), traitsList);
    }
}