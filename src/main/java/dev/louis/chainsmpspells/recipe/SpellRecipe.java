package dev.louis.chainsmpspells.recipe;

import com.google.gson.JsonObject;
import dev.louis.chainsmpspells.items.SpellBookItem;
import dev.louis.nebula.spell.SpellType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.world.World;

import java.util.Optional;

public class SpellRecipe implements Recipe<Inventory> {
    private final Identifier id;
    private final ItemStack input;
    private final ItemStack output;
    public SpellRecipe(Identifier id, ItemStack input, ItemStack output) {
        this.id = id;
        this.input = input;
        this.output = output;
    }

    public ItemStack getInput() {
        return input;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return getOutput();
    }

    public ItemStack getOutput() {
        return output;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        return inventory.getStack(0).isOf(Items.BOOK);
    }

    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        return this.getOutput(registryManager).copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.SPELL_RECIPE;
    }

    public static class SpellRecipeSerializer implements RecipeSerializer<SpellRecipe> {
        public static final Identifier ID = new Identifier("chainsmpspells:spell_recipe");
        public static final SpellRecipeSerializer INSTANCE = new SpellRecipeSerializer();

        private SpellRecipeSerializer() {}

        @Override
        public SpellRecipe read(Identifier id, JsonObject json) {
            String spell = json.get("spell").getAsString();

            if(!Identifier.isValid(spell)) throw new InvalidIdentifierException(spell + " is not a valid identifier!");
            Optional<SpellType<?>> optionalSpellType = SpellType.get(new Identifier(spell));
            if(optionalSpellType.isEmpty())throw new InvalidIdentifierException(spell + " is not a valid spell!");

            ItemStack input = new ItemStack(Items.BOOK);
            ItemStack output = SpellBookItem.createSpellBook(optionalSpellType.get());
            return new SpellRecipe(id, input, output);
        }

        @Override
        public SpellRecipe read(Identifier id, PacketByteBuf buf) {
            ItemStack input = new ItemStack(Items.BOOK);
            ItemStack output = SpellBookItem.createSpellBook(SpellType.get(buf.readIdentifier()).get());
            return new SpellRecipe(id, input, output);
        }

        @Override
        public void write(PacketByteBuf buf, SpellRecipe recipe) {
            buf.writeIdentifier(SpellType.getId(SpellBookItem.getSpellType(recipe.output).get()));
        }
    }

    @Override
    public String toString() {
        return "SpellRecipe{" +
                "id=" + id +
                '}';
    }
}
