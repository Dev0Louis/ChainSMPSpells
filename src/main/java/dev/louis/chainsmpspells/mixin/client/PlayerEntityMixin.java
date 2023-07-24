package dev.louis.chainsmpspells.mixin.client;

import dev.louis.chainsmpspells.ChainSMPSpells;
import dev.louis.chainsmpspells.ChainSMPSpellsClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Shadow public abstract void readCustomDataFromNbt(NbtCompound nbt);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean isGlowing() {
        if(this.getWorld().isClient && this.isTargetedPlayer()) return true;
        return super.isGlowing();
    }

    @Override
    public int getTeamColorValue() {
        if(this.getWorld().isClient && this.isTargetedPlayer())return 0xFFFF005A;
        return super.getTeamColorValue();
    }

    private boolean isTargetedPlayer() {
        var targetedPlayer = ChainSMPSpellsClient.getPlayerInView();
        if(targetedPlayer.isEmpty())return false;
        if(ChainSMPSpells.isPlayerTargetable(targetedPlayer.get())) {
            return (Object) this == targetedPlayer.get();
        }
        return false;
    }
}
