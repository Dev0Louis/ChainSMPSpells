package org.d1p4k.chainsmpspells.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.d1p4k.chainsmpspells.accessor.ItemStackJuggernautModeAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

    @Shadow public ServerPlayerEntity player;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/ScreenHandler;onSlotClick(IILnet/minecraft/screen/slot/SlotActionType;Lnet/minecraft/entity/player/PlayerEntity;)V", shift = At.Shift.BEFORE),
            method = "onClickSlot", cancellable = true)
    public void onClickSlot(ClickSlotC2SPacket packet, CallbackInfo ci) {
        if(packet.getSlot() < -1 && packet.getSlot() == -999) return;

        var modifiedStack = packet.getModifiedStacks();
        if(modifiedStack.isEmpty())return;
        var size = modifiedStack.size();

        boolean shouldCancel = false;
        if(size == 1) {
            ItemStack itemStack = modifiedStack.values().stream().toList().get(0);
            shouldCancel = ItemStackJuggernautModeAccessor.access(itemStack).isJuggernautItem();
        }else if (size == 2) {
            ItemStack itemStack = modifiedStack.values().stream().toList().get(0);
            ItemStack itemStack2 = modifiedStack.values().stream().toList().get(1);
            shouldCancel = ItemStackJuggernautModeAccessor.access(itemStack).isJuggernautItem() || ItemStackJuggernautModeAccessor.access(itemStack2).isJuggernautItem();
        }

        if(shouldCancel) {
            ci.cancel();
            player.currentScreenHandler.syncState();
        }
    }
}
