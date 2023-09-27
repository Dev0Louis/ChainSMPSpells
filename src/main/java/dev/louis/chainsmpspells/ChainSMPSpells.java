package dev.louis.chainsmpspells;

import com.mojang.logging.LogUtils;
import dev.louis.chainsmpspells.blocks.ChainSMPSpellsBlocks;
import dev.louis.chainsmpspells.command.LearnCommand;
import dev.louis.chainsmpspells.items.ChainSMPSpellsItems;
import dev.louis.chainsmpspells.mana.effect.ManaEffects;
import dev.louis.chainsmpspells.networking.ICanHasChainSMPSpellsPayload;
import dev.louis.chainsmpspells.recipe.ModRecipes;
import dev.louis.chainsmpspells.spell.*;
import dev.louis.nebula.spell.SpellType;
import eu.pb4.polymer.networking.api.PolymerNetworking;
import eu.pb4.polymer.networking.api.server.PolymerServerNetworking;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;

public class ChainSMPSpells implements ModInitializer {

    public static MinecraftServer server;
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String id = "chainsmpspells";

    public static final Identifier HAS_CLIENT_MODS = new Identifier("chainsmpspells", "has_spell_table");


    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, commandRegistryAccess, registrationEnvironment) -> {
            LearnCommand.register(dispatcher);
        });
        ModRecipes.init();
        ServerLifecycleEvents.SERVER_STARTING.register((server) -> {
            ChainSMPSpells.server = server;
        });
        PolymerNetworking.registerCommonPayload(ChainSMPSpells.HAS_CLIENT_MODS, 0, ICanHasChainSMPSpellsPayload::read);

        Spells.init();
        ChainSMPSpellsItems.init();
        ChainSMPSpellsBlocks.init();
        ManaEffects.init();
    }

    public static class Spells {
        public static List<SpellType<?>> targetingSpells;

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
            targetingSpells = List.of(ChainSMPSpells.Spells.PULL, ChainSMPSpells.Spells.PUSH, ChainSMPSpells.Spells.TELEPORT);

        }
    }

    public static boolean isPlayerTargetable(PlayerEntity targetedPlayer) {
        final var player = MinecraftClient.getInstance().player;
        if(player == null)return false;
        boolean is = player.canSee(targetedPlayer) && player.isPartOfGame();
        boolean isNot = targetedPlayer.isCreative() || targetedPlayer.isSpectator() || targetedPlayer.isInvisibleTo(targetedPlayer) || player.isSpectator();
        return is && !isNot;
    }

    public static boolean isClientVanilla(@Nullable ServerPlayerEntity player) {
        var playerNotNull = player != null;
        if(playerNotNull) {
            var networkNotNull = player.networkHandler != null;
            if(networkNotNull) {
                var version = PolymerServerNetworking.getSupportedVersion(player.networkHandler, ChainSMPSpells.HAS_CLIENT_MODS);
                return version == -1;
            }
        }
        return false;
    }

}
