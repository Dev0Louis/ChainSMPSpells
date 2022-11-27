package org.d1p4k.chainsmpspells.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.d1p4k.chainsmpspells.accessor.ItemStackJuggernautModeAccessor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemStackJuggernautModeAccessor {
    @Shadow public abstract @Nullable NbtCompound getNbt();
    @Shadow public abstract void setCount(int count);


    @Inject(method = "inventoryTick", at = @At("TAIL"))
    public void removeAfterExperation(World world, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        if(world instanceof ServerWorld serverWorld) {
            if(isJuggernautItem() && isInValid(serverWorld)) {
                this.setCount(0);
            }
        }
    }
    @Override
    public void setJuggernautModeTick(long ticks) {
        if(this.getNbt() == null)return;
        this.getNbt().putLong("JuggernautTicks", ticks);
    }

    @Override
    public long getJuggernautTick() {
        if(this.getNbt() == null)return 0L;
        return this.getNbt().getLong("JuggernautTicks");
    }

    @Override
    public boolean isJuggernautItem() {
        if(this.getNbt() == null)return false;
        return this.getNbt().contains("JuggernautTicks");
    }

    public boolean isInValid(ServerWorld world) {
        return !isValid(world);
    }
    public boolean isValid(ServerWorld world) {
        long juggernautTicks = getJuggernautTick();
        if(juggernautTicks == 0L)return false;
        return (((ServerWorldAccessor) world).getWorldProperties().getTime()-getJuggernautTick())<20*90;
    }


}
