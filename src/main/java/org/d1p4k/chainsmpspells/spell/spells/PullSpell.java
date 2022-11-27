package org.d1p4k.chainsmpspells.spell.spells;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.d1p4k.nebula.spell.AbstractSpell;

import java.util.UUID;

import static org.d1p4k.chainsmpspells.ChainSMPSpells.server;

public class PullSpell extends AbstractSpell {
    public UUID uuid;
    public static Identifier spellId = new Identifier("css" , "pull");

    public PullSpell(ServerPlayerEntity player, UUID uuid, Identifier spellIdentifier, int cost) {
        super(player, spellIdentifier, cost);
        this.uuid = uuid;
    }
    public PullSpell(ServerPlayerEntity player, UUID uuid, Identifier spellIdentifier) {
        this(player, uuid, spellIdentifier, 3);
    }


    @Override
    public void cast() {
        if(check()) {
            var pulledPlayer = server.getPlayerManager().getPlayer(uuid);
            if(pulledPlayer == null)return;
            Vec3d velocity = player.getPos().subtract(pulledPlayer.getPos()).normalize();
            pulledPlayer.setVelocity(velocity);
            pulledPlayer.velocityModified = true;
        }
    }
    public boolean check() {
        return checkKnowledge() && checkMana();
    }
}
