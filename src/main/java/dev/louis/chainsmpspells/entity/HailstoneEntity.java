package dev.louis.chainsmpspells.entity;

import dev.louis.chainsmpspells.mixin.BlockDisplayEntityInvoker;
import dev.louis.chainsmpspells.mixin.DisplayEntityInvoker;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.joml.Matrix4f;

public class HailstoneEntity extends DisplayEntity.BlockDisplayEntity implements PolymerEntity {
    public static EntityType<HailstoneEntity> HAILSTONE = (
            EntityType.Builder.create(HailstoneEntity::new, SpawnGroup.MISC)
                    .setDimensions(0.0F, 0.0F)
                    .maxTrackingRange(10)
                    .trackingTickInterval(1).build("chainsmpspells:hailstone")
    );
    private float fallAcceleration;

    public HailstoneEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
        this.noClip = false;
        this.setSilent(false);
        fallAcceleration = .1f * Math.max(world.getRandom().nextFloat(), 0.1f);
        setUp();
    }



    @Override
    public void tick() {
        super.tick();
        if(getWorld().isClient())return;
        super.attemptTickInVoid();
        if(fallAcceleration < .1f) {
            fallAcceleration += .01f;
        }
        this.addVelocity(0, -fallAcceleration, 0);
        this.move(MovementType.SELF, this.getVelocity());
    }



    public void setUp() {
        ((DisplayEntityInvoker)this).invokeSetTransformation(new AffineTransformation(generateMatrix()));
        ((BlockDisplayEntityInvoker)this).invokeSetBlockState(Blocks.ICE.getDefaultState());
    }

    protected Matrix4f generateMatrix() {
        Matrix4f transformationMatrix = new Matrix4f();
        transformationMatrix.rotateLocalX((float) this.random.nextTriangular(Math.PI, Math.PI));
        transformationMatrix.rotateLocalY((float) this.random.nextTriangular(Math.PI, Math.PI));
        transformationMatrix.rotateLocalZ((float) this.random.nextTriangular(Math.PI, Math.PI));
        transformationMatrix.scale(0.3456789f);
        return transformationMatrix;
    }
    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {}
    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {}

    static float f = -2f;
    @Override
    public void onLanding() {
        if(this.getWorld().isClient())return;
        if(!this.isOnGround())return;
        this.playSound(SoundEvents.BLOCK_GLASS_BREAK, 0.03f, 1.1f);
        //((ServerWorld)this.getWorld()).spawnParticles(ParticleTypes.SNOWFLAKE, this.getX(), this.getY(), this.getZ(), 1, 0.1, 0.1, 0.1, 0.01);
        ((ServerWorld)this.getWorld()).spawnParticles(
                new BlockStateParticleEffect(ParticleTypes.BLOCK, ((BlockDisplayEntityInvoker)this).invokeGetBlockState()),
                this.getX(),
                this.getY(),
                this.getZ(),
                10,
                0.1,
                0.1,
                0.1,
                0.01
        );
        //((ServerWorld)this.getWorld()).spawnParticles(ParticleTypes.ELECTRIC_SPARK, this.getX(), this.getY(), this.getZ(), 1, 0.1, 0.1, 0.1, 0.01);
        //this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), fallDistance, World.ExplosionSourceType.MOB);
        this.getWorld().getPlayers(TargetPredicate.DEFAULT ,null, this.getBoundingBox().expand(2)).forEach(this::damagePlayer);
        this.discard();

    }

    private final int damage = 3;

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        damagePlayer(player);
        this.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1f, 0.9f);
        this.discard();
        super.onPlayerCollision(player);
    }

    private void damagePlayer(PlayerEntity player) {
        var damageSource = player.getDamageSources().freeze();
        double speedLength = this.getVelocity().length();
        int damage = MathHelper.ceil(MathHelper.clamp(speedLength * this.damage, 2.0, 100));

        if(player.getWorld().getBiome(player.getBlockPos()).value().isCold(player.getBlockPos())) {
            damage = damage * 2;
        }

        player.damage(damageSource, damage);
    }

    @Override
    public EntityType<?> getPolymerEntityType(ServerPlayerEntity player) {
        return EntityType.BLOCK_DISPLAY;
    }


    @Override
    protected void refreshData(boolean shouldLerp, float lerpProgress) {
        super.refreshData(shouldLerp, lerpProgress);
    }
}
