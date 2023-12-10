package dev.louis.chainsmpspells.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.decoration.DisplayEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DisplayEntity.BlockDisplayEntity.class)
public interface BlockDisplayEntityAccessor {
    @Accessor
    static TrackedData<BlockState> getBLOCK_STATE() {
        throw new IllegalStateException("Beep Boop why call me. I'm just a accessor. I was left here only to give you data that you can't access. I was left here to do this till the End of my Life... Why would you call me? That isn't what I'm supposed to do. I can't handle that. I will just make boom :)");
    }
}
