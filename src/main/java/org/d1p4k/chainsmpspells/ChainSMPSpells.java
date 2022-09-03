package org.d1p4k.chainsmpspells;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.effect.InstantStatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.d1p4k.chainsmpspells.packet.c2s.SpellUseS2CPacket;
import org.d1p4k.chainsmpspells.potions.ManaStatusEffect;

public class ChainSMPSpells implements ModInitializer {
    private static Potion MANA;
    private static InstantStatusEffect MANAEFFECT = new ManaStatusEffect();
    @Override
    public void onInitialize() {
        SpellUseS2CPacket.register();
        MANAEFFECT = Registry.register(
                Registry.STATUS_EFFECT,
                    new Identifier("css", "mana"),
                        MANAEFFECT);
        MANA = register("mana", new Potion("a", new StatusEffectInstance(MANAEFFECT, 1)));


    }

    private static Potion register(String name, Potion potion) {
        return Registry.register(Registry.POTION, name, potion);
    }
}
