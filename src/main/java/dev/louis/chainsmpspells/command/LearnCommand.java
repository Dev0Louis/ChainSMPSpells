package dev.louis.chainsmpspells.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import dev.louis.nebula.Nebula;
import dev.louis.nebula.api.NebulaPlayer;
import dev.louis.nebula.spell.SpellType;

public class LearnCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        var executer = CommandManager.literal("learn").executes(context -> 0);
        Nebula.NebulaRegistries.SPELL_TYPE.forEach(spellType -> {
            executer.then(CommandManager.literal(SpellType.getId(spellType).toString()).executes(context -> {
                if(context.getSource().isExecutedByPlayer()) {
                    NebulaPlayer.access(context.getSource().getPlayer()).getSpellKnowledgeManager().addCastableSpell(spellType);
                }
                return 0;
            }));
        });
        dispatcher.register(executer);
    }
}
