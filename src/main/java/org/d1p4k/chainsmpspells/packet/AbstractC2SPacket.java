package org.d1p4k.chainsmpspells.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class AbstractC2SPacket {
    protected MinecraftServer server;
    protected ServerPlayerEntity player;
    protected ServerPlayNetworkHandler serverPlayNetworkHandler;
    protected PacketByteBuf buf;
    protected PacketSender packetSender;

    public AbstractC2SPacket(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf buf, PacketSender packetSender) {
        this.server = server;
        this.player = player;
        this.serverPlayNetworkHandler = serverPlayNetworkHandler;
        this.buf = buf;
        this.packetSender = packetSender;
    }
}
