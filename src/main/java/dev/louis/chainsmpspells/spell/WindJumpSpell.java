package dev.louis.chainsmpspells.spell;

import dev.louis.nebula.api.spell.Spell;
import dev.louis.nebula.api.spell.SpellType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class WindJumpSpell extends Spell {
    public WindJumpSpell(SpellType<?> spellType) {
        super(spellType);
    }

    @Override
    public void cast() {
        this.getCaster().setVelocity(this.getCaster().getRotationVector().multiply(5, 3, 5));

        this.getCaster().velocityModified = true;
    }

    @Override
    public void tick() {
        if (this.getCaster() instanceof ServerPlayerEntity serverPlayer) {

            serverPlayer.getServerWorld().spawnParticles(
                    serverPlayer,
                    ParticleTypes.CLOUD,
                    false,
                    serverPlayer.getX(),
                    serverPlayer.getY(),
                    serverPlayer.getZ(),
                    2,
                    0.2,
                    0,
                    0.2,
                    0.1
            );
            this.getCaster().playSound(SoundEvents.BLOCK_GLASS_HIT, SoundCategory.PLAYERS, 2f, -1f);

            if (serverPlayer.isSneaking()) {
                stop();
            }
        }
    }

    @Override
    public void onEnd() {
        if(getCaster() instanceof ServerPlayerEntity serverPlayer) {
            if (this.wasInterrupted()) {
                this.getCaster().playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1f, -1f);
            }

            serverPlayer.playSound(SoundEvents.ENTITY_CAMEL_DASH, SoundCategory.PLAYERS, 2f, -1f);
            serverPlayer.getServerWorld().spawnParticles(
                    serverPlayer,
                    ParticleTypes.SMOKE,
                    false,
                    serverPlayer.getX(),
                    serverPlayer.getY(),
                    serverPlayer.getZ(),
                    5,
                    0,
                    1,
                    0,
                    0.1
            );
        }
    }

    @Override
    public int getDuration() {
        return 20;
    }
}
