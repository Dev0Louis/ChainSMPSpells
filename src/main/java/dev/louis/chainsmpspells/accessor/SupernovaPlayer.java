package dev.louis.chainsmpspells.accessor;

import net.minecraft.server.network.ServerPlayerEntity;

public interface SupernovaPlayer {
    public void startCombustion();
    public void defuseSupernova();
    public boolean isCombusting();
    public int getTicksTillSupernova();

        public static SupernovaPlayer access(ServerPlayerEntity player) {
        return ((SupernovaPlayer)player);
    }
}
