package dev.louis.chainsmpspells.mixin.client;

import dev.louis.chainsmpspells.ChainSMPSpellsClient;
import dev.louis.nebula.Nebula;
import dev.louis.nebula.networking.SpellCastC2SPacket;
import dev.louis.nebula.spell.Spell;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
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

    @Shadow public abstract void tick();

    int spellCooldown = 0;

    @Inject(method = "handleInputEvents", at = @At("HEAD"))
    private void handelInputEventsForNebula(CallbackInfo ci) {
        if(spellCooldown > 0) {
            spellCooldown--;
        } else {
            Nebula.NebulaRegistries.SPELL_TYPE.forEach(spellType -> {
                ChainSMPSpellsClient.INSTANCE.getSpellKeybindManager().getKey(spellType).ifPresent(keyBinding -> {
                    //System.out.println(SpellType.getId(spellType).toString() +" | "+ keyBinding.getBoundKeyTranslationKey());
                    if(keyBinding.isPressed()) {
                        resetSpellCooldown();
                        Spell spell = spellType.create(player);
                        /*System.out.println(((TargetingSpell)spell).castedOn());
                        ((NebulaSpellManagerAccessor) ((NebulaPlayer)player).getSpellManager()).getCastableSpells().forEach((spellType1 -> System.out.println(SpellType.getId(spellType1))));
                        boolean b =  ((NebulaSpellManagerAccessor) ((NebulaPlayer)player).getSpellManager()).getCastableSpells().contains(spellType);
                        System.out.println("ACABA: " + b);
                        System.out.println(((NebulaPlayer)player).getManaManager().getMana());
                        System.out.println("AAA: " + ((TargetingSpell)spell).castedOn() != null);
                        boolean i = ((NebulaPlayer)player).getManaManager().getMana() - spellType.getManaCost() >= 0;
                        System.out.println("BBB: " + i);
                        System.out.println("I should cast: " + spell);
                        System.out.println("Casting is " + spell.isCastable());*/
                        if(!spell.isCastable())return;
                        ClientPlayNetworking.send(new SpellCastC2SPacket(spell));
                    }
                });
            });
        }
    }

    public void resetSpellCooldown() {
        spellCooldown = getSpellCooldown();
    }

    public int getSpellCooldown() {
        return ChainSMPSpellsClient.INSTANCE.config.getSpellCooldown();
    }
}
