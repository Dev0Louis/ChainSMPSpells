package dev.louis.chainsmpspells.spell;

import dev.louis.chainsmpspells.accessor.SupernovaPlayer;
import dev.louis.chainsmpspells.network.SupernovaS2CPacket;
import dev.louis.nebula.spell.Spell;
import dev.louis.nebula.spell.SpellType;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class SupernovaSpell extends Spell {
    public SupernovaSpell(SpellType<? extends Spell> spellType, PlayerEntity caster) {
        super(spellType, caster);
    }

    @Override
    public void cast() {
        if(this.isCastable()) {
            drainMana();
            syncPlayerCombustingStatus();
            ((SupernovaPlayer)this.getCaster()).startCombustion();
        }
    }
    public void syncPlayerCombustingStatus() {
        SupernovaS2CPacket packet = new SupernovaS2CPacket(getCaster().getUuid(), 40);
        ServerPlayerEntity player = ((ServerPlayerEntity)this.getCaster());
        List<ServerPlayerEntity> players = player.getServerWorld().getPlayers();;

        for (ServerPlayerEntity serverPlayerEntity : players) {
            sendToPlayerIfNearby(serverPlayerEntity, player.getX(), player.getY(), player.getZ(), packet);
        }
    }

    public final void sendToPlayerIfNearby(ServerPlayerEntity player, double x, double y, double z, FabricPacket packet) {
        if (player.getWorld() != getCaster().getWorld()) return;

        BlockPos blockPos = player.getBlockPos();
        if (blockPos.isWithinDistance(new Vec3d(x, y, z), 64.00)) {
            ServerPlayNetworking.send(player, packet);
        }
    }

}
