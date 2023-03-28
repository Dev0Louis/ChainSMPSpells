package dev.louis.chainsmpspells.gui.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ManaDrawer extends DrawableHelper {
    public static void renderMana(Mana mana, MatrixStack matrices, int x, int s) {
        switch (mana) {
            case FULL -> render(new Identifier("chainsmpspells","textures/gui/mana.png"), matrices, x, s);
            case HALF -> render(new Identifier("chainsmpspells","textures/gui/half_mana.png"), matrices, x, s);
            case EMPTY -> render(new Identifier("chainsmpspells","textures/gui/empty_mana.png"), matrices, x, s);
        }
    }

    private static void render(Identifier id, MatrixStack matrices, int x, int s) {
        RenderSystem.setShaderTexture(0, id);
        drawTexture(matrices, x, s, 9, 9, 9, 9, 9, 9);
    }

    public enum Mana{
        FULL,
        HALF,
        EMPTY
    }
}
