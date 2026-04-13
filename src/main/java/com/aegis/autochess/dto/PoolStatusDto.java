package com.aegis.autochess.dto;

import java.util.List;
import java.util.Map;

public record PoolStatusDto(Map<Integer, List<PoolUnitEntry>> unitsByTier) {

    public record PoolUnitEntry(Long unitId, String unitName, int availableCount, int maxCount) {
    }
}
