package dev.louis.chainsmpspells.gui.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class ManaDrawer {
    public static void renderMana(Mana mana, DrawContext context, int x, int s) {
        switch (mana) {
            case FULL -> render(new Identifier("chainsmpspells","textures/gui/mana.png"), context, x, s);
            case HALF -> render(new Identifier("chainsmpspells","textures/gui/half_mana.png"), context, x, s);
            case EMPTY -> render(new Identifier("chainsmpspells","textures/gui/empty_mana.png"), context, x, s);
        }
    }

    private static void render(Identifier id, DrawContext context, int x, int s) {
        RenderSystem.setShaderTexture(0, id);
        context.drawTexture(id, x, s, 9, 9, 9, 9, 9, 9);
    }

    public enum Mana{
        FULL,
        HALF,
        EMPTY
    }
}
