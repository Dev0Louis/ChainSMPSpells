package dev.louis.chainsmpspells.spell;

import dev.louis.nebula.spell.Spell;
import dev.louis.nebula.spell.SpellType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

//3
public class PushSpell extends TargetingSpell {
    public PushSpell(SpellType<? extends Spell> spellType, PlayerEntity caster) {
        super(spellType, caster);
    }

    @Override
    public void cast() {
        var pulledPlayer = castedOn();
        if(pulledPlayer == null)return;
        Vec3d velocity = getCaster().getPos().subtract(pulledPlayer.getPos()).normalize().negate();
        pulledPlayer.setVelocity(velocity);
        pulledPlayer.velocityModified = true;
    }

    @Override
    public boolean isCastable() {
        return super.isCastable() && getCaster().distanceTo(castedOn()) < 25;
    }
}
