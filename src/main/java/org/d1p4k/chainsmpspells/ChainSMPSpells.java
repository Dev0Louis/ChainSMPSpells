package org.d1p4k.chainsmpspells;

import net.fabricmc.api.ModInitializer;
import net.minecraft.potion.Potion;
import net.minecraft.util.registry.Registry;
import org.d1p4k.chainsmpspells.packet.c2s.SpellUseS2CPacket;

public class ChainSMPSpells implements ModInitializer {
    @Override
    public void onInitialize() {
        SpellUseS2CPacket.register();
    }

    private static Potion register(String name, Potion potion) {
        return Registry.register(Registry.POTION, name, potion);
    }
}
