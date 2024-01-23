package dev.louis.chainsmpspells.spell;

import dev.louis.nebula.api.spell.Spell;
import dev.louis.nebula.api.spell.SpellType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public class SupernovaSpell extends Spell {
    public SupernovaSpell(SpellType<? extends Spell> spellType) {
        super(spellType);
    }

    @Override
    public void cast() {
        combust();
    }

    public void combust() {
        ((ServerPlayerEntity)getCaster()).getServerWorld().createExplosion(null, getCaster().getX(), getCaster().getY()+0.4, this.getCaster().getZ(), 16.0F, World.ExplosionSourceType.MOB);
        this.getCaster().setHealth(0f);
    }
}
