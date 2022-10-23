package org.d1p4k.chainsmpspells.spell.spells;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.d1p4k.nebula.spell.AbstractSpell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PullSpell extends AbstractSpell {
    public static Identifier spellId = new Identifier("css" , "pull");

    public PullSpell(ServerPlayerEntity player, Identifier spellIdentifier, int cost) {
        super(player, spellIdentifier, cost);
    }
    public PullSpell(ServerPlayerEntity player, Identifier spellIdentifier) {
        this(player, spellIdentifier, 3);
    }


    @Override
    public void cast() {
        if(check()) {
            Box box = player.getBoundingBox().expand(32, 40, 32);
            List<PlayerEntity> players = getPlayersInBox(box, player.getWorld().getPlayers());
            players.remove(player);
            for (PlayerEntity player1 : players) {
                Vec3d velocity = player.getPos().subtract(player1.getPos());
                player1.setVelocity(velocity.multiply(30/velocity.lengthSquared()));
                player1.velocityModified = true;
            }
        }
    }
    public List<PlayerEntity> getPlayersInBox(Box box, Collection<? extends PlayerEntity> players) {
        List<PlayerEntity> playerList = new ArrayList<>();
        for(PlayerEntity player1 : player.getWorld().getPlayers()) {
            if(box.contains(player1.getPos())) {
                playerList.add(player1);
            }
        }
        return playerList;
    }
    public boolean check() {
        return checkKnowledge() && checkMana();
    }
}
