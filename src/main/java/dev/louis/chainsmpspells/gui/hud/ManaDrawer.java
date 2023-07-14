package dev.louis.chainsmpspells.gui.hud;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class ManaDrawer {
    public static void renderMana(Mana mana, DrawContext context, int x, int y) {
        switch (mana) {
            case FULL -> render(new Identifier("chainsmpspells","textures/gui/mana.png"), context, x, y);
            case HALF -> render(new Identifier("chainsmpspells","textures/gui/half_mana.png"), context, x, y);
            case EMPTY -> render(new Identifier("chainsmpspells","textures/gui/empty_mana.png"), context, x, y);
        }
    }

    private static void render(Identifier texture, DrawContext context, int x, int y) {
        context.drawTexture(texture, x, y, 9, 9, 9, 9, 9, 9);
    }

    public enum Mana{
        FULL,
        HALF,
        EMPTY
    }
}
