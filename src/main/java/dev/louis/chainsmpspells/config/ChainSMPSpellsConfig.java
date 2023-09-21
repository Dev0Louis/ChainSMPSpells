package dev.louis.chainsmpspells.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class ChainSMPSpellsConfig extends MidnightConfig {

    @Comment(category = "chainsmpspells") public static Comment title;


    ManaDirection manaDirection = ManaDirection.RIGHT;
    @Comment(category = "chainsmpspells")
    @Entry(min = 1L, max = 100L, isSlider = true)
    int spellCooldown = 10;
    //@Comment("Higher Values might Result in Lag")
    @Comment(category = "chainsmpspells")
    @Entry(min = 2, max = 20)
    int raycastScanPrecision = 2;
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
