package org.d1p4k.chainsmpspells.spell.spells;

import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.d1p4k.chainsmpspells.ChainSMPSpells;
import org.d1p4k.chainsmpspells.packet.s2c.SpellS2CPacket;
import org.d1p4k.chainsmpspells.scheduler.RepeatingTask;
import org.d1p4k.nebula.spell.AbstractSpell;

import java.util.*;

public class RewindSpell extends AbstractSpell {

    public static List<RewindSpell> activeRewindSpells = new ArrayList<>();
    public static Identifier spellId = new Identifier("chainsmpspells" , "rewind");


    public RewindSpell(ServerPlayerEntity player, Identifier spellIdentifier, int cost) {
        super(player, spellIdentifier, cost);
        activeRewindSpells.add(this);
    }

    public RewindSpell(ServerPlayerEntity player, Identifier spellIdentifier) {
        this(player, spellIdentifier,7);
        activeRewindSpells.add(this);
    }
    public boolean canceled = false;
    public static Set<UUID> rewindingPlayer = new HashSet<>();
    public ArrayList<LocFallTicks> locationHistory = new ArrayList<>();
    class LocFallTicks {
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

    public Vec3d velocity;

    @Override
    public void cast() {
        if(rewindingPlayer.contains(player.getUuid()))return;

        if(check()) {
            rewindingPlayer.add(player.getUuid());
            velocity = player.getVelocity().negate();
            new RepeatingTask(5) {
                @Override
                public void run() {
                    if(locationHistory.size() > 6*4) {
                        new RepeatingTask(1) {
                            int i = (locationHistory.size()-1);
                            @Override
                            public void run() {
                                if(canceled) {
                                    rewindingPlayer.remove(player.getUuid());
                                    activeRewindSpells.remove(getThis());
                                    cancel();
                                    return;
                                }
                                if(i <= 0) {
                                    player.setVelocity(velocity);
                                    player.velocityModified = true;
                                    rewindingPlayer.remove(player.getUuid());
                                    activeRewindSpells.remove(getThis());
                                    cancel();
                                    return;
                                }
                                LocFallTicks a = locationHistory.get(i);
                                if(!player.world.getRegistryKey().equals(a.worldKey)) {
                                    player.moveToWorld(ChainSMPSpells.server.getWorld(a.worldKey));
                                }
                                player.teleport(a.loc.x, a.loc.y, a.loc.z, true);
                                ChainSMPSpells.server.getWorld(a.worldKey).spawnParticles(ParticleTypes.REVERSE_PORTAL, a.loc.x,a.loc.y,a.loc.z, 2,0,0,0, 0);
                                //ChainSMPSpells.server.getWorld(a.worldKey).sü(ChainSMPSpells.server.getWorld(a.worldKey), new BlockPos(a.loc.x, a.loc.y, a.loc.z), Direction.UP, ParticleTypes.CLOUD, new Vec3d(0,0,0), 1.0);
                                player.fallDistance = a.fallDistance;
                                i--;
                            }
                        };
                        cancel();
                        return;
                    }
                    if(locationHistory.size() % 4 == 0) {
                        SpellS2CPacket packet = new SpellS2CPacket((ServerPlayerEntity) player);
                        packet.write((byte) 6);
                        packet.getBuf().writeInt(6-locationHistory.size()/4);
                        packet.send();
                    }
                    locationHistory.add(new LocFallTicks(player.getWorld(), player.getPos(), player.fallDistance));
                }
            };
        }
    }

    @Override
    public Identifier getID() {
        return new Identifier("chainsmpspells", "rewind");
    }

    public boolean check() {
        return checkKnowledge() && checkMana();
    }

    public RewindSpell getThis() {
        return this;
    }

}
