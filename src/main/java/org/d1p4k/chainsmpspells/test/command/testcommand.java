package org.d1p4k.chainsmpspells.test.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.d1p4k.chainsmpspells.spell.spells.*;
import org.d1p4k.nebula.api.NebulaPlayer;

public class testcommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("csstest").executes(context -> {
                    NebulaPlayer nebulaPlayer = (NebulaPlayer) context.getSource().getPlayer();
                    if(nebulaPlayer == null)return 0;
                    context.getSource().getPlayer().sendMessage(Text.of(
                            nebulaPlayer.getSpellKnowledge().getCastableSpells().toString()
                    ));
                    return 1;
                }).then(CommandManager.literal("suicide").executes((context) -> {
                    NebulaPlayer nebulaplayer = (NebulaPlayer) context.getSource().getPlayer();
                    if(!nebulaplayer.getSpellKnowledge().getCastableSpells().contains(SuicideSpell.spellId)) {
                        nebulaplayer.getSpellKnowledge().addCastableSpell(SuicideSpell.spellId);
                    }
                    return 1;

                })).then(CommandManager.literal("teleport").executes((context) -> {
                    NebulaPlayer nebulaplayer = (NebulaPlayer) context.getSource().getPlayer();
                    if(!nebulaplayer.getSpellKnowledge().getCastableSpells().contains(TeleportSpell.spellId)) {
                        nebulaplayer.getSpellKnowledge().addCastableSpell(TeleportSpell.spellId);
                    }
                    return 1;
                })).then(CommandManager.literal("arrow").executes((context -> {
                    NebulaPlayer nebulaplayer = (NebulaPlayer) context.getSource().getPlayer();
                    if(!nebulaplayer.getSpellKnowledge().getCastableSpells().contains(ArrowSpell.spellId)) {
                        nebulaplayer.getSpellKnowledge().addCastableSpell(ArrowSpell.spellId);
                    }
                    return 1;
                }))).then(CommandManager.literal("pull").executes((context -> {
                    NebulaPlayer nebulaplayer = (NebulaPlayer) context.getSource().getPlayer();
                    if(!nebulaplayer.getSpellKnowledge().getCastableSpells().contains(PullSpell.spellId)) {
                        nebulaplayer.getSpellKnowledge().addCastableSpell(PullSpell.spellId);
                    }
                    return 1;
                }))).then(CommandManager.literal("rewind").executes((context -> {
                    NebulaPlayer nebulaplayer = (NebulaPlayer) context.getSource().getPlayer();
                    if (!nebulaplayer.getSpellKnowledge().getCastableSpells().contains(RewindSpell.spellId)) {
                        nebulaplayer.getSpellKnowledge().addCastableSpell(RewindSpell.spellId);
                    }
                    return 1;
                }))).then(CommandManager.literal("juggernaut").executes((context -> {

                    NebulaPlayer nebulaplayer = (NebulaPlayer) context.getSource().getPlayer();
                    if (!nebulaplayer.getSpellKnowledge().getCastableSpells().contains(JuggernautSpell.spellId)) {
                        nebulaplayer.getSpellKnowledge().addCastableSpell(JuggernautSpell.spellId);
                    }
                    return 1;
                })))
        );
}}
