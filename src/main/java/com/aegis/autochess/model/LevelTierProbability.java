package com.aegis.autochess.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "level_tier_probabilities")
public class LevelTierProbability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "player_level", nullable = false)
    private int playerLevel;

    @Column(nullable = false)
    private int tier;

    @Column(nullable = false)
    private int weight;

    public LevelTierProbability() {
    }

    public LevelTierProbability(Long id, int playerLevel, int tier, int weight) {
        this.id = id;
        this.playerLevel = playerLevel;
        this.tier = tier;
        this.weight = weight;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPlayerLevel() {
        return playerLevel;
    }

    public void setPlayerLevel(int playerLevel) {
        this.playerLevel = playerLevel;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LevelTierProbability that = (LevelTierProbability) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "LevelTierProbability{id=" + id + ", playerLevel=" + playerLevel + 
               ", tier=" + tier + ", weight=" + weight + "}";
    }
}