package dev.louis.chainsmpspells.spell;

import dev.louis.nebula.spell.SpellType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;

public class FireSpell extends AreaEffectSpell {
    public FireSpell(SpellType<? extends AreaEffectSpell> spellType, PlayerEntity caster) {
        super(spellType, caster, ParticleTypes.FLAME, caster.getWorld().getDamageSources().playerAttack(caster), 20);
    }

    @Override
    protected void affect(Entity entity) {
        entity.setVelocity(entity.getPos().subtract(getCaster().getPos()).normalize().add(0, 1, 0));
        entity.velocityModified = true;
        entity.setFireTicks(100);
        super.affect(entity);
    }
}
