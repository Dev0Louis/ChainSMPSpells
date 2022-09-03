package org.d1p4k.chainsmpspells.spell.spells;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.d1p4k.nebula.api.NebulaPlayer;
import org.d1p4k.nebula.spell.AbstractSpell;

public class SuicideSpell extends AbstractSpell {

    public static Identifier spellId = new Identifier("css" , "suicide");
    public SuicideSpell(PlayerEntity player, Identifier spellIdentifier, int cost) {
        super(player, spellIdentifier, cost);
    }

    @Override
    public void cast() {
        if(check()) {
            PlayerEntity playerEntity = (PlayerEntity) player;
            playerEntity.kill();
        }
    }

    public boolean check() {
        return checkKnowledge() && checkMana();
    }
}
