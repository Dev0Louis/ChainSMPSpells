package dev.louis.chainsmpspells.mana.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.InstantStatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import dev.louis.nebula.api.NebulaPlayer;
import org.jetbrains.annotations.Nullable;

public class InstantManaStatusEffect extends InstantStatusEffect {
    public InstantManaStatusEffect() {
        super(StatusEffectCategory.BENEFICIAL,
                0x98D982);
    }

    @Override
    public void applyInstantEffect(@Nullable Entity source, @Nullable Entity attacker, LivingEntity target, int amplifier, double proximity) {
        if (target instanceof NebulaPlayer) {
            ((NebulaPlayer) target).getPlayerManaManager().addMana((amplifier+1)*5);
        }
    }
}