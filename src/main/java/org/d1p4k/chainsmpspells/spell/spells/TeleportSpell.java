package org.d1p4k.chainsmpspells.spell.spells;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.d1p4k.nebula.api.NebulaPlayer;
import org.d1p4k.nebula.spell.AbstractSpell;

import java.util.UUID;

import static org.d1p4k.chainsmpspells.ChainSMPSpells.server;

public class TeleportSpell extends AbstractSpell {
    private UUID uuid;
    public TeleportSpell(int cost) {
        super(cost);
    }
    public TeleportSpell() {
        this(5);
    }

    public TeleportSpell setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    @Override
    public void cast(ServerPlayerEntity player) {
        //TODO: Refactoring!
        if(checkKnowledge(player) && checkMana(player)) {
            var teleportPlayer = server.getPlayerManager().getPlayer(uuid);
            if(teleportPlayer == null)return;
            if(teleportPlayer.distanceTo(player) > 25)return;
            decreaseMana(player);
            player.teleport(teleportPlayer.getX(), teleportPlayer.getY(), teleportPlayer.getZ(), true);
        }


    }

    @Override
    public Identifier getID() {
        return new Identifier("chainsmpspells", "teleport");
    }

    @Override
    public boolean checkMana(ServerPlayerEntity player) {
        return ((NebulaPlayer) player).getManaManager().get() >= cost;
    }

    private void decreaseMana(ServerPlayerEntity player) {
        ((NebulaPlayer) player).getManaManager().decrease(cost);
    }
}
