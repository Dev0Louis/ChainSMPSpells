package dev.louis.chainsmpspells.spell;

import dev.louis.nebula.spell.MultiTickSpell;
import dev.louis.nebula.spell.Spell;
import dev.louis.nebula.spell.SpellType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import static java.lang.Math.*;

public abstract class AreaEffectSpell extends MultiTickSpell {
    private final ParticleEffect particle;
    private final DamageSource damageSource;
    private final int duration;

    private final Box spellCastingBox;

    public AreaEffectSpell(
            SpellType<? extends Spell> spellType,
            PlayerEntity caster,
            ParticleEffect particle,
            DamageSource damageSource,
            int duration
    ) {
        super(spellType, caster);
        this.particle = particle;
        this.damageSource = damageSource;
        this.duration = duration;
        this.spellCastingBox = getSpellCastingBox(caster);
    }

    @Override
    public void tick() {
        super.tick();
        if(getCaster() instanceof ServerPlayerEntity serverPlayer && spellAge < duration) {
            spawnParticles(serverPlayer.getServerWorld());
            for (Entity entity : getCaster().getWorld().getOtherEntities(getCaster(), spellCastingBox)) {
                affect(entity);
            }
        }else {
            stop();
        }
    }

    /**
     * This method can be overridden to run something when a Entity is affected by the spell.
     */
    protected void affect(Entity entity) {
        entity.damage(damageSource, 1);
    };

    private static Box getSpellCastingBox(PlayerEntity player) {
        Vec3d playerRotation = player.getRotationVec(1.0f).normalize();
        double x = playerRotation.x;
        double z = playerRotation.z;

        double absX = abs(x);
        double absZ = abs(z);

        final double threshold = 0.4;
        final double minLockRotation = 0.35;

        if(absX + absZ < threshold) {
            x = copySign(max(absX, minLockRotation), x);
            z = copySign(max(absZ, minLockRotation), z);
        }
        Vec3d adjustedRotation = new Vec3d(x, 0, z);
        final double multiplier = 3;
        final double yOffset = -0.3;

        return player.getBoundingBox().stretch(adjustedRotation).expand(1.0, -0.5, 1.0).offset(adjustedRotation.multiply(multiplier).add(0, yOffset, 0));
    }

    private void spawnParticles(ServerWorld world) {
        for (double x = spellCastingBox.minX; x < spellCastingBox.maxX; x = x + 0.5) {
            for (double y = spellCastingBox.minY; y < spellCastingBox.maxY; y = y + 0.5) {
                for (double z = spellCastingBox.minZ; z < spellCastingBox.maxZ; z = z + 0.5) {
                    world.spawnParticles(particle, x, y, z, 1, 0.1, 0.1, 0.1, 0.01);
                }
            }
        }
    }
}
