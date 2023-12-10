package dev.louis.chainsmpspells.accessor;

import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;

public interface ItemStackJuggernautModeAccessor {
    static ItemStackJuggernautModeAccessor access(ItemStack itemStack) {
        return (ItemStackJuggernautModeAccessor) (Object) itemStack;
    }
    void chainSMPSpells$setJuggernautModeTick(long ticks);
    long chainSMPSpells$getJuggernautTick();
    boolean chainSMPSpells$isJuggernautItem();
    boolean chainSMPSpells$isInValid(ServerWorld world);
    boolean chainSMPSpells$isValid(ServerWorld world);
}
