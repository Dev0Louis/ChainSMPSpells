package org.d1p4k.chainsmpspells.spell.spells;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.d1p4k.nebula.registry.NebulaRegistries;
import org.d1p4k.nebula.spell.AbstractSpell;

public class SuicideSpell extends AbstractSpell {
    public SuicideSpell(int cost) {
        super(cost);
    }
    public SuicideSpell() {
        this(1);
    }
    @Override
    public void cast() {
        if(check()) {
            PlayerEntity playerEntity = (PlayerEntity) player;
            playerEntity.kill();
        }
    }

    @Override
    public Identifier getID() {
        return NebulaRegistries.SPELLS.getId(this);
    }

    public boolean check() {
        return checkKnowledge() && checkMana();
    }
}
