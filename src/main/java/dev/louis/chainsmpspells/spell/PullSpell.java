package dev.louis.chainsmpspells.spell;

import dev.louis.nebula.api.spell.Spell;
import dev.louis.nebula.api.spell.SpellType;
import net.minecraft.util.math.Vec3d;

public class PullSpell extends TargetingSpell {
    public PullSpell(SpellType<? extends Spell> spellType) {
        super(spellType);
    }

    @Override
    public void cast() {
        var pulledPlayer = castedOn();
        if(pulledPlayer == null)return;
        Vec3d velocity = getCaster().getPos().subtract(pulledPlayer.getPos()).normalize();
        pulledPlayer.setVelocity(velocity);
        pulledPlayer.velocityModified = true;
    }
}
