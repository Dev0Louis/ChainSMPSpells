package org.d1p4k.chainsmpspells.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.d1p4k.chainsmpspells.accessor.ServerPlayerEntityJuggernautModeAccessor;
import org.d1p4k.chainsmpspells.spell.spells.JuggernautSpell;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityJuggernautSpellMixin implements ServerPlayerEntityJuggernautModeAccessor  {
    long juggernautTick = 0L;
    @Inject(method = "onDeath", at = @At("RETURN"))
    public void removeJuggernautModeOnDeath(DamageSource damageSource, CallbackInfo ci) {
        JuggernautSpell.clearJuggernautItems(this.getPlayer());
        juggernautTick = 0L;
    }

    @Inject(method = "copyFrom", at = @At("RETURN"))
    public void copyJuggernautMode(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        if(alive) {
            this.setJuggernautModeTick(ServerPlayerEntityJuggernautModeAccessor.access(oldPlayer).getJuggernautTick());
        }
    }

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    public void removeJuggernautItemsOnLeave(CallbackInfo ci) {
        JuggernautSpell.clearJuggernautItems(this.getPlayer());
    }




    @Override
    public void setJuggernautModeTick(long ticks) {
        juggernautTick = ticks;
    }

    @Override
    public long getJuggernautTick() {
        return juggernautTick;
    }


    @Override
    public boolean isInJuggernautMode() {
        return (juggernautTick > 0L);
    }
    @Override
    public boolean isNotInJuggernautMode() {
        return !isInJuggernautMode();
    }
    @Override
    public ServerPlayerEntity getPlayer() {
        return (ServerPlayerEntity) (Object) this;
    }
}
