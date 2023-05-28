package dev.louis.chainsmpspells.screen;

public class SpellTableScreenHandlerOld /*extends ScreenHandler*/ {
/**    //private final Inventory inventory;
    private static final int CRAFTING_SLOTS = 6;
    //private final CraftingInventory input = new CraftingInventory(this, CRAFTING_SLOTS, 1);

    final Slot inputSlot;
    final Slot outputSlot;
    private final World world;
    private List<SpellRecipe> availableRecipes = Lists.newArrayList();
    private final ScreenHandlerContext context;

    private final CraftingResultInventory result = new CraftingResultInventory();
    final CraftingResultInventory output = new CraftingResultInventory();
    private final Property selectedRecipe = Property.create();

    public final Inventory input = new SimpleInventory(1){
        @Override
        public void markDirty() {
            super.markDirty();
            SpellTableScreenHandlerOld.this.onContentChanged(this);
            //SpellTableScreenHandler.this.contentsChangedListener.run();
        }
    };

    public SpellTableScreenHandlerOld(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }


    public SpellTableScreenHandlerOld(int syncId, PlayerInventory playerInventory, final ScreenHandlerContext context) {
        super(ModRecipes.SPELL_TABLE, syncId);
        int i;
        this.context = context;
        this.world = playerInventory.player.getWorld();
        inputSlot = this.addSlot(new Slot(this.input, 0, 78, 38));
        this.outputSlot = this.addSlot(new Slot(this.output, 1, 143, 33){
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }
            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                stack.onCraft(player.world, player, stack.getCount());
                SpellTableScreenHandlerOld.this.output.unlockLastRecipe(player);
                ItemStack itemStack = SpellTableScreenHandlerOld.this.inputSlot.takeStack(1);
                if (!itemStack.isEmpty()) {
                    SpellTableScreenHandlerOld.this.populateResult();
                }
                super.onTakeItem(player, stack);
            }
        });

        final int y = 38;
        //addSlots(78,y, 28);


        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    private boolean isInBounds(int id) {
        return id >= 0 && id < this.availableRecipes.size();
    }

    void populateResult() {
        if (!this.availableRecipes.isEmpty() && this.isInBounds(this.selectedRecipe.get())) {
            SpellRecipe spellRecipe = this.availableRecipes.get(this.selectedRecipe.get());
            ItemStack itemStack = spellRecipe.craft(this.input, this.world.getRegistryManager());
            if (itemStack.isItemEnabled(this.world.getEnabledFeatures())) {
                this.output.setLastRecipe(spellRecipe);
                this.outputSlot.setStackNoCallbacks(itemStack);
            } else {
                this.outputSlot.setStackNoCallbacks(ItemStack.EMPTY);
            }
        } else {
            this.outputSlot.setStackNoCallbacks(ItemStack.EMPTY);
        }
        this.sendContentUpdates();
    }



    void addSlots(int x, int y, int radius) {
        double angle, dx1, dy1;
        int i;
        int x1, y1;

        for(i = 0; i < CRAFTING_SLOTS; i++)
        {
            angle = i*(360/CRAFTING_SLOTS);
            dx1 = radius * cos(angle * PI / 180);
            dy1 = radius * sin(angle * PI / 180);
            x1 = (int) Math.round(dx1);
            y1 = (int) Math.round(dy1);
            this.addSlot(new Slot(this.input, i, x+x1, y+y1));

        }
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        ItemStack itemStack = this.inputSlot.getStack();
        if (!itemStack.isOf(this.inputStack.getItem())) {
            this.inputStack = itemStack.copy();
            this.updateInput(inventory, itemStack);
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);
        if (slot2 != null && slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot < 9 ? !this.insertItem(itemStack2, 9, 45, true) : !this.insertItem(itemStack2, 0, 5, false)) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot2.setStack(ItemStack.EMPTY);
            } else {
                slot2.markDirty();
            }
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot2.onTakeItem(player, itemStack2);
        }
        return itemStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.dropInventory(player, this.input);
    }

    @Override
    public void populateRecipeFinder(RecipeMatcher finder) {
        System.out.println("a");
        this.input.provideRecipeInputs(finder);
    }

    @Override
    public void clearCraftingSlots() {
        this.input.clear();
        this.result.clear();
    }

    @Override
    public boolean matches(Recipe<? super CraftingInventory> recipe) {
        System.out.println("a: " + recipe.getIngredients());
        return recipe.matches(this.input, this.player.world);
    }
    @Override
    public int getCraftingResultSlotIndex() {
        return 0;
    }

    @Override
    public int getCraftingWidth() {
        return this.input.getWidth();
    }

    @Override
    public int getCraftingHeight() {
        return this.input.getHeight();
    }

    @Override
    public int getCraftingSlotCount() {
        return 6;
    }
    @Override
    public RecipeBookCategory getCategory() {
        return RecipeBookCategory.CRAFTING;
    }

    @Override
    public boolean canInsertIntoSlot(int index) {
        return index != this.getCraftingResultSlotIndex();
    }**/
}
