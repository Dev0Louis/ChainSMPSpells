package dev.louis.chainsmpspells.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.louis.chainsmpspells.ChainSMPSpells;
import dev.louis.chainsmpspells.items.SpellBookItem;
import dev.louis.nebula.spell.SpellType;
import eu.pb4.polymer.core.api.item.PolymerRecipe;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SpellRecipe implements Recipe<Inventory>, PolymerRecipe {
    private final Ingredient ingredient;
    private final ItemStack result;
    public SpellRecipe(ItemStack result) {
        this.ingredient = Ingredient.ofItems(Items.BOOK);
        this.result = result;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    @Override
    public ItemStack getResult(DynamicRegistryManager registryManager) {
        return getResult();
    }

    public ItemStack getResult() {
        return result;
    }

    public Identifier getSpellIdentifier() {
        return Registries.ITEM.getId(getResult().getItem());
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        return inventory.getStack(0).isOf(Items.BOOK);
    }

    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        return this.getResult(registryManager).copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SpellRecipeSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.SPELL_RECIPE;
    }

    public static class SpellRecipeSerializer implements RecipeSerializer<SpellRecipe> {
        public static final Identifier ID = new Identifier("chainsmpspells:spell_recipe");
        public static final SpellRecipeSerializer INSTANCE = new SpellRecipeSerializer();
        private final Codec<SpellRecipe> codec;


        private SpellRecipeSerializer() {
            this.codec = RecordCodecBuilder.create(
                    instance -> instance.group(Identifier.CODEC.fieldOf("spell").forGetter(SpellRecipe::getSpellIdentifier))
                            .apply(
                                    instance,
                                    identifier -> new SpellRecipe(SpellBookItem.createSpellBook(SpellType.get(identifier).get()))
                            )
            );
        }

        @Override
        public Codec<SpellRecipe> codec() {
            return codec;
        }

        @Override
        public SpellRecipe read(PacketByteBuf buf) {
            ItemStack output = SpellBookItem.createSpellBook(SpellType.get(buf.readIdentifier()).get());
            return new SpellRecipe(output);
        }

        @Override
        public void write(PacketByteBuf buf, SpellRecipe recipe) {
            buf.writeIdentifier(SpellBookItem.getSpellType(recipe.result).get().getId());
        }
    }

    public Recipe<?> getPolymerReplacement(ServerPlayerEntity player) {
        if(ChainSMPSpells.isClientVanilla(player))return PolymerRecipe.createStonecuttingRecipe(this);
        return this;
    }
}
