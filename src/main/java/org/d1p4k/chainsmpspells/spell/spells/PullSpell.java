package org.d1p4k.chainsmpspells.spell.spells;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.d1p4k.nebula.spell.AbstractSpell;

import java.util.UUID;

import static org.d1p4k.chainsmpspells.ChainSMPSpells.server;

public class PullSpell extends AbstractSpell {

    public PullSpell(int cost) {
        super(cost);
    }
    public PullSpell() {
        this(3);
    }


    @Override
    public void cast(ServerPlayerEntity player) {
        throw new UnsupportedOperationException();
    }

    public void cast(ServerPlayerEntity player, UUID uuid) {
        if(check()) {
            var pulledPlayer = server.getPlayerManager().getPlayer(uuid);
            if(pulledPlayer == null)return;
            Vec3d velocity = player.getPos().subtract(pulledPlayer.getPos()).normalize();
            pulledPlayer.setVelocity(velocity);
            pulledPlayer.velocityModified = true;
        }
    }

    @Override
    public Identifier getID() {
        return new Identifier("chainsmpspells", "pull");
    }

    public boolean check() {
        return checkKnowledge() && checkMana();
    }
}
