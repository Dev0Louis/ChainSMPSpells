package org.d1p4k.chainsmpspells.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.d1p4k.chainsmpspells.ChainSMPSpells;
import org.d1p4k.chainsmpspells.accessor.ItemStackJuggernautModeAccessor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemStackJuggernautModeAccessor {

    @Shadow public abstract Item getItem();

    @Shadow public abstract @Nullable NbtCompound getNbt();


    @Shadow @Final public static ItemStack EMPTY;

    @Shadow @Final @Deprecated private Item item;

    @Shadow public abstract void setCount(int count);

    @Inject(method = "postHit", at = @At("HEAD"))
    public void noDamageMixin(LivingEntity target, PlayerEntity attacker, CallbackInfo ci) {
        attacker.sendMessage(Text.of(getJuggernautTick() + ""));
    }


    @Inject(method = "inventoryTick", at = @At("TAIL"))
    public void removeAfterExperation(World world, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        if(world instanceof ServerWorld serverWorld) {
            if(isInValid(serverWorld)) {
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

    public boolean isInValid(ServerWorld world) {
        return !isValid(world);
    }
    public boolean isValid(ServerWorld world) {
        return getJuggernautTick() == 0L || (((ServerWorldAccessor) world).getWorldProperties().getTime()-getJuggernautTick())<20*90;
    }


}
