package dev.louis.chainsmpspells.spell;

import dev.louis.chainsmpspells.accessor.RewindPlayer;
import dev.louis.nebula.spell.Spell;
import dev.louis.nebula.spell.SpellType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

//TODO: Completely Rework!!! Manacost: 7
public class RewindSpell extends Spell {
    public RewindSpell(SpellType<? extends Spell> spellType, PlayerEntity caster) {
        super(spellType, caster);
    }

    @Override
    public void cast() {
        if(isCastable()) {
            RewindPlayer player = RewindPlayer.access((ServerPlayerEntity) getCaster());
            drainMana();
            player.chainsmpspells$addMemory((ServerWorld) getCaster().getWorld(), getCaster().getBlockPos(), getCaster().getVelocity(), getCaster().getYaw(), getCaster().getPitch(), 6*20);
        }
    }

    @Override
    public boolean isCastable() {
        return isCasterRemembering() && super.isCastable();
    }

    private boolean isCasterRemembering() {
        if(getCaster() instanceof ServerPlayerEntity serverPlayer) {
            return !RewindPlayer.access(serverPlayer).isRemembering();
        }
        return true;
    }
}
