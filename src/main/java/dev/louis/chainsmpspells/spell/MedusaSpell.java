package dev.louis.chainsmpspells.spell;

import dev.louis.nebula.spell.SpellType;
import dev.louis.nebula.spell.TickingSpell;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class MedusaSpell extends TickingSpell {
    public MedusaSpell(SpellType<? extends TickingSpell> spellType, PlayerEntity caster) {
        super(spellType, caster);
    }

    @Override
    public void cast() {
        super.cast();
    }

    @Override
    public boolean isCastable() {
        return !isCasterMedusa() && super.isCastable();
    }

    private boolean isCasterMedusa() {
        if(getCaster() instanceof ServerPlayerEntity serverPlayer) {
            return serverPlayer.getSpellManager().isSpellTicking(this);
        }
        return false;
    }
}
