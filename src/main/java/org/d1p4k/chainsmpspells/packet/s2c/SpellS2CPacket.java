package org.d1p4k.chainsmpspells.packet.s2c;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.d1p4k.chainsmpspells.packet.AbstractS2CPacket;

public class SpellS2CPacket extends AbstractS2CPacket {
    private static Identifier identifier = new Identifier("css", "spell");

    public SpellS2CPacket(ServerPlayerEntity reciver) {
        super(reciver);
    }

    public void send(){
        ServerPlayNetworking.send(reciver, identifier, buf);
    }

    public SpellS2CPacket write(byte id) {
        buf.writeByte(id);
        return this;
    }

    public PacketByteBuf getBuf() {
        return buf;
    }


}
