package dev.louis.chainsmpspells.spell.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.louis.chainsmpspells.ChainSMPSpellsClient;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.List;

public class SpellEffectRenderer {

    public static List<Vec3d> positions = new ArrayList<>();

    public static void render(WorldRenderContext context) {
        ChainSMPSpellsClient.getPlayerInView().ifPresent(player -> {;
            Camera camera = context.camera();

            Vec3d targetPosition = /*new Vec3d(0.5, -60, 0.5);*/ player.getPos();
            Vec3d transformedPosition = targetPosition.subtract(camera.getPos());


            MatrixStack matrixStack = context.matrixStack();
            matrixStack.push();
            matrixStack.translate(transformedPosition.x, transformedPosition.y, transformedPosition.z);
            matrixStack.scale(3, 3, 3);
            Quaternionf rotation = new Quaternionf();
            rotation.rotationYXZ((float) (-Math.PI / 180.0) * (camera.getYaw() - 180F), (float) (Math.PI / 180.0) * -(camera.getPitch()), 0.0F);
            matrixStack.multiply(rotation);


            Matrix4f positionMatrix = matrixStack.peek().getPositionMatrix();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();

            buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
            int color = Colors.RED;
            buffer.vertex(positionMatrix, 0, 1, 0).color(color).texture(0f, 0f).next();
            buffer.vertex(positionMatrix, 0, 0, 0).color(color).texture(0f, 1f).next();
            buffer.vertex(positionMatrix, 1, 0, 0).color(color).texture(1f, 1f).next();
            buffer.vertex(positionMatrix, 1, 1, 0).color(color).texture(1f, 0f).next();

            RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);
            RenderSystem.setShaderTexture(0, new Identifier("chainsmpspells", "textures/other/octagram.png"));
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
            RenderSystem.disableCull();
            //RenderSystem.depthFunc(GL11.GL_ALWAYS);

            tessellator.draw();

            //RenderSystem.depthFunc(GL11.GL_LEQUAL);
            RenderSystem.enableCull();
            matrixStack.pop();
        });
    }
}
