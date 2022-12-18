package org.d1p4k.chainsmpspells;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.d1p4k.chainsmpspells.mana.effect.ManaEffects;
import org.d1p4k.chainsmpspells.packet.c2s.SpellUseS2CPacket;
import org.d1p4k.chainsmpspells.scheduler.TaskExecutor;
import org.d1p4k.chainsmpspells.test.command.testcommand;

public class ChainSMPSpells implements ModInitializer {

    public static MinecraftServer server;

    @Override
    public void onInitialize() {
        SpellUseS2CPacket.register();
        CommandRegistrationCallback.EVENT.register((dispatcher, commandRegistryAccess, registrationEnvironment) -> {
            testcommand.register(dispatcher);
        });
        ServerLifecycleEvents.SERVER_STARTING.register((server) -> {
            ChainSMPSpells.server = server;
        });
        TaskExecutor.init();
        ChainSMPSpellItems.init();
        ChainSMPSpellBlocks.init();
        ManaEffects.init();
    }
}
