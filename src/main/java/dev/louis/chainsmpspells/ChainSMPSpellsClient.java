package dev.louis.chainsmpspells;

import dev.louis.chainsmpspells.config.ChainSMPSpellsConfig;
import dev.louis.chainsmpspells.keybind.SpellKeybindManager;
import dev.louis.chainsmpspells.recipe.ModRecipes;
import dev.louis.chainsmpspells.spell.TargetingSpell;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.glfw.GLFW;

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

        spellKeybindManager = getSpellKeybindManager();
        spellKeybindManager.setSpellKeyBinding(ChainSMPSpells.Spells.ARROW, createKeyBind("arrow"));
        spellKeybindManager.setSpellKeyBinding(ChainSMPSpells.Spells.JUGGERNAUT, createKeyBind("juggernaut"));
        spellKeybindManager.setSpellKeyBinding(ChainSMPSpells.Spells.PULL, createKeyBind("pull"));
        spellKeybindManager.setSpellKeyBinding(ChainSMPSpells.Spells.PUSH, createKeyBind("push"));
        spellKeybindManager.setSpellKeyBinding(ChainSMPSpells.Spells.REWIND, createKeyBind("rewind"));
        spellKeybindManager.setSpellKeyBinding(ChainSMPSpells.Spells.SUICIDE, createKeyBind("suicide"));
        spellKeybindManager.setSpellKeyBinding(ChainSMPSpells.Spells.TELEPORT, createKeyBind("teleport"));
        spellKeybindManager.setSpellKeyBinding(ChainSMPSpells.Spells.SUPERNOVA, createKeyBind("supernova"));
        spellKeybindManager.setSpellKeyBinding(ChainSMPSpells.Spells.FIRE, createKeyBind("fire"));
        spellKeybindManager.setSpellKeyBinding(ChainSMPSpells.Spells.ICE, createKeyBind("ice"));
        ModRecipes.initClient();
    }

    public static KeyBinding createKeyBind(String name, int glfw){
        return KeyBindingHelper.registerKeyBinding(new KeyBinding(
                        "key.chainsmpspells.spell." + name,
                        InputUtil.Type.KEYSYM,
                        glfw,
                        "category.chainsmpspells.spells"
                )
        );
    }

    public static boolean isPlayerTargetable(PlayerEntity targetedPlayer) {
        final var player = MinecraftClient.getInstance().player;
        return player != null && player.canSee(targetedPlayer) && player.isPartOfGame() && !(targetedPlayer.isCreative() || targetedPlayer.isSpectator() || targetedPlayer.isInvisibleTo(targetedPlayer) || player.isSpectator());
    }

    public static KeyBinding createKeyBind(String name){
        return createKeyBind(name, GLFW.GLFW_KEY_UNKNOWN);
    }
    public static SpellKeybindManager getSpellKeybindManager() {
        if(spellKeybindManager != null)return spellKeybindManager;
        return (spellKeybindManager = new SpellKeybindManager());
    }

    public static Optional<PlayerEntity> getPlayerInView() {
        return TargetingSpell.TargetedPlayerSelector.getPlayerInView();
    }
}
