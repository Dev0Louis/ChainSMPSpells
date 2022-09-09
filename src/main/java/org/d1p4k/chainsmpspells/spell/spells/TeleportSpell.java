package org.d1p4k.chainsmpspells.spell.spells;

import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.d1p4k.nebula.spell.AbstractSpell;

import java.util.List;
import java.util.Random;

public class TeleportSpell extends AbstractSpell {

    Random rd = new Random();
    public static Identifier spellId = new Identifier("css" , "teleport");

    private final TargetPredicate PLAYERS_IN_RANGE_PREDICATE = TargetPredicate.createAttackable().setBaseMaxDistance(64.0);

    public TeleportSpell(PlayerEntity player, Identifier spellIdentifier, int cost) {
        super(player, spellIdentifier, cost);
    }

    @Override
    public void cast() {
        //TODO: Refactoring!
        PlayerEntity selectedPlayer = this.player;
        List<PlayerEntity> list = player.getWorld().getPlayers(this.PLAYERS_IN_RANGE_PREDICATE, player, player.getBoundingBox().expand(20.0, 64.0, 20.0));
        for (PlayerEntity playerEntity : list) {
            if(playerEntity.getUuid() == this.player.getUuid()) {
                selectedPlayer = playerEntity;
            }
            if(rd.nextFloat() > 0.6) {
                selectedPlayer = playerEntity;
                break;
            }
        }
        this.player.teleport(selectedPlayer.getX(), selectedPlayer.getY(), selectedPlayer.getX(), true);

    }
}
