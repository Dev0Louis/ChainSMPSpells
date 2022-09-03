package org.d1p4k.chainsmpspells.packet;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;

public abstract class AbstractS2CPacket {
    protected PacketByteBuf buf;
    public AbstractS2CPacket() {
        buf = new PacketByteBuf(Unpooled.buffer());
    }

}
