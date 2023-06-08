package dev.louis.chainsmpspells.mixin.client;

import dev.louis.chainsmpspells.ChainSMPSpellsClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {


    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean isGlowing() {
        if(this.getWorld().isClient) {
            AtomicBoolean shouldGlow = new AtomicBoolean();
            ChainSMPSpellsClient.getPlayerInView().ifPresent(player -> {
                shouldGlow.set(me() == player);
            });
            return shouldGlow.get() || super.isGlowing();
        }
        return super.isGlowing();
    }

    private PlayerEntity me() {
        return (PlayerEntity) (Object) this;
    }
}
