package org.d1p4k.chainsmpspells.spell.spells;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.d1p4k.nebula.api.NebulaPlayer;
import org.d1p4k.nebula.spell.AbstractSpell;

import java.util.UUID;

import static org.d1p4k.chainsmpspells.ChainSMPSpells.server;

public class TeleportSpell extends AbstractSpell {
    public UUID uuid;
    public static Identifier spellId = new Identifier("css" , "teleport");

    public TeleportSpell(ServerPlayerEntity player, UUID uuid, Identifier spellIdentifier, int cost) {
        super(player, spellIdentifier, cost);
        this.uuid = uuid;
    }
    public TeleportSpell(ServerPlayerEntity player, UUID uuid, Identifier spellIdentifier) {
        this(player, uuid, spellIdentifier, 5);
        this.uuid = uuid;
    }

    @Override
    public void cast() {
        //TODO: Refactoring!
        if(checkKnowledge() && checkMana()) {
            var teleportPlayer = server.getPlayerManager().getPlayer(uuid);
            if(teleportPlayer == null)return;
            if(teleportPlayer.distanceTo(player) > 25)return;
            decreaseMana();
            player.teleport(teleportPlayer.getX(), teleportPlayer.getY(), teleportPlayer.getZ(), true);
        }


    }
    @Override
    public boolean checkMana() {
        return ((NebulaPlayer) player).getManaManager().get() >= cost;
    }

    private void decreaseMana() {
        ((NebulaPlayer) player).getManaManager().decrease(cost);
    }
}
