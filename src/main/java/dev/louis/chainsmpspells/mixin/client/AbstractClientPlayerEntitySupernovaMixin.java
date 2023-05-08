package dev.louis.chainsmpspells.mixin.client;

import com.mojang.authlib.GameProfile;
import dev.louis.chainsmpspells.accessor.SupernovaClientPlayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntitySupernovaMixin extends PlayerEntity implements SupernovaClientPlayer {
    public AbstractClientPlayerEntitySupernovaMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    int playerCombustionTimer = 0;

    @Inject(method = "tick", at = @At("HEAD"))
    public void tickCombustTimer(CallbackInfo ci) {
        if(playerCombustionTimer > 0) {
            playerCombustionTimer--;
            var player = MinecraftClient.getInstance().player;
            var pos = this.getPos();
            var random = this.getRandom();

            double d = pos.getX();
            double e = this.getEyeY() - random.nextDouble()*2;
            double f = pos.getZ();
            double g = (random.nextDouble() - 0.5) * 2;
            double h = (random.nextDouble() - 0.5) * 2;
            double k = (random.nextDouble() - 0.5) * 2;

            int i = this.getRandom().nextInt(3);
            while (i > 0) {
                if(i % 2 == 0) {
                    player.getWorld().addParticle(ParticleTypes.EXPLOSION, d, e, f, g, h, k);
                    player.getWorld().playSound(player, this.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS);
                }
                player.getWorld().addParticle(ParticleTypes.ELECTRIC_SPARK, d, e, f, g, h, k);
                i--;
            }
        }
    }

    @Override
    public void setCombustion(int time) {
        this.playerCombustionTimer = time;
    }

    @Override
    public boolean isCombusting() {
        return playerCombustionTimer > 0;
    }
    public int getCombustionTime() {
        return playerCombustionTimer;
    }
}
