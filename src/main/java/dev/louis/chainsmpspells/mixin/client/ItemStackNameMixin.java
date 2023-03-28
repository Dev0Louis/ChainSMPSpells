package dev.louis.chainsmpspells.mixin.client;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackNameMixin {

    @Shadow public abstract @Nullable NbtCompound getNbt();

    @Inject(method = "getName", at = @At("HEAD"), cancellable = true)
    public void nameMixin(CallbackInfoReturnable<Text> cir) {
        if(this.getNbt() != null) {
            if (this.getNbt().contains("spell")) {
                var a = this.getNbt().getString("spell").split(":");
                if(a.length < 2)return;
                cir.setReturnValue(Text.translatable("item."+ a[0] + "." + a[1] + "_spell_book"));
            }
        }
    }
}
