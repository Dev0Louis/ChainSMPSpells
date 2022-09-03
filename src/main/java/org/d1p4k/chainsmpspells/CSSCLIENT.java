package org.d1p4k.chainsmpspells;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.effect.InstantStatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.d1p4k.chainsmpspells.potions.ManaStatusEffect;
import org.d1p4k.chainsmpspells.test.command.testcommand;
import org.d1p4k.nebula.commands.NebluaCommand;

public class CSSCLIENT implements ClientModInitializer {
    private static Potion MANA;
    private static InstantStatusEffect MANAEFFECT = new ManaStatusEffect();
    @Override
    public void onInitializeClient() {
        MANAEFFECT = Registry.register(
                Registry.STATUS_EFFECT,
                new Identifier("css", "mana"),
                MANAEFFECT);
        MANA = register("mana", new Potion("mana", new StatusEffectInstance(MANAEFFECT, 1)));
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            testcommand.register(dispatcher);
        });
    }
    private static Potion register(String name, Potion potion) {
        return Registry.register(Registry.POTION, name, potion);
    }
}
