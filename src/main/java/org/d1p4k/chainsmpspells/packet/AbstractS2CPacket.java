package org.d1p4k.chainsmpspells.packet;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class AbstractS2CPacket {
    protected PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
    protected ServerPlayerEntity reciver;
    public AbstractS2CPacket(ServerPlayerEntity reciver) {
        this.reciver = reciver;
    }
}
