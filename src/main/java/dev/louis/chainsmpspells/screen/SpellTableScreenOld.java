package dev.louis.chainsmpspells.screen;

public class SpellTableScreenOld /*extends HandledScreen<SpellTableScreenHandler>*/ {
    /**private static final Identifier TEXTURE = new Identifier("chainsmpspells","textures/gui/container/spell_table.png");

    public SpellTableScreenOld(SpellTableScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
        this.titleY = this.titleY - 2;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        int i = this.x;
        int j = (this.height - this.backgroundHeight) / 2;
        CraftingScreen.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }**/
}
