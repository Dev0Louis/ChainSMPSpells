package org.d1p4k.chainsmpspells.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.d1p4k.chainsmpspells.spell.spells.RewindSpell;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityRewindSpellOnDeathRemoveMixin {

    @Inject(method = "onDeath", at = @At("TAIL"))
    public void removeRewindOnDeath(DamageSource damageSource, CallbackInfo ci) {

    }
}
