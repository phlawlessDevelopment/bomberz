package com.aegis.autochess.synergy;

public interface SynergyStrategy {
    String getTraitName();
    
    SynergyBonus calculateBonus(int unitCount);
}
