package dev.louis.chainsmpspells;

import com.mojang.logging.LogUtils;
import dev.louis.chainsmpspells.blocks.ChainSMPSpellsBlocks;
import dev.louis.chainsmpspells.items.ChainSMPSpellsItems;
import dev.louis.chainsmpspells.mana.effect.ManaEffects;
import dev.louis.chainsmpspells.networking.ICanHasChainSMPSpellsPayload;
import dev.louis.chainsmpspells.recipe.ModRecipes;
import dev.louis.chainsmpspells.spell.*;
import dev.louis.nebula.spell.SpellType;
import eu.pb4.polymer.networking.api.PolymerNetworking;
import eu.pb4.polymer.networking.api.server.PolymerServerNetworking;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;

public class ChainSMPSpells implements ModInitializer {

    public static MinecraftServer server;
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "chainsmpspells";
    public static final Identifier HAS_CLIENT_MODS = new Identifier(MOD_ID, "has_spell_table");


    @Override
    public void onInitialize() {
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
                SpellType.register(new Identifier(MOD_ID, "arrow"),SpellType.Builder.create(ArrowSpell::new, 2));
        public static SpellType<JuggernautSpell> JUGGERNAUT =
                SpellType.register(new Identifier(MOD_ID, "juggernaut"), SpellType.Builder.create(JuggernautSpell::new, 20));
        public static SpellType<PullSpell> PULL =
                SpellType.register(new Identifier(MOD_ID, "pull"), SpellType.Builder.create(PullSpell::new, 3));
        public static SpellType<PushSpell> PUSH =
                SpellType.register(new Identifier(MOD_ID, "push"), SpellType.Builder.create(PushSpell::new, 3));
        public static SpellType<RewindSpell> REWIND =
                SpellType.register(new Identifier(MOD_ID, "rewind"), SpellType.Builder.create(RewindSpell::new, 7));
        public static SpellType<SuicideSpell> SUICIDE =
                SpellType.register(new Identifier(MOD_ID, "suicide"), SpellType.Builder.create(SuicideSpell::new, 1));
        public static SpellType<TeleportSpell> TELEPORT =
                SpellType.register(new Identifier(MOD_ID, "teleport"), SpellType.Builder.create(TeleportSpell::new, 5));
        public static SpellType<SupernovaSpell> SUPERNOVA =
                SpellType.register(new Identifier(MOD_ID, "supernova"), SpellType.Builder.create(SupernovaSpell::new, 20));
        public static SpellType<FireSpell> FIRE =
                SpellType.register(new Identifier(MOD_ID, "fire"), SpellType.Builder.create(FireSpell::new, 2));
        public static SpellType<IceSpell> ICE =
                SpellType.register(new Identifier(MOD_ID, "ice"), SpellType.Builder.create(IceSpell::new, 2));


        public static void init() {
            targetingSpells = List.of(Spells.PULL, Spells.PUSH, Spells.TELEPORT);
        }
    }

    public static boolean isClientVanilla(@Nullable ServerPlayerEntity player) {
        if(player != null && player.networkHandler != null) {
            var version = PolymerServerNetworking.getSupportedVersion(player.networkHandler, ChainSMPSpells.HAS_CLIENT_MODS);
            return version == -1;
        }
        return false;
    }

}
