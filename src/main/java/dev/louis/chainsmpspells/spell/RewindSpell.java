package dev.louis.chainsmpspells.spell;

import dev.louis.chainsmpspells.ChainSMPSpells;
import dev.louis.nebula.spell.MultiTickSpell;
import dev.louis.nebula.spell.Spell;
import dev.louis.nebula.spell.SpellType;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.TeleportTarget;

//TODO: Completely Rework!!! Manacost: 7
public class RewindSpell extends MultiTickSpell {
    public RewindSpell(SpellType<? extends Spell> spellType, PlayerEntity caster) {
        super(spellType, caster);
    }
    private ServerWorld rewindWorld;
    private TeleportTarget rewindTarget;
    @Override
    public void cast() {
        super.cast();
        rewindWorld = (ServerWorld) getCaster().getWorld();
        rewindTarget = new TeleportTarget(getCaster().getPos(), getCaster().getVelocity(), getCaster().getYaw(), getCaster().getPitch());
        ChainSMPSpells.LOGGER.debug("Player setting rewindTarget to " + rewindTarget.position + " and rewindWorld to " + rewindWorld.getRegistryKey().getValue());
    }

    @Override
    public void tick() {
        super.tick();
        if (!(this.getCaster() instanceof ServerPlayerEntity serverPlayer))return;
        if(spellAge % 10 == 0) playPingSound(serverPlayer);
        if(spellAge > 6*20) {
            double x = rewindTarget.position.getX();
            double y = rewindTarget.position.getY();
            double z = rewindTarget.position.getY();

            rewindWorld.spawnParticles(
                    ParticleTypes.REVERSE_PORTAL,
                    x,
                    y,
                    z,
                    2,
                    0,
                    0,
                    0,
                    0
            );
            ChainSMPSpells.LOGGER.debug("Player is rewinding to " + rewindTarget.position + " in " + rewindWorld.getRegistryKey().getValue());
            FabricDimensions.teleport(serverPlayer, rewindWorld, rewindTarget);

            stop();
        }
    }


    @Override
    public boolean isCastable() {
        return !isCasterRemembering() && super.isCastable();
    }

    private boolean isCasterRemembering() {
        if(getCaster() instanceof ServerPlayerEntity serverPlayer) {
            return serverPlayer.getMultiTickSpells().stream().anyMatch(RewindSpell.class::isInstance);
        }
        return false;
    }

    private void playPingSound(ServerPlayerEntity player) {
        player.getServerWorld().playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.BLOCK_NOTE_BLOCK_BANJO.value(),
                player.getSoundCategory(),
                1,
                -1
        );
    }

    private void playRewindSound(ServerPlayerEntity player) {
        player.getServerWorld().playSound(
                null,
                rewindTarget.position.getX(),
                rewindTarget.position.getY(),
                rewindTarget.position.getZ(),
                SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                player.getSoundCategory(),
                1,
                1
        );
    }
}
