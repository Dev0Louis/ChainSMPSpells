package org.d1p4k.chainsmpspells.test.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.d1p4k.chainsmpspells.spell.spells.SuicideSpell;
import org.d1p4k.nebula.Nebula;
import org.d1p4k.nebula.api.NebulaPlayer;

public class testcommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        Nebula.LOGGER.info("Test");
        dispatcher.register(
                CommandManager.literal("csstest").executes(context -> {
                    // For versions below 1.19, replace "Text.literal" with "new LiteralText".
                    NebulaPlayer Nebulaplayer = (NebulaPlayer) context.getSource().getPlayer();
                    if(!Nebulaplayer.getCastableSpells().contains(SuicideSpell.spellId)) {

                        Nebulaplayer.getCastableSpells().add(SuicideSpell.spellId);
                    }
                    return 1;
                }));
}}
