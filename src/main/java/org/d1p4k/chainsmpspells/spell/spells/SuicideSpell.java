package org.d1p4k.chainsmpspells.spell.spells;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.d1p4k.nebula.spell.AbstractSpell;

public class SuicideSpell extends AbstractSpell {

    public static Identifier spellId = new Identifier("css" , "suicide");
    public SuicideSpell(ServerPlayerEntity player, Identifier spellIdentifier, int cost) {
        super(player, spellIdentifier, cost);
    }
    public SuicideSpell(ServerPlayerEntity player, Identifier spellIdentifier) {
        this(player, spellIdentifier, 1);
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
