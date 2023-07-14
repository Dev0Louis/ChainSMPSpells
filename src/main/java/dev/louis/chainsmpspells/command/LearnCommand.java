package dev.louis.chainsmpspells.command;

import com.mojang.brigadier.CommandDispatcher;
import dev.louis.nebula.Nebula;
import dev.louis.nebula.api.NebulaPlayer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class LearnCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        var executer = CommandManager.literal("learn").requires(source -> source.hasPermissionLevel(4));
        Nebula.NebulaRegistries.SPELL_TYPE.forEach(spellType -> {
            executer.then(CommandManager.literal(spellType.getId().toString()).executes(context -> {
                if(context.getSource().isExecutedByPlayer()) {
                    NebulaPlayer.access(context.getSource().getPlayer()).getSpellManager().addSpell(spellType);
                }
                return 0;
            }));
        });
        executer.then(CommandManager.literal("all").executes(context -> {
            if(context.getSource().isExecutedByPlayer()) {
                Nebula.NebulaRegistries.SPELL_TYPE.forEach(spellType -> {
                    NebulaPlayer.access(context.getSource().getPlayer()).getSpellManager().addSpell(spellType);
                });
            }
            return 0;
        }));
        dispatcher.register(executer);
    }

}
