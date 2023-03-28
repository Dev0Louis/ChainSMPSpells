package dev.louis.chainsmpspells.accessor;

import net.minecraft.server.network.ServerPlayerEntity;

public interface ServerPlayerEntityJuggernautModeAccessor {

    public static ServerPlayerEntityJuggernautModeAccessor access(ServerPlayerEntity serverPlayer) {
        return (ServerPlayerEntityJuggernautModeAccessor) serverPlayer;
    }
    public void setJuggernautModeTick(int ticks);
    public int getJuggernautTick();
    public void makeJuggernaut(int ticks);

    public boolean isInJuggernautMode();
    public boolean isNotInJuggernautMode();
    public ServerPlayerEntity getPlayer();
}
