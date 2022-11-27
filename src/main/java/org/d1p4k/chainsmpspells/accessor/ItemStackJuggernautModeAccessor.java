package org.d1p4k.chainsmpspells.accessor;

import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;

public interface ItemStackJuggernautModeAccessor {
    public static ItemStackJuggernautModeAccessor access(ItemStack itemStack) {
        return (ItemStackJuggernautModeAccessor) (Object) itemStack;
    }
    public void setJuggernautModeTick(long ticks);
    public long getJuggernautTick();
    public boolean isJuggernautItem();
    public boolean isInValid(ServerWorld world);
    public boolean isValid(ServerWorld world);
}
