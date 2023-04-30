package dev.louis.chainsmpspells;

import dev.louis.chainsmpspells.blocks.ChainSMPSpellsBlocks;
import dev.louis.chainsmpspells.command.LearnCommand;
import dev.louis.chainsmpspells.items.ChainSMPSpellsItems;
import dev.louis.chainsmpspells.mana.effect.ManaEffects;
import dev.louis.chainsmpspells.scheduler.TaskExecutor;
import dev.louis.chainsmpspells.spell.*;
import dev.louis.nebula.spell.SpellType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

import java.util.logging.Logger;

public class ChainSMPSpells implements ModInitializer {

    public static MinecraftServer server;
    public static Logger LOGGER = Logger.getLogger("ChainSMPSpells");

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, commandRegistryAccess, registrationEnvironment) -> {
            LearnCommand.register(dispatcher);
        });
        ServerLifecycleEvents.SERVER_STARTING.register((server) -> {
            ChainSMPSpells.server = server;
        });
        Spells.init();
        ChainSMPSpellsItems.init();
        ChainSMPSpellsBlocks.init();
        TaskExecutor.init();
        ManaEffects.init();
    }

    public static class Spells {
        public static SpellType<ArrowSpell> ARROW =
                SpellType.register(new Identifier("chainsmpspells", "arrow"),SpellType.Builder.create(ArrowSpell::new, 2));
        public static SpellType<JuggernautSpell> JUGGERNAUT =
                SpellType.register(new Identifier("chainsmpspells", "juggernaut"), SpellType.Builder.create(JuggernautSpell::new, 20));
        public static SpellType<PullSpell> PULL =
                SpellType.register(new Identifier("chainsmpspells", "pull"), SpellType.Builder.create(PullSpell::new, 3));
        public static SpellType<PushSpell> PUSH =
                SpellType.register(new Identifier("chainsmpspells", "push"), SpellType.Builder.create(PushSpell::new, 3));
        public static SpellType<RewindSpell> REWIND =
                SpellType.register(new Identifier("chainsmpspells", "rewind"), SpellType.Builder.create(RewindSpell::new, 7));
        public static SpellType<SuicideSpell> SUICIDE =
                SpellType.register(new Identifier("chainsmpspells", "suicide"), SpellType.Builder.create(SuicideSpell::new, 1));
        public static SpellType<TeleportSpell> TELEPORT =
                SpellType.register(new Identifier("chainsmpspells", "teleport"), SpellType.Builder.create(TeleportSpell::new, 5));
        public static SpellType<SupernovaSpell> SUPERNOVA =
                SpellType.register(new Identifier("chainsmpspells", "supernova"), SpellType.Builder.create(SupernovaSpell::new, 20));
        public static void init() {

        }
    }

}
