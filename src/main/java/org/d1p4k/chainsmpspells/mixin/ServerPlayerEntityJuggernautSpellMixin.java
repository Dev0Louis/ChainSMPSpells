package org.d1p4k.chainsmpspells.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.d1p4k.chainsmpspells.accessor.ItemStackJuggernautModeAccessor;
import org.d1p4k.chainsmpspells.accessor.ServerPlayerEntityJuggernautModeAccessor;
import org.d1p4k.chainsmpspells.spell.spells.JuggernautSpell;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityJuggernautSpellMixin implements ServerPlayerEntityJuggernautModeAccessor {
    boolean isInJuggernautMode = false;
    @Inject(method = "onDeath", at = @At("HEAD"))
    public void removeJuggernautModeOnDeath(DamageSource damageSource, CallbackInfo ci) {
        JuggernautSpell.clearJuggernautItems(this.getPlayer());
        isInJuggernautMode = false;
    }

    @Inject(method = "copyFrom", at = @At("TAIL"))
    public void copyJuggernautMode(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        if(alive) {
            this.setInJuggernautMode(ServerPlayerEntityJuggernautModeAccessor.access(oldPlayer).isInJuggernautMode());
        }
    }

    @Override
    public void setInJuggernautMode(boolean isInJuggernautMode) {
        this.isInJuggernautMode = isInJuggernautMode;
    }
    @Override
    public boolean isInJuggernautMode() {
        return this.isInJuggernautMode;
    }
    @Override
    public boolean isNotInJuggernautMode() {
        return !this.isInJuggernautMode;
    }
    @Override
    public ServerPlayerEntity getPlayer() {
        return (ServerPlayerEntity) (Object) this;
    }
}
