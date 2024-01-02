package dev.louis.chainsmpspells;

import dev.louis.chainsmpspells.config.ChainSMPSpellsConfig;
import dev.louis.chainsmpspells.keybind.SpellKeyBinding;
import dev.louis.chainsmpspells.keybind.SpellKeybindManager;
import dev.louis.chainsmpspells.recipe.ModRecipes;
import dev.louis.chainsmpspells.spell.TargetingSpell;
import dev.louis.nebula.spell.SpellType;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Optional;

public class ChainSMPSpellsClient implements ClientModInitializer {
    private static SpellKeybindManager spellKeybindManager;

    @Override
    public void onInitializeClient() {
        try {
            MidnightConfig.init(ChainSMPSpells.MOD_ID, ChainSMPSpellsConfig.class);
        } catch (Exception e) {
            ChainSMPSpells.LOGGER.warn("Something went wrong during Config init.");
        }
        TargetingSpell.TargetedPlayerSelector.init();

        createSpellKeyBind(ChainSMPSpells.Spells.ARROW, false);
        createSpellKeyBind(ChainSMPSpells.Spells.PULL, false);
        createSpellKeyBind(ChainSMPSpells.Spells.PUSH, false);
        createSpellKeyBind(ChainSMPSpells.Spells.REWIND, false);
        createSpellKeyBind(ChainSMPSpells.Spells.SUICIDE, false);
        createSpellKeyBind(ChainSMPSpells.Spells.TELEPORT, false);
        createSpellKeyBind(ChainSMPSpells.Spells.FIRE, false);
        createSpellKeyBind(ChainSMPSpells.Spells.ICE, false);
        createSpellKeyBind(ChainSMPSpells.Spells.SUPERNOVA, true);
        createSpellKeyBind(ChainSMPSpells.Spells.JUGGERNAUT, true);
        ModRecipes.initClient();
    }

    public static void createSpellKeyBind(SpellType<?> spellType, boolean hides){
        System.out.println(spellType);
        System.out.println(spellType.getId().getPath());
        System.out.println("LLLLLLLLLL\n");
        var keybind = KeyBindingHelper.registerKeyBinding(new SpellKeyBinding(spellType, hides));

        getSpellKeybindManager().setSpellKeyBinding(spellType, keybind);
    }

    public static boolean isPlayerTargetable(PlayerEntity targetedPlayer) {
        final var player = MinecraftClient.getInstance().player;
        return player != null && player.canSee(targetedPlayer) && player.isPartOfGame() && !(targetedPlayer.isCreative() || targetedPlayer.isSpectator() || targetedPlayer.isInvisibleTo(targetedPlayer) || player.isSpectator());
    }
    public static SpellKeybindManager getSpellKeybindManager() {
        if(spellKeybindManager != null)return spellKeybindManager;
        return (spellKeybindManager = new SpellKeybindManager());
    }

    public static Optional<PlayerEntity> getPlayerInView() {
        return TargetingSpell.TargetedPlayerSelector.getPlayerInView();
    }
}
