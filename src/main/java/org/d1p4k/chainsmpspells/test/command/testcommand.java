package org.d1p4k.chainsmpspells.test.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.d1p4k.chainsmpspells.spell.spells.SuicideSpell;
import org.d1p4k.chainsmpspells.spell.spells.TeleportSpell;
import org.d1p4k.nebula.Nebula;
import org.d1p4k.nebula.api.NebulaPlayer;

public class testcommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        Nebula.LOGGER.info("Test");
        dispatcher.register(
                CommandManager.literal("csstest").executes(context -> {
                    NebulaPlayer nebulaPlayer = (NebulaPlayer) context.getSource().getPlayer();
                    context.getSource().getPlayer().sendMessage(Text.of(
                            nebulaPlayer.getCastableSpells().toString()
                    ));
                    return 1;
                }).then(CommandManager.literal("suicide").executes((context) -> {
                    NebulaPlayer Nebulaplayer = (NebulaPlayer) context.getSource().getPlayer();
                    if(!Nebulaplayer.getCastableSpells().contains(SuicideSpell.spellId)) {
                        Nebulaplayer.getCastableSpells().add(SuicideSpell.spellId);
                    }
                    return 1;

                        })
                ).then(CommandManager.literal("teleport").executes((context) -> {
                    NebulaPlayer Nebulaplayer = (NebulaPlayer) context.getSource().getPlayer();
                    if(!Nebulaplayer.getCastableSpells().contains(TeleportSpell.spellId)) {
                        Nebulaplayer.getCastableSpells().add(TeleportSpell.spellId);
                    }
                    return 1;
                })
        ));
}}
