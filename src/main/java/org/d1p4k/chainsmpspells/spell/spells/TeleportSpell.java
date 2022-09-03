package org.d1p4k.chainsmpspells.spell.spells;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.d1p4k.nebula.spell.AbstractSpell;

public class TeleportSpell extends AbstractSpell {
    public TeleportSpell(PlayerEntity player, Identifier spellIdentifier, int cost) {
        super(player, spellIdentifier, cost);
    }

    @Override
    public void cast() {

    }
}
