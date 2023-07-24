package dev.louis.chainsmpspells;

import dev.louis.chainsmpspells.config.ChainSMPSpellsConfig;
import dev.louis.chainsmpspells.keybind.SpellKeybindManager;
import dev.louis.chainsmpspells.recipe.ModRecipes;
import dev.louis.chainsmpspells.spell.TargetingSpell;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.glfw.GLFW;

import java.util.Optional;

public class ChainSMPSpellsClient implements ClientModInitializer {
    public static ChainSMPSpellsClient INSTANCE;
    public static final MinecraftClient client = MinecraftClient.getInstance();
    public ChainSMPSpellsConfig config;
    private SpellKeybindManager spellKeybindManager;
    public final TargetingSpell.TargetedPlayerSelector targetedPlayerSelector = TargetingSpell.TargetedPlayerSelector.INSTANCE;


    @Override
    public void onInitializeClient() {
        INSTANCE = this;
        try {
            AutoConfig.register(ChainSMPSpellsConfig.class, JanksonConfigSerializer::new);
            config = AutoConfig.getConfigHolder(ChainSMPSpellsConfig.class).getConfig();
        } catch (Exception e) {
            config = new ChainSMPSpellsConfig();
            ChainSMPSpells.LOGGER.info("Autoconfig couldn't be registered.");
        }
        targetedPlayerSelector.init();

        spellKeybindManager = getSpellKeybindManager();
        spellKeybindManager.setSpellKeyBinding(ChainSMPSpells.Spells.ARROW, createKeyBind("arrow"));
        spellKeybindManager.setSpellKeyBinding(ChainSMPSpells.Spells.JUGGERNAUT, createKeyBind("juggernaut"));
        spellKeybindManager.setSpellKeyBinding(ChainSMPSpells.Spells.PULL, createKeyBind("pull"));
        spellKeybindManager.setSpellKeyBinding(ChainSMPSpells.Spells.PUSH, createKeyBind("push"));
        spellKeybindManager.setSpellKeyBinding(ChainSMPSpells.Spells.REWIND, createKeyBind("rewind"));
        spellKeybindManager.setSpellKeyBinding(ChainSMPSpells.Spells.SUICIDE, createKeyBind("suicide"));
        spellKeybindManager.setSpellKeyBinding(ChainSMPSpells.Spells.TELEPORT, createKeyBind("teleport"));
        spellKeybindManager.setSpellKeyBinding(ChainSMPSpells.Spells.SUPERNOVA, createKeyBind("supernova"));
        ModRecipes.init_client();
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

    public static KeyBinding createKeyBind(String name){
        return createKeyBind(name, GLFW.GLFW_KEY_UNKNOWN);
    }
    public  SpellKeybindManager getSpellKeybindManager() {
        if(spellKeybindManager != null)return spellKeybindManager;
        return (spellKeybindManager = new SpellKeybindManager());
    }

    public static Optional<PlayerEntity> getPlayerInView() {
        return INSTANCE.targetedPlayerSelector.getPlayerInView();
    }
}
