package dev.louis.chainsmpspells.spell;

import dev.louis.nebula.spell.Spell;
import dev.louis.nebula.spell.SpellType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

public class TeleportSpell extends TargetingSpell {
    public TeleportSpell(SpellType<? extends Spell> spellType, PlayerEntity caster) {
        super(spellType, caster);
    }

    @Override
    public void cast() {
        if(isCastable()) {
            drainMana();
            double x = castedOn().getX();
            double y = castedOn().getY();
            double z = castedOn().getZ();
            getCaster().world.playSound(null, BlockPos.ofFloored(getCaster().getX(),getCaster().getY(),getCaster().getZ()), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1, 1);
            getCaster().teleport(x, y, z, true);
            getCaster().world.playSound(null, BlockPos.ofFloored(x,y,z), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1, 1);

        }
    }

    @Override
    public boolean isCastable() {
        return super.isCastable() && getCaster().distanceTo(castedOn()) < 25;
    }
}
