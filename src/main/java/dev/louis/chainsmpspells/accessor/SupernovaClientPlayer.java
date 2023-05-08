package dev.louis.chainsmpspells.accessor;

import net.minecraft.client.network.AbstractClientPlayerEntity;

public interface SupernovaClientPlayer {
    public void setCombustion(int time);
    public boolean isCombusting();
    public int getCombustionTime();

        public static SupernovaClientPlayer access(AbstractClientPlayerEntity player) {
        return ((SupernovaClientPlayer)player);
    }
}
