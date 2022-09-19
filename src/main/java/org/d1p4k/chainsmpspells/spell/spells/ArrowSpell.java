package org.d1p4k.chainsmpspells.spell.spells;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import org.d1p4k.chainsmpspells.ChainSMPSpells;
import org.d1p4k.chainsmpspells.accessor.ArrowEntityAccessor;
import org.d1p4k.nebula.spell.AbstractSpell;

public class ArrowSpell extends AbstractSpell {
    public static Identifier spellId = new Identifier("css" , "arrow");

    public ArrowSpell(PlayerEntity player, Identifier spellIdentifier, int cost) {
        super(player, spellIdentifier, cost);
    }

    public ArrowSpell(PlayerEntity player, Identifier spellIdentifier) {
        super(player, spellIdentifier, 2);
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
                        Quaternion quaternion = new Quaternion(new Vec3f(vec3d), 0.0f, true);
                        Vec3d vec3d2 = player.getRotationVec(1.0f);
                        Vec3f vec3f = new Vec3f(vec3d2);
                        vec3f.rotate(quaternion);
                        arrow.setVelocity(vec3f.getX(), vec3f.getY(), vec3f.getZ(), 1.7f, 100.0f);
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
