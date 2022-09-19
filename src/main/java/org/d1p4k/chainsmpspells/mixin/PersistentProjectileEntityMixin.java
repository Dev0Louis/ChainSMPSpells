package org.d1p4k.chainsmpspells.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.d1p4k.chainsmpspells.accessor.ArrowEntityAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends ProjectileEntity implements ArrowEntityAccessor  {


    public PersistentProjectileEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public boolean damageOwner = true;

    @Override
    public boolean shouldDamageOwner() {
        return damageOwner;
    }

    @Override
    public void shouldDamageOwner(boolean b) {
        this.damageOwner = b;
    }

    @Inject(method = "onEntityHit", at = @At("HEAD"), cancellable = true)
    public void noOwnerDamageMixin(EntityHitResult entityHitResult, CallbackInfo ci) {
        Entity target = entityHitResult.getEntity();
        if(!damageOwner) {
            if(this.getOwner() == null)return;
            if(target.equals(this.getOwner())) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "onBlockHit", at = @At("HEAD"))
    public void despawnOnHit(CallbackInfo ci) {
        if(damageOwner)return;
        this.remove(RemovalReason.DISCARDED);

    }
}
