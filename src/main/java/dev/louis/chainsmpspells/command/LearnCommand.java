package dev.louis.chainsmpspells.command;

import com.mojang.brigadier.CommandDispatcher;
import dev.louis.nebula.Nebula;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class LearnCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        var executer = CommandManager.literal("learn").requires(source -> source.hasPermissionLevel(4));
        Nebula.NebulaRegistries.SPELL_TYPE.forEach(spellType -> {
            executer.then(CommandManager.literal(spellType.getId().toString()).executes(context -> {
                if(context.getSource().isExecutedByPlayer()) {
                    context.getSource().getPlayer().getSpellManager().learnSpell(spellType);
                }
                return 0;
            }));
        });
        executer.then(CommandManager.literal("all").executes(context -> {
            if(context.getSource().isExecutedByPlayer()) {
                Nebula.NebulaRegistries.SPELL_TYPE.forEach(spellType -> {
                    context.getSource().getPlayer().getSpellManager().learnSpell(spellType);
                });
            }
            return 0;
        }));
        dispatcher.register(executer);
    }

}
