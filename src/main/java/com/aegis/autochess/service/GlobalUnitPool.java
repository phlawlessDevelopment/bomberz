package com.aegis.autochess.service;

import com.aegis.autochess.config.GameConstants;
import com.aegis.autochess.model.Unit;
import com.aegis.autochess.repository.UnitRepository;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class GlobalUnitPool {

    private final ConcurrentHashMap<Long, AtomicInteger> availableCounts;
    private final Map<Long, Integer> maxCounts;
    private final Map<Long, Unit> unitLookup;

    public GlobalUnitPool(UnitRepository unitRepository) {
        this.availableCounts = new ConcurrentHashMap<>();
        this.unitLookup = new ConcurrentHashMap<>();

        List<Unit> units = unitRepository.findAll();
        Map<Long, Integer> tempMaxCounts = new ConcurrentHashMap<>();

        for (Unit unit : units) {
            Long unitId = unit.getId();
            int maxCopies = GameConstants.getCopiesForTier(unit.getTier());

            tempMaxCounts.put(unitId, maxCopies);
            availableCounts.put(unitId, new AtomicInteger(maxCopies));
            unitLookup.put(unitId, unit);
        }

        this.maxCounts = Map.copyOf(tempMaxCounts);
    }

    public boolean acquire(Long unitId) {
        AtomicInteger counter = availableCounts.get(unitId);
        if (counter == null) {
            return false;
        }

        while (true) {
            int current = counter.get();
            if (current <= 0) {
                return false;
            }
            if (counter.compareAndSet(current, current - 1)) {
                return true;
            }
        }
    }

    public void release(Long unitId) {
        AtomicInteger counter = availableCounts.get(unitId);
        Integer max = maxCounts.get(unitId);
        if (counter == null || max == null) {
            return;
        }

        while (true) {
            int current = counter.get();
            if (current >= max) {
                return;
            }
            if (counter.compareAndSet(current, current + 1)) {
                return;
            }
        }
    }

    public int getAvailableCount(Long unitId) {
        AtomicInteger counter = availableCounts.get(unitId);
        return counter != null ? counter.get() : 0;
    }

    public int getMaxCount(Long unitId) {
        Integer max = maxCounts.get(unitId);
        return max != null ? max : 0;
    }

    public Unit getUnit(Long unitId) {
        return unitLookup.get(unitId);
    }

    public List<Unit> getAvailableUnitsByTier(int tier) {
        return unitLookup.values().stream()
            .filter(u -> u.getTier() == tier)
            .filter(u -> getAvailableCount(u.getId()) > 0)
            .collect(Collectors.toList());
    }

    public Map<Integer, Map<Long, Integer>> getPoolStatus() {
        return availableCounts.entrySet().stream()
            .collect(Collectors.groupingBy(
                e -> unitLookup.get(e.getKey()).getTier(),
                Collectors.toMap(
                    Map.Entry::getKey,
                    e -> e.getValue().get()
                )
            ));
    }
}