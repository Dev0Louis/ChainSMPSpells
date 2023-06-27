package dev.louis.chainsmpspells.spell;

import dev.louis.chainsmpspells.ChainSMPSpells;
import dev.louis.chainsmpspells.ChainSMPSpellsClient;
import dev.louis.chainsmpspells.config.ChainSMPSpellsConfig;
import dev.louis.nebula.spell.Spell;
import dev.louis.nebula.spell.SpellType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class TargetingSpell extends Spell {
    @Nullable
    private Entity castedOn;

    public TargetingSpell(SpellType<? extends Spell> spellType, PlayerEntity caster) {
        super(spellType, caster);
        if(caster.getWorld().isClient)ChainSMPSpellsClient.getPlayerInView().ifPresent(this::castedOn);
    }

    @Override
    public boolean isCastable() {
        return castedOn() != null && super.isCastable();
    }

    public Entity castedOn(Entity castedOn) {
        this.castedOn = castedOn;
        return castedOn;
    }
    public Entity castedOn() {
        return castedOn;
    }

    @Override
    public PacketByteBuf writeBuf(PacketByteBuf buf) {
        super.writeBuf(buf);
        Optional<Integer> optionalInteger = castedOn() != null ? Optional.of(castedOn().getId()) : Optional.empty();
        buf.writeOptional(optionalInteger, (PacketByteBuf::writeVarInt));
        return buf;
    }

    @Override
    public PacketByteBuf readBuf(PacketByteBuf buf) {
        super.readBuf(buf);
        Optional<Integer> o = buf.readOptional(PacketByteBuf::readVarInt);
        o.ifPresent((integer -> castedOn(getCaster().getWorld().getEntityById(integer))));
        return buf;
    }


    //Class to hold the logic to get the player, the player is looking at
    public static class TargetedPlayerSelector {
        private TargetedPlayerSelector(){}
        public static TargetedPlayerSelector INSTANCE = new TargetedPlayerSelector();
        private final MinecraftClient client = MinecraftClient.getInstance();
        //boolean hasTargetingSpell = false;
        boolean hasRaycastRun = false;
        int playerInViewScanTimeout = 0;
        PlayerEntity playerInView;

        public void init() {
            registerTickCallbacks();
        }

        private void registerTickCallbacks() {
            ClientTickEvents.END_WORLD_TICK.register(world -> {
                if(playerInViewScanTimeout > 0)playerInViewScanTimeout--;
                hasRaycastRun = false;
                //long tick = world.getTime();

                /**if(tick % 100 == 0) {
                    for (SpellType<? extends Spell> spellType : Nebula.NebulaRegistries.SPELL_TYPE) {
                        if (NebulaPlayer.access(client.player).getSpellManager().canCast()) {
                            INSTANCE.hasTargetingSpell = true;
                            break;
                        }
                    }
                }**/

            });
        }

        private PlayerEntity getPlayerInViewOrNull() {
            if(!hasRaycastRun) {
                INSTANCE.calculatePlayerInView();
            }
            return playerInView;
        }
        public Optional<PlayerEntity> getPlayerInView() {
            return Optional.ofNullable(getPlayerInViewOrNull());
        }

        private void calculatePlayerInView() {
            if(!shouldCalculatePlayerInView())return;

            playerInView = null;
            final ChainSMPSpellsConfig config = ChainSMPSpellsClient.INSTANCE.config;

            var pos = client.getCameraEntity().getEyePos();
            int divider = config.getRaycastScanPrecision();
            double i = 1.0 / divider;
            var x = client.getCameraEntity().getRotationVecClient().normalize().multiply(i);

            searchForPlayerInView:
            for (int y = 0; y < 24*divider; ++y) {
                for (PlayerEntity targetedPlayer : getPlayersWithoutSelf()) {
                    if(targetedPlayer.getBoundingBox().expand(0.3).contains(pos)) {
                        if(ChainSMPSpells.isPlayerTargetable(targetedPlayer)) {
                            playerInView = targetedPlayer;
                            playerInViewScanTimeout = 10;
                            break searchForPlayerInView;
                        }
                    }
                    pos = pos.add(x);
                }
            }
            hasRaycastRun = true;
        };

        private static Iterable<? extends PlayerEntity> getPlayersWithoutSelf() {
            final PlayerEntity clientPlayer = MinecraftClient.getInstance().player;
            var players = clientPlayer.getWorld().getPlayers();
            players.remove(clientPlayer);
            return players;
        }

        public boolean shouldCalculatePlayerInView() {
            return /*hasTargetingSpell &&*/ playerInViewScanTimeout == 0;
        }

    }
}
