package dev.louis.chainsmpspells.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import dev.louis.chainsmpspells.accessor.ItemStackJuggernautModeAccessor;
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
    @Shadow public abstract NbtCompound getOrCreateNbt();

    @Inject(method = "inventoryTick", at = @At("TAIL"))
    public void removeAfterExpiration(World world, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        if(world instanceof ServerWorld serverWorld) {
            if(chainSMPSpells$isJuggernautItem() && chainSMPSpells$isInValid(serverWorld)) {
                this.setCount(0);
            }
        }
    }
    @Override
    public void chainSMPSpells$setJuggernautModeTick(long ticks) {
        NbtCompound nbt = this.getOrCreateNbt();
        if(nbt == null)return;
        nbt.putLong("JuggernautTicks", ticks);
    }

    @Override
    public long chainSMPSpells$getJuggernautTick() {
        if(this.getNbt() == null)return 0L;
        return this.getNbt().getLong("JuggernautTicks");
    }

    @Override
    public boolean chainSMPSpells$isJuggernautItem() {
        if(this.getNbt() == null)return false;
        return this.getNbt().contains("JuggernautTicks");
    }

    public boolean chainSMPSpells$isInValid(ServerWorld world) {
        return !chainSMPSpells$isValid(world);
    }

    public boolean chainSMPSpells$isValid(ServerWorld world) {
        long juggernautTicks = chainSMPSpells$getJuggernautTick();
        if(juggernautTicks == 0L)return false;
        return (((ServerWorldAccessor) world).getWorldProperties().getTime()- chainSMPSpells$getJuggernautTick())<20*90;
    }


}
