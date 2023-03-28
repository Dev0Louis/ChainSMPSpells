package dev.louis.chainsmpspells.spell;

import dev.louis.chainsmpspells.ChainSMPSpells;
import dev.louis.chainsmpspells.accessor.ArrowEntityAccessor;
import dev.louis.nebula.spell.Spell;
import dev.louis.nebula.spell.SpellType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ArrowSpell extends Spell {


    public ArrowSpell(SpellType<? extends Spell> spellType, PlayerEntity caster) {
        super(spellType, caster);
    }

    @Override
    public void cast() {
        if(isCastable()) {
            drainMana();
            var caster = getCaster();
            var world = caster.getWorld();
            var loc = caster.getEyePos();
            for (int x = -5; x < 5; x++) {
                for (int z = -5; z < 5; z++) {
                    ChainSMPSpells.server.executeSync(() -> {
                        ArrowEntity arrow = new ArrowEntity(world, caster);
                        ((ArrowEntityAccessor) arrow).shouldDamageOwner(false);

                        Vec3d vec3d = caster.getOppositeRotationVector(1.0F);
                        Quaternionf quaternionf = (new Quaternionf()).setAngleAxis(0 * 0.017453292F, vec3d.x, vec3d.y, vec3d.z);
                        Vec3d vec3d2 = caster.getRotationVec(1.0F);
                        Vector3f vector3f = vec3d2.toVector3f().rotate(quaternionf);
                        ((ProjectileEntity)arrow).setVelocity((double)vector3f.x(), (double)vector3f.y(), (double)vector3f.z(), 1.7f, 10f);
                        arrow.setPosition(loc);
                        world.spawnEntity(arrow);
                    });


                }
            }
        }
    }

    @Override
    public Identifier getID() {
        return new Identifier("chainsmpspells", "arrow");
    }


}
