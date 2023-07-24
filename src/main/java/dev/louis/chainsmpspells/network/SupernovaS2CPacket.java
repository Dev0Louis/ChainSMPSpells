package dev.louis.chainsmpspells.network;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.UUID;

@Deprecated(forRemoval = true)
public record SupernovaS2CPacket(UUID uuid, int ticks) implements FabricPacket {
    public static final PacketType<SupernovaS2CPacket> TYPE = PacketType.create(new Identifier("chainsmpspells:supernova"), SupernovaS2CPacket::new);
    public SupernovaS2CPacket(PacketByteBuf buf) {
        this(buf.readUuid(), buf.readInt());
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeUuid(uuid);
        buf.writeInt(ticks);
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
