package dev.louis.chainsmpspells.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import dev.louis.chainsmpspells.accessor.RewindPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityRewindSpellOnDeathRemoveMixin extends PlayerEntity implements RewindPlayer {
    public ServerPlayerEntityRewindSpellOnDeathRemoveMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Shadow public abstract void teleport(ServerWorld targetWorld, double x, double y, double z, float yaw, float pitch);

    @Shadow public abstract void playSound(SoundEvent event, SoundCategory category, float volume, float pitch);

    World memorizedWorld;
    BlockPos memorizedPos;
    Vec3d memorizedVelocity;
    float memorizedYaw;
    float memorizedPitch;
    int ticksTillMemory;
    boolean isRemembering = false;

    @Inject(method = "tick", at = @At("RETURN"))
    public void rewindTick(CallbackInfo ci) {
        if(ticksTillMemory > 0) {
            if(ticksTillMemory % 10 == 0)playPingSound();
            isRemembering = true;
            ticksTillMemory--;
        } else if (isRemembering){
            isRemembering = false;
            ((ServerWorld) memorizedWorld).spawnParticles(ParticleTypes.REVERSE_PORTAL, memorizedPos.getX(),memorizedPos.getY(),memorizedPos.getZ(), 2,0,0,0, 0);
            this.teleport((ServerWorld) memorizedWorld, memorizedPos.getX(), memorizedPos.getY(), memorizedPos.getZ(), memorizedYaw, memorizedPitch);
            playRewindSound();
            this.setVelocity(memorizedVelocity.negate());
        }
    }

    private void playPingSound() {
        this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_NOTE_BLOCK_BANJO.value(), this.getSoundCategory(), 1, -1);
    }
    private void playRewindSound() {
        this.world.playSound(null, memorizedPos.getX(), memorizedPos.getY(), memorizedPos.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, this.getSoundCategory(), 1, 1);
    }
    @Inject(method = "onDeath", at = @At("RETURN"))
    public void forget(DamageSource damageSource, CallbackInfo ci) {
        clearMemory();
    }

    public void clearMemory() {
        memorizedWorld = null;
        memorizedPos = null;
        ticksTillMemory = 0;
        isRemembering = false;
    }
    public void addMemory(World memorizedWorld, BlockPos memorizedPos, Vec3d memorizedVelocity, float memorizedYaw, float memorizedPitch, int ticksTillMemory) {
        this.memorizedWorld = memorizedWorld;
        this.memorizedPos = memorizedPos;
        this.memorizedVelocity = memorizedVelocity;
        this.ticksTillMemory = ticksTillMemory;
        this.memorizedYaw = memorizedYaw;
        this.memorizedPitch = memorizedPitch;
        this.isRemembering = true;
    }
    public boolean isRemembering() {
        return isRemembering;
    }
}
