package dev.louis.chainsmpspells.spell;

import dev.louis.chainsmpspells.accessor.SupernovaPlayer;
import dev.louis.nebula.spell.Spell;
import dev.louis.nebula.spell.SpellType;
import net.minecraft.entity.player.PlayerEntity;

public class SupernovaSpell extends Spell {
    public SupernovaSpell(SpellType<? extends Spell> spellType, PlayerEntity caster) {
        super(spellType, caster);
    }

    @Override
    public void cast() {
        if(this.isCastable()) {
            drainMana();
            ((SupernovaPlayer)this.getCaster()).startCombustion();
        }
    }
}
