package dev.louis.chainsmpspells.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "ChainSMPSpells")
public class ChainSMPSpellsConfig implements ConfigData {
    ManaDirection manaDirection = ManaDirection.RIGHT;
    @ConfigEntry.BoundedDiscrete(min = 1L, max = 100L)
    @Comment("In Ticks")
    int spellCooldown = 10;
    @ConfigEntry.BoundedDiscrete(min = 2, max = 20)
    @Comment("Higher Values might Result in Lag")
    int raycastScanPrecision = 2;
    public ManaDirection getManaDirection() {
        return manaDirection;
    }

    public boolean isStandardManaDirection() {
        return (getManaDirection() == ManaDirection.RIGHT);
    }
    enum ManaDirection {
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
