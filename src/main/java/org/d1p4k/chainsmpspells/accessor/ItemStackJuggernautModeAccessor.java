package org.d1p4k.chainsmpspells.accessor;

import net.minecraft.item.ItemStack;

public interface ItemStackJuggernautModeAccessor {
    public static ItemStackJuggernautModeAccessor access(ItemStack itemStack) {
        return (ItemStackJuggernautModeAccessor) (Object) itemStack;
    }
    public void setJuggernautModeTick(long ticks);
    public long getJuggernautTick();
}
