package dev.louis.chainsmpspells.networking;

import eu.pb4.polymer.networking.api.payload.VersionedPayload;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import xyz.nucleoid.packettweaker.PacketContext;

public record ICanHasChainSMPSpellsPayload() implements VersionedPayload {
    public static final Identifier ID = new Identifier("chainsmpspells", "icanhas");

    @Override
    public void write(PacketContext context, int version, PacketByteBuf buf) {
    }

    @Override
    public Identifier id() {
        return ID;
    }

    public static ICanHasChainSMPSpellsPayload read(PacketContext context, Identifier identifier, int version, PacketByteBuf buf) {
        return new ICanHasChainSMPSpellsPayload();
    }
}
