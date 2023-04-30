package dev.louis.chainsmpspells;

import dev.louis.nebula.Nebula;
import dev.louis.nebula.spell.SpellType;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import dev.louis.chainsmpspells.config.ChainSMPSpellsConfig;
import dev.louis.chainsmpspells.keybind.SpellKeybindManager;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.glfw.GLFW;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChainSMPSpellsClient implements ClientModInitializer {
    public static ChainSMPSpellsClient INSTANCE;
    public static MinecraftClient client = MinecraftClient.getInstance();
    public ChainSMPSpellsConfig config;
    private SpellKeybindManager spellKeybindManager;

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
        registerTickCallbacks();
        spellKeybindManager = getSpellKeybindManager();
        spellKeybindManager.setSpellKeyBinding(ChainSMPSpells.Spells.ARROW, createKeyBind("arrow"));
        spellKeybindManager.setSpellKeyBinding(ChainSMPSpells.Spells.JUGGERNAUT, createKeyBind("juggernaut"));
        spellKeybindManager.setSpellKeyBinding(ChainSMPSpells.Spells.PULL, createKeyBind("pull"));
        spellKeybindManager.setSpellKeyBinding(ChainSMPSpells.Spells.PUSH, createKeyBind("push"));
        spellKeybindManager.setSpellKeyBinding(ChainSMPSpells.Spells.REWIND, createKeyBind("rewind"));
        spellKeybindManager.setSpellKeyBinding(ChainSMPSpells.Spells.SUICIDE, createKeyBind("suicide"));
        spellKeybindManager.setSpellKeyBinding(ChainSMPSpells.Spells.TELEPORT, createKeyBind("teleport"));
        spellKeybindManager.setSpellKeyBinding(ChainSMPSpells.Spells.SUPERNOVA, createKeyBind("supernova"));

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

    private static Collection<AbstractClientPlayerEntity> getPlayersWithoutSelf() {
        var players = client.world.getPlayers();
        players.remove(client.player);
        return players;
    }

    public  SpellKeybindManager getSpellKeybindManager() {
        if(spellKeybindManager != null)return spellKeybindManager;
        return (spellKeybindManager = new SpellKeybindManager());
    }

    private static final Collection<SpellType> targetingSpells = List.of(ChainSMPSpells.Spells.PULL, ChainSMPSpells.Spells.PUSH, ChainSMPSpells.Spells.TELEPORT);
    static int playerHasTargetingSpellCooldown = 0;
    boolean hasTargetingSpell = false;
    private static void registerTickCallbacks() {
        ClientTickEvents.END_CLIENT_TICK.register(mcClient -> {
            if(playerInViewScanTimeout > 0) {
                playerInViewScanTimeout--;
            }


            calculatedPlayerInView = false;

            if(playerHasTargetingSpellCooldown > 0) {
                playerHasTargetingSpellCooldown = 100;
                AtomicBoolean atomicHasTargetingSpell = new AtomicBoolean(false);

                Nebula.NebulaRegistries.SPELL_TYPE.forEach((spellType -> {
                    atomicHasTargetingSpell.set(targetingSpells.contains(spellType));
                }));

                INSTANCE.hasTargetingSpell = atomicHasTargetingSpell.get();
                return;
            }

            playerHasTargetingSpellCooldown--;

        });
    }

    static boolean calculatedPlayerInView = false;
    static int playerInViewScanTimeout = 0;
    static PlayerEntity playerInView;
    public static PlayerEntity getPlayerInViewOrNull() {
        if(!calculatedPlayerInView) {
            INSTANCE.calculatePlayerInView();
        }
        return playerInView;
    }
    public static Optional<PlayerEntity> getPlayerInView() {
        return Optional.ofNullable(getPlayerInViewOrNull());
    }

    private void calculatePlayerInView() {
        if(hasTargetingSpell || playerInViewScanTimeout > 0)return;

        var pos = client.getCameraEntity().getEyePos();
        int divider = config.getRaycastScanPrecision();
        double i = 1.0 / divider;
        var x = client.getCameraEntity().getRotationVecClient().normalize().multiply(i);
        calculatedPlayerInView = true;
        for (int y = 0; y < 24*divider; ++y) {
            for (AbstractClientPlayerEntity otherPlayer : getPlayersWithoutSelf()) {
                if(otherPlayer.getBoundingBox().expand(0.5).contains(pos)) {
                    if(client.player.canSee(otherPlayer)) {
                        playerInView = otherPlayer;
                        playerInViewScanTimeout = 20;
                        return;
                    }
                }
                pos = pos.add(x);
            }
        }
        playerInView = null;
    };
    
}
