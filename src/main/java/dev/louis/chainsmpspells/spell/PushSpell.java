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
        if(isCastable()) {
            var pulledPlayer = castedOn();
            if(pulledPlayer == null)return;
            Vec3d velocity = getCaster().getPos().subtract(pulledPlayer.getPos()).normalize().negate();
            pulledPlayer.setVelocity(velocity);
            pulledPlayer.velocityModified = true;
            drainMana();
        }
    }

    @Override
    public boolean isCastable() {
        //System.out.println("AFAS: " + getCaster().distanceTo(castedOn()));
        return super.isCastable()/* && getCaster().distanceTo(castedOn()) < 25*/;
    }
}
