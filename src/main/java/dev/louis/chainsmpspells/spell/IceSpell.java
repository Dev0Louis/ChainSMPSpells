package dev.louis.chainsmpspells.spell;

import dev.louis.nebula.spell.Spell;
import dev.louis.nebula.spell.SpellType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;

public class IceSpell extends AreaEffectSpell {
    public IceSpell(SpellType<? extends Spell> spellType, PlayerEntity caster) {
        super(spellType, caster, ParticleTypes.SNOWFLAKE, caster.getWorld().getDamageSources().playerAttack(caster), 20);
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
