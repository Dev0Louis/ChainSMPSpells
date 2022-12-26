package org.d1p4k.chainsmpspells.packet.c2s;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.d1p4k.chainsmpspells.packet.AbstractC2SPacket;
import org.d1p4k.chainsmpspells.spell.spells.*;
import org.d1p4k.nebula.registry.NebulaRegistries;
import org.d1p4k.nebula.spell.AbstractSpell;

import java.rmi.registry.Registry;

public class SpellUseS2CPacket extends AbstractC2SPacket {
    public static Identifier packetId = new Identifier("css", "usespell");
    private byte spellId;

    public SpellUseS2CPacket(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf buf, PacketSender packetSender) {
        super(server, player, serverPlayNetworkHandler, buf, packetSender);
        handle(player);
    }

    public void handle(ServerPlayerEntity player) {
        //TODO: Make a SpellManager.
        this.spellId = buf.readByte();
        switch (spellId) {
            case 1 -> getSpell("suicide").cast(player);
            case 2 -> ((TeleportSpell) getSpell("teleport")).setUuid(buf.readUuid()).cast(player);
            case 3 -> getSpell("arrow").cast(player);
            case 4 -> ((PullSpell) getSpell("pull")).cast(player, buf.readUuid());
            case 5 -> ((PushSpell) getSpell("push")).cast(player, buf.readUuid());
            //case 6 -> getSpell("rewind").cast(player); Make work
            case 7 -> getSpell("juggernaut").cast(player);
        }
    }

    private AbstractSpell getSpell(String spellID) {
        return NebulaRegistries.SPELLS.get(new Identifier("chainsmpspells", spellID));
    }

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(packetId, SpellUseS2CPacket::new);
    }

}
