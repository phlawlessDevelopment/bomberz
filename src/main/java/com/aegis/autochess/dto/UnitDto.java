package com.aegis.autochess.dto;

import java.util.List;

public record UnitDto(Long id, String name, int tier, int cost, List<String> traits) {
}
