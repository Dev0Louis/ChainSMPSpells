package dev.louis.chainsmpspells.mixin.client;

import dev.louis.chainsmpspells.ChainSMPSpellsClient;
import dev.louis.chainsmpspells.config.ChainSMPSpellsConfig;
import dev.louis.nebula.api.spell.SpellType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow @Nullable public ClientPlayerEntity player;
    @Unique
    int spellCooldown = 0;

    @Inject(method = "handleInputEvents", at = @At("HEAD"))
    private void handelInputEventsForNebula(CallbackInfo ci) {
        if (spellCooldown > 0) {
            spellCooldown--;
            return;
        }

        for (SpellType<?> spellType : SpellType.REGISTRY) {
            var optionalKey = ChainSMPSpellsClient.getSpellKeybindManager().getKey(spellType);
            if(optionalKey.isPresent()) {
                var key = optionalKey.get();
                if(key.isPressed()) {
                    this.player.getSpellManager().cast(spellType);
                    this.resetSpellCooldown();
                    return;
                }
            }
        }
    }

    public void resetSpellCooldown() {
        spellCooldown = getSpellCooldown();
    }

    public int getSpellCooldown() {
        return ChainSMPSpellsConfig.getSpellCooldown();
    }
}
