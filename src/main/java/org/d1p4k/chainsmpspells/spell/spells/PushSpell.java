package org.d1p4k.chainsmpspells.spell.spells;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.d1p4k.nebula.spell.AbstractSpell;

import java.util.UUID;

import static org.d1p4k.chainsmpspells.ChainSMPSpells.server;

public class PushSpell extends AbstractSpell {

    public UUID uuid;
    public static Identifier spellId = new Identifier("chainsmpspells" , "push");

    public PushSpell(ServerPlayerEntity player, UUID uuid, Identifier spellIdentifier, int cost) {
        super(player, spellIdentifier, cost);
        this.uuid = uuid;
    }
    public PushSpell(ServerPlayerEntity player, UUID uuid, Identifier spellIdentifier) {
        this(player, uuid, spellIdentifier, 3);
    }

    @Override
    public void cast() {
        if(check()) {
            var pulledPlayer = server.getPlayerManager().getPlayer(uuid);
            if(pulledPlayer == null)return;
            Vec3d velocity = player.getPos().subtract(pulledPlayer.getPos()).normalize().negate();
            pulledPlayer.setVelocity(velocity);
            pulledPlayer.velocityModified = true;
        }
    }

    @Override
    public Identifier getID() {
        return new Identifier("chainsmpspells", "push");
    }


    public boolean check() {
        return checkKnowledge() && checkMana();
    }
}
