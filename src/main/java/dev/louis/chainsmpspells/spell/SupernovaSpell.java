package dev.louis.chainsmpspells.spell;

import dev.louis.nebula.spell.Spell;
import dev.louis.nebula.spell.SpellType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public class SupernovaSpell extends Spell {
    public SupernovaSpell(SpellType<? extends Spell> spellType, PlayerEntity caster) {
        super(spellType, caster);
    }

    @Override
    public void cast() {
        combust();
    }
    public void combust() {
        ((ServerPlayerEntity)getCaster()).getServerWorld().createExplosion(null, getCaster().getX(), getCaster().getY()+0.4, this.getCaster().getZ(), 16.0F, World.ExplosionSourceType.MOB);
        getCaster().setHealth(0f);
    }
}
