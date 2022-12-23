package org.d1p4k.chainsmpspells.spell.spells;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.joml.Quaterniond;
import org.joml.Quaternionf;
import net.minecraft.util.math.Vec3d;
import org.d1p4k.chainsmpspells.ChainSMPSpells;
import org.d1p4k.chainsmpspells.accessor.ArrowEntityAccessor;
import org.d1p4k.nebula.spell.AbstractSpell;
import org.joml.Vector3f;

public class ArrowSpell extends AbstractSpell {
    public static Identifier spellId = new Identifier("css" , "arrow");

    public ArrowSpell(ServerPlayerEntity player, Identifier spellIdentifier, int cost) {
        super(player, spellIdentifier, cost);
    }

    public ArrowSpell(ServerPlayerEntity player, Identifier spellIdentifier) {
        this(player, spellIdentifier, 2);
    }

    @Override
    public void cast() {
        if(check()) {
            var world = this.player.getWorld();
            //var loc = this.player.getEyePos().add(0, 0.6, 0);
            var loc = this.player.getEyePos();
            for (int x = -5; x < 5; x++) {
                for (int z = -5; z < 5; z++) {
                    ChainSMPSpells.server.executeSync(() -> {
                        ArrowEntity arrow = new ArrowEntity(world, player);
                        ((ArrowEntityAccessor) arrow).shouldDamageOwner(false);
                        Vec3d vec3d = player.getOppositeRotationVector(1.0f);
                        Quaternionf quaternion = new Quaternionf(vec3d.getX(), vec3d.getY(), vec3d.getZ(), 1.0f);
                        Vec3d vec3d2 = player.getRotationVec(1.0f);
                        Vector3f vec3f = vec3d2.toVector3f();
                        vec3f.rotate(quaternion);
                        arrow.setVelocity(vec3f.x(), vec3f.y(), vec3f.z(), 1.7f, 100.0f);
                        arrow.setPosition(loc);
                        world.spawnEntity(arrow);
                    });


                }
            }
        }
    }

    public boolean check() {
        return checkKnowledge() && checkMana();
    }

}
