package dev.louis.chainsmpspells.spell;

import dev.louis.nebula.api.spell.SpellType;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;

;

public class IceSpell extends AreaEffectSpell {
    public IceSpell(SpellType<? extends AreaEffectSpell> spellType) {
        super(spellType, ParticleTypes.SNOWFLAKE, 20);
    }

    @Override
    protected void affect(Entity entity) {
        entity.setVelocity(Vec3d.ZERO);
        entity.velocityModified = true;
        entity.setFrozenTicks(100);
        entity.extinguishWithSound();
        super.affect(entity);
    }
}
