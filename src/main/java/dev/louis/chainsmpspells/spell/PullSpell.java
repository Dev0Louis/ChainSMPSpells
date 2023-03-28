package dev.louis.chainsmpspells.spell;

import dev.louis.nebula.spell.Spell;
import dev.louis.nebula.spell.SpellType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class PullSpell extends TargetingSpell {
    public PullSpell(SpellType<? extends Spell> spellType, PlayerEntity caster) {
        super(spellType, caster);
    }

    @Override
    public void cast() {
        if(isCastable()) {
            drainMana();
            var pulledPlayer = castedOn();
            if(pulledPlayer == null)return;
            Vec3d velocity = getCaster().getPos().subtract(pulledPlayer.getPos()).normalize();
            pulledPlayer.setVelocity(velocity);
            pulledPlayer.velocityModified = true;
        }
    }

    @Override
    public boolean isCastable() {
        return super.isCastable() && getCaster().distanceTo(castedOn()) < 25;
    }

}
