package dev.louis.chainsmpspells;

import dev.louis.chainsmpspells.accessor.SupernovaClientPlayer;
import dev.louis.chainsmpspells.config.ChainSMPSpellsConfig;
import dev.louis.chainsmpspells.keybind.SpellKeybindManager;
import dev.louis.chainsmpspells.network.SupernovaS2CPacket;
import dev.louis.chainsmpspells.spell.TargetingSpell;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
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
    public TargetingSpell.TargetedPlayerSelector targetedPlayerSelector = TargetingSpell.TargetedPlayerSelector.INSTANCE;


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
        setupSupernovaSpell();
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
    public  SpellKeybindManager getSpellKeybindManager() {
        if(spellKeybindManager != null)return spellKeybindManager;
        return (spellKeybindManager = new SpellKeybindManager());
    }


    private void setupSupernovaSpell() {
        ClientPlayNetworking.registerGlobalReceiver(SupernovaS2CPacket.TYPE, ((packet, player, responseSender) -> {
            PlayerEntity combustingPlayer = player.getWorld().getPlayerByUuid(packet.uuid());
            if(player.getUuid().equals(packet.uuid())) combustingPlayer = player;
            if(combustingPlayer instanceof AbstractClientPlayerEntity abstractClientPlayerEntity) {
                SupernovaClientPlayer.access(abstractClientPlayerEntity).setCombustion(packet.ticks());
            }
        }));
        /**EntityRenderRGBACallback.EVENT.register(((livingEntity, rgba) -> {
            if(livingEntity instanceof AbstractClientPlayerEntity combustingPlayer && isPlayerCombusting(combustingPlayer))rgba.setBlue(0);
        }));**/
    }


    public static Optional<PlayerEntity> getPlayerInView() {
        return INSTANCE.targetedPlayerSelector.getPlayerInView();
    }
    public boolean isPlayerCombusting(AbstractClientPlayerEntity player) {
        int playerCombustionTimer = SupernovaClientPlayer.access(player).getCombustionTime();
        return playerCombustionTimer > 0;
    }
    
}
