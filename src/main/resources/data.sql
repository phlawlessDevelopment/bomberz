-- Seed data for Aegis Auto Chess

-- Tier 1 units (cost 1)
INSERT OR IGNORE INTO units (id, name, tier, cost, traits) VALUES (1, 'Goblin Scout', 1, 1, 'Warrior');
INSERT OR IGNORE INTO units (id, name, tier, cost, traits) VALUES (2, 'Flame Imp', 1, 1, 'Mage');
INSERT OR IGNORE INTO units (id, name, tier, cost, traits) VALUES (3, 'Shadow Rat', 1, 1, 'Assassin');
INSERT OR IGNORE INTO units (id, name, tier, cost, traits) VALUES (4, 'Wind Sprite', 1, 1, 'Ranger');

-- Tier 2 units (cost 2)
INSERT OR IGNORE INTO units (id, name, tier, cost, traits) VALUES (5, 'Iron Golem', 2, 2, 'Warrior');
INSERT OR IGNORE INTO units (id, name, tier, cost, traits) VALUES (6, 'Frost Witch', 2, 2, 'Mage');
INSERT OR IGNORE INTO units (id, name, tier, cost, traits) VALUES (7, 'Venom Blade', 2, 2, 'Assassin');
INSERT OR IGNORE INTO units (id, name, tier, cost, traits) VALUES (8, 'Storm Archer', 2, 2, 'Ranger');

-- Tier 3 units (cost 3)
INSERT OR IGNORE INTO units (id, name, tier, cost, traits) VALUES (9, 'War Elephant', 3, 3, 'Warrior');
INSERT OR IGNORE INTO units (id, name, tier, cost, traits) VALUES (10, 'Arcane Sage', 3, 3, 'Mage');
INSERT OR IGNORE INTO units (id, name, tier, cost, traits) VALUES (11, 'Phantom Thief', 3, 3, 'Assassin');
INSERT OR IGNORE INTO units (id, name, tier, cost, traits) VALUES (12, 'Eagle Eye', 3, 3, 'Ranger');

-- Tier 4 units (cost 4)
INSERT OR IGNORE INTO units (id, name, tier, cost, traits) VALUES (13, 'Dragon Knight', 4, 4, 'Warrior,Mage');
INSERT OR IGNORE INTO units (id, name, tier, cost, traits) VALUES (14, 'Death Stalker', 4, 4, 'Assassin');
INSERT OR IGNORE INTO units (id, name, tier, cost, traits) VALUES (15, 'Thunder Lord', 4, 4, 'Mage,Ranger');

-- Tier 5 units (cost 5)
INSERT OR IGNORE INTO units (id, name, tier, cost, traits) VALUES (16, 'Ancient Titan', 5, 5, 'Warrior');
INSERT OR IGNORE INTO units (id, name, tier, cost, traits) VALUES (17, 'Archmage Supreme', 5, 5, 'Mage');
INSERT OR IGNORE INTO units (id, name, tier, cost, traits) VALUES (18, 'Shadow Sovereign', 5, 5, 'Assassin');

-- Level-to-tier probability matrix (levels 1-9)
-- Level 1
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (1, 1, 1, 100);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (2, 1, 2, 0);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (3, 1, 3, 0);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (4, 1, 4, 0);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (5, 1, 5, 0);

-- Level 2
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (6, 2, 1, 100);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (7, 2, 2, 0);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (8, 2, 3, 0);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (9, 2, 4, 0);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (10, 2, 5, 0);

-- Level 3
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (11, 3, 1, 75);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (12, 3, 2, 25);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (13, 3, 3, 0);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (14, 3, 4, 0);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (15, 3, 5, 0);

-- Level 4
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (16, 4, 1, 55);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (17, 4, 2, 30);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (18, 4, 3, 15);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (19, 4, 4, 0);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (20, 4, 5, 0);

-- Level 5
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (21, 5, 1, 45);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (22, 5, 2, 33);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (23, 5, 3, 20);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (24, 5, 4, 2);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (25, 5, 5, 0);

-- Level 6
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (26, 6, 1, 25);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (27, 6, 2, 40);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (28, 6, 3, 30);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (29, 6, 4, 5);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (30, 6, 5, 0);

-- Level 7
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (31, 7, 1, 19);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (32, 7, 2, 30);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (33, 7, 3, 35);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (34, 7, 4, 15);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (35, 7, 5, 1);

-- Level 8
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (36, 8, 1, 16);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (37, 8, 2, 20);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (38, 8, 3, 35);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (39, 8, 4, 25);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (40, 8, 5, 4);

-- Level 9
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (41, 9, 1, 9);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (42, 9, 2, 15);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (43, 9, 3, 30);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (44, 9, 4, 30);
INSERT OR IGNORE INTO level_tier_probabilities (id, player_level, tier, weight) VALUES (45, 9, 5, 16);

-- Test player
INSERT OR IGNORE INTO players (id, name, gold, level) VALUES (1, 'TestPlayer', 50, 5);