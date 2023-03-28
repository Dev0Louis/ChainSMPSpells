package dev.louis.chainsmpspells.accessor;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public interface RewindPlayer {
    public void clearMemory();
    public void addMemory(World memorizedWorld, BlockPos memorizedPos, Vec3d memorizedVelocity, float memorizedYaw, float memorizedPitch, int ticksTillMemory);
    public boolean isRemembering();
    public static RewindPlayer access(ServerPlayerEntity player) {
        return ((RewindPlayer)player);
    }
}
