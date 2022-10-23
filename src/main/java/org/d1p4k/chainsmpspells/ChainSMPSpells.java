package org.d1p4k.chainsmpspells;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.PotionItem;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.d1p4k.chainsmpspells.knowledge.items.SuicideSpellBook;
import org.d1p4k.chainsmpspells.packet.c2s.SpellUseS2CPacket;
import org.d1p4k.chainsmpspells.scheduler.TaskExecutor;
import org.d1p4k.chainsmpspells.test.command.testcommand;
import org.d1p4k.nebula.knowledge.SpellKnowledge;

public class ChainSMPSpells implements ModInitializer {

    public static MinecraftServer server;
    public static final Item SUICIDE_SPELL_BOOK = new SuicideSpellBook(new FabricItemSettings().group(ItemGroup.MISC).fireproof());

    @Override
    public void onInitialize() {
        SpellUseS2CPacket.register();
        registerSpellBooks();
        CommandRegistrationCallback.EVENT.register((dispatcher, commandRegistryAccess, registrationEnvironment) -> {
            testcommand.register(dispatcher);
        });
        ServerLifecycleEvents.SERVER_STARTING.register((server) -> {
            ChainSMPSpells.server = server;
        });
        TaskExecutor.init();
    }

    private void registerSpellBooks() {
        Registry.register(Registry.ITEM, new Identifier("css", "suicide_spell_book"), SUICIDE_SPELL_BOOK);
    }
}
