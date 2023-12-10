package dev.louis.chainsmpspells.mixin.client;

import dev.louis.chainsmpspells.ChainSMPSpellsClient;
import dev.louis.chainsmpspells.config.ChainSMPSpellsConfig;
import dev.louis.nebula.Nebula;
import dev.louis.nebula.spell.Spell;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MinecraftClient.class, remap = false)
public abstract class MinecraftClientMixin {
    @Shadow @Nullable public ClientPlayerEntity player;
    @Unique
    int spellCooldown = 0;

    @Inject(method = "handleInputEvents", at = @At("HEAD"))
    private void handelInputEventsForNebula(CallbackInfo ci) {
        if(spellCooldown > 0) {
            spellCooldown--;
        } else {
            Nebula.SPELL_REGISTRY.forEach(spellType -> {
                ChainSMPSpellsClient.getSpellKeybindManager().getKey(spellType).ifPresent(keyBinding -> {
                    if(keyBinding.isPressed()) {
                        resetSpellCooldown();
                        Spell spell = spellType.create(player);
                        if(!spell.isCastable())return;
                        this.player.getSpellManager().cast(spell);
                    }
                });
            });
        }
    }

    public void resetSpellCooldown() {
        spellCooldown = getSpellCooldown();
    }

    public int getSpellCooldown() {
        return ChainSMPSpellsConfig.getSpellCooldown();
    }
}
