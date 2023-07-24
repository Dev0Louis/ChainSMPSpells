package dev.louis.chainsmpspells.accessor;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public interface RewindPlayer {
    public void chainsmpspells$clearMemory();
    public void chainsmpspells$addMemory(ServerWorld memorizedWorld, BlockPos memorizedPos, Vec3d memorizedVelocity, float memorizedYaw, float memorizedPitch, int ticksTillMemory);
    public boolean isRemembering();
    public static RewindPlayer access(ServerPlayerEntity player) {
        return ((RewindPlayer)player);
    }
}
