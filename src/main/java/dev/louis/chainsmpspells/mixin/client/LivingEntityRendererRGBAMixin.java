package dev.louis.chainsmpspells.mixin.client;

import dev.louis.chainsmpspells.callback.EntityRenderRGBACallback;
import dev.louis.chainsmpspells.callback.EntityRenderRGBACallback.RGBA;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererRGBAMixin<T extends LivingEntity> {

    @ModifyArgs(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"))
    public void modifyRGBAValues(Args args, T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i)  {
        float red = args.get(4);
        float green = args.get(5);
        float blue = args.get(6);
        float alpha = args.get(7);
        RGBA rgba = new RGBA(red, green, blue, alpha);
        EntityRenderRGBACallback.EVENT.invoker().interact(livingEntity, rgba);
        args.set(4, rgba.getRed());
        args.set(5, rgba.getGreen());
        args.set(6, rgba.getBlue());
        args.set(7, rgba.getAlpha());
    }
}
