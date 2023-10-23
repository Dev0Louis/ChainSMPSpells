package dev.louis.chainsmpspells.spell;

import dev.louis.nebula.spell.MultiTickSpell;
import dev.louis.nebula.spell.Spell;
import dev.louis.nebula.spell.SpellType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class MedusaSpell extends MultiTickSpell {
    public MedusaSpell(SpellType<? extends Spell> spellType, PlayerEntity caster) {
        super(spellType, caster);
    }

    @Override
    public void cast() {
        super.cast();
    }

    private boolean isCasterMedusa() {
        if(getCaster() instanceof ServerPlayerEntity serverPlayer) {
            return serverPlayer.getMultiTickSpells().stream().anyMatch(MedusaSpell.class::isInstance);
        }
        return false;
    }
}
