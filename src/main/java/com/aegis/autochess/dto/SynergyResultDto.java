package com.aegis.autochess.dto;

public record SynergyResultDto(String synergyName, int activeUnits, int threshold, 
                               String bonusType, int bonusValue, boolean active) {
}
