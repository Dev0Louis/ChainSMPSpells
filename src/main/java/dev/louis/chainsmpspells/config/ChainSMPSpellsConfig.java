package dev.louis.chainsmpspells.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class ChainSMPSpellsConfig extends MidnightConfig {

    //@Comment(category = "chainsmpspells") public static Comment title;

    @Entry
    public static ManaDirection manaDirection = ManaDirection.RIGHT;
    //@Comment(category = "chainsmpspells")
    @Entry(min = 1L, max = 100L)
    public static int spellCooldown = 10;
    //@Comment("Higher Values might Result in Lag")
    @Entry(min = 2, max = 20)
    public static int raycastScanPrecision = 2;
    public ManaDirection getManaDirection() {
        return manaDirection;
    }

    public enum ManaDirection {
        LEFT,
        RIGHT
    }


    public int getSpellCooldown() {
        return spellCooldown;
    }

    public int getRaycastScanPrecision() {
        return raycastScanPrecision;
    }
}
