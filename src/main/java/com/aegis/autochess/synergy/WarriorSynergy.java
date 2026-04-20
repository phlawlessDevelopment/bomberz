package com.aegis.autochess.synergy;

import org.springframework.stereotype.Component;

@Component
public class WarriorSynergy implements SynergyStrategy {
    
    private static final String BONUS_TYPE = "armor";
    private static final int THRESHOLD_SIX = 6;
    private static final int THRESHOLD_FOUR = 4;
    private static final int THRESHOLD_TWO = 2;
    private static final int BONUS_SIX = 75;
    private static final int BONUS_FOUR = 45;
    private static final int BONUS_TWO = 20;
    
    @Override
    public String getTraitName() {
        return "Warrior";
    }
    
    @Override
    public SynergyBonus calculateBonus(int unitCount) {
        if (unitCount >= THRESHOLD_SIX) {
            return new SynergyBonus(BONUS_TYPE, BONUS_SIX, THRESHOLD_SIX);
        }
        if (unitCount >= THRESHOLD_FOUR) {
            return new SynergyBonus(BONUS_TYPE, BONUS_FOUR, THRESHOLD_FOUR);
        }
        if (unitCount >= THRESHOLD_TWO) {
            return new SynergyBonus(BONUS_TYPE, BONUS_TWO, THRESHOLD_TWO);
        }
        return null;
    }
}
