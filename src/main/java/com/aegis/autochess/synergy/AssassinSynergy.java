package com.aegis.autochess.synergy;

import org.springframework.stereotype.Component;

@Component
public class AssassinSynergy implements SynergyStrategy {
    
    private static final String BONUS_TYPE = "critical_strike";
    private static final int THRESHOLD_SIX = 6;
    private static final int THRESHOLD_FOUR = 4;
    private static final int THRESHOLD_TWO = 2;
    private static final int BONUS_SIX = 40;
    private static final int BONUS_FOUR = 25;
    private static final int BONUS_TWO = 10;
    
    @Override
    public String getTraitName() {
        return "Assassin";
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
