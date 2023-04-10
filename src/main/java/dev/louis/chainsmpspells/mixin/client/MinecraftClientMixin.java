package dev.louis.chainsmpspells.mixin.client;

import dev.louis.chainsmpspells.ChainSMPSpellsClient;
import dev.louis.nebula.Nebula;
import dev.louis.nebula.networking.SpellCastC2SPacket;
import dev.louis.nebula.spell.Spell;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow @Nullable public ClientPlayerEntity player;
    int spellCooldown = 10;

    @Inject(method = "handleInputEvents", at = @At("HEAD"))
    private void handelInputEventsForNebula(CallbackInfo ci) {
        if(spellCooldown > 0) {
            spellCooldown--;
        } else {
            Nebula.NebulaRegistries.SPELL_TYPE.forEach(spellType -> {
                ChainSMPSpellsClient.INSTANCE.getSpellKeybindManager().getKey(spellType).ifPresent(keyBinding -> {
                    if(keyBinding.wasPressed()) {
                        resetSpellCooldown();
                        Spell spell = spellType.create(player);
                        if(!spell.isCastable())return;
                        var buf = PacketByteBufs.create();
                        new SpellCastC2SPacket(spell).write(buf);
                        ClientPlayNetworking.send(SpellCastC2SPacket.getId(), buf);
                    }
                });
            });
        }
    }

    public void resetSpellCooldown() {
        spellCooldown = ChainSMPSpellsClient.INSTANCE.config.getSpellCooldown();
    }
}
