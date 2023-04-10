package dev.louis.chainsmpspells.spell;

import dev.louis.chainsmpspells.accessor.RewindPlayer;
import dev.louis.nebula.spell.Spell;
import dev.louis.nebula.spell.SpellType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

//TODO: Completely Rework!!! Manacost: 7
public class RewindSpell extends Spell {
    public RewindSpell(SpellType<? extends Spell> spellType, PlayerEntity caster) {
        super(spellType, caster);
    }

    static class LocFallTicks {
        public RegistryKey<World> worldKey;
        public Vec3d loc;
        public float fallDistance;
        public LocFallTicks(World world, Vec3d loc, float fallDistance) {
            this.worldKey = world.getRegistryKey();
            this.loc = loc;
            this.fallDistance = fallDistance;
        }

        @Override
        public String toString() {
            return "["+worldKey+","+loc+","+fallDistance+"]";
        }
    }

    @Override
    public void cast() {
        if(isCastable()) {
            RewindPlayer player = RewindPlayer.access((ServerPlayerEntity) getCaster());
            drainMana();
            player.addMemory(getCaster().getWorld(), getCaster().getBlockPos(), getCaster().getVelocity(), getCaster().getYaw(), getCaster().getPitch(), 6*20);

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
    public void sendActionBarUpdate(int time) {
        getCaster().sendMessage(Text.translatable("message.chainsmpspells.cooldown", time), true);
    }
}
