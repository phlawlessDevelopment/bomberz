package com.aegis.autochess.config;

/**
 * Game constants for the auto-battler game.
 * Provides named constants for tier copy limits, shop settings, and player level bounds.
 */
public final class GameConstants {

    private GameConstants() {
        // Prevent instantiation - utility class
    }

    // Tier copy limits
    public static final int TIER_1_COPIES = 29;
    public static final int TIER_2_COPIES = 22;
    public static final int TIER_3_COPIES = 18;
    public static final int TIER_4_COPIES = 12;
    public static final int TIER_5_COPIES = 10;

    // Shop settings
    public static final int SHOP_SIZE = 5;
    public static final int ROLL_COST = 2;

    // Player level bounds
    public static final int MIN_PLAYER_LEVEL = 1;
    public static final int MAX_PLAYER_LEVEL = 9;

    // Starting resources
    public static final int STARTING_GOLD = 0;

    /**
     * Returns the copy limit for a given tier.
     *
     * @param tier the tier number (1-5)
     * @return the maximum number of copies allowed for that tier
     * @throws IllegalArgumentException if tier is not in range 1-5
     */
    public static int getCopiesForTier(int tier) {
        return switch (tier) {
            case 1 -> TIER_1_COPIES;
            case 2 -> TIER_2_COPIES;
            case 3 -> TIER_3_COPIES;
            case 4 -> TIER_4_COPIES;
            case 5 -> TIER_5_COPIES;
            default -> throw new IllegalArgumentException("Invalid tier: " + tier + ". Must be between 1 and 5.");
        };
    }
}