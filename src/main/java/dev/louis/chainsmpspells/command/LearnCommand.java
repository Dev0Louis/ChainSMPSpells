package dev.louis.chainsmpspells.command;

import com.mojang.brigadier.CommandDispatcher;
import dev.louis.nebula.Nebula;
import dev.louis.nebula.api.NebulaPlayer;
import dev.louis.nebula.spell.SpellType;
import net.minecraft.block.BlockState;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class LearnCommand {
    private static final Text TITLE = Text.translatable("container.spell_crafting");

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
        executer.then(CommandManager.literal("testScreen").executes(context -> {
            BlockState blockState = context.getSource().getWorld().getBlockState(BlockPos.ofFloored(0,0,0));
            var a = blockState.createScreenHandlerFactory(context.getSource().getWorld(), new BlockPos(0,0,0));
            context.getSource().getPlayer().openHandledScreen(a);
            return 0;
        }));
        dispatcher.register(executer);
    }

}
