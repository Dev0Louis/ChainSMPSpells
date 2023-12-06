package dev.louis.chainsmpspells.mixin;

import net.minecraft.server.network.EntityTrackerEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityTrackerEntry.class)
public interface EntityTrackerEntryInvoker {
    @Invoker
    void invokeSyncEntityData();
}
