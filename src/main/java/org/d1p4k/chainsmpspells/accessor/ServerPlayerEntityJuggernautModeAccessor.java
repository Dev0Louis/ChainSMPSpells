package org.d1p4k.chainsmpspells.accessor;

import net.minecraft.server.network.ServerPlayerEntity;

public interface ServerPlayerEntityJuggernautModeAccessor {

    public static ServerPlayerEntityJuggernautModeAccessor access(ServerPlayerEntity serverPlayer) {
        return (ServerPlayerEntityJuggernautModeAccessor) serverPlayer;
    }
    public void setJuggernautModeTick(long ticks);
    public long getJuggernautTick();

    public boolean isInJuggernautMode();
    public boolean isNotInJuggernautMode();
    public ServerPlayerEntity getPlayer();
}
