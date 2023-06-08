package dev.louis.chainsmpspells.mixin;

import com.mojang.authlib.GameProfile;
import dev.louis.chainsmpspells.accessor.SupernovaPlayer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntitySupernovaSpellMixin extends PlayerEntity implements SupernovaPlayer {
    @Shadow public abstract ServerWorld getServerWorld();

    public ServerPlayerEntitySupernovaSpellMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }


    int ticksTillSupernova = 0;
    boolean isCombusting = false;
    @Inject(method = "onDeath", at = @At("HEAD"))
    public void defuseExplosionOnDeath(DamageSource damageSource, CallbackInfo ci) {
        defuseSupernova();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tickSupernova(CallbackInfo ci) {
        if (isCombusting()) {
            ticksTillSupernova--;
            if (getTicksTillSupernova() == 0) {
                combust();
                defuseSupernova();
            }
        }
    }

    public void combust() {
        this.getServerWorld().createExplosion(null, this.getX(), this.getY()+0.4, this.getZ(), 16.0F, World.ExplosionSourceType.MOB);
        this.setHealth(0f);
    }

    public void startCombustion() {
        this.ticksTillSupernova = 40;
        this.isCombusting = true;
    }

    public void defuseSupernova() {
        this.ticksTillSupernova = 0;
        this.isCombusting = false;
    }

    public boolean isCombusting() {
        return this.isCombusting;
    }
    public int getTicksTillSupernova() {
        return this.ticksTillSupernova;
    }
}
