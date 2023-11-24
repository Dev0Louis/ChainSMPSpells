package dev.louis.chainsmpspells.spell;

import dev.louis.chainsmpspells.entity.HailstoneEntity;
import dev.louis.nebula.spell.Spell;
import dev.louis.nebula.spell.SpellType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class HailstoneSpell extends Spell {
    public HailstoneSpell(SpellType<? extends Spell> spellType, PlayerEntity caster) {
        super(spellType, caster);
    }

    @Override
    public void cast() {
        var castedOn = getCaster();
        if(castedOn == null) return;
        castedOn.getServer().executeSync(() -> {
            for (int i = 0; i < 100; i++) {
                var random = castedOn.getRandom();
                BlockPos hailstonePos = castedOn.getBlockPos().add(random.nextBetween(-6, 6), random.nextBetween(3, 6), random.nextBetween(-6, 6));
                HailstoneEntity hailstone = HailstoneEntity.HAILSTONE.create(castedOn.getWorld());
                assert hailstone != null;
                hailstone.setPosition(hailstonePos.toCenterPos());
                castedOn.getWorld().spawnEntity(hailstone);
            }
        });
    }
}
