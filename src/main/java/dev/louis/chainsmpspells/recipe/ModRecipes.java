package dev.louis.chainsmpspells.recipe;

import dev.louis.chainsmpspells.screen.SpellTableScreen;
import dev.louis.chainsmpspells.screen.SpellTableScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class ModRecipes {
    public static final RecipeType<SpellRecipe> SPELL_RECIPE = RecipeType.register("chainsmpspells:spell_recipe");
    public static final ScreenHandlerType<SpellTableScreenHandler> SPELL_TABLE = register("chainsmpspells:spellcraft", SpellTableScreenHandler::new);
    public static void init() {
        Registry.register(Registries.RECIPE_SERIALIZER, SpellRecipe.SpellRecipeSerializer.ID, SpellRecipe.SpellRecipeSerializer.INSTANCE);
    }

    public static void init_client() {
        HandledScreens.register(SPELL_TABLE, SpellTableScreen::new);
    }

    private static <T extends ScreenHandler> ScreenHandlerType<T> register(String id, ScreenHandlerType.Factory<T> factory) {
        return Registry.register(Registries.SCREEN_HANDLER, id, new ScreenHandlerType<T>(factory, FeatureFlags.VANILLA_FEATURES));
    }
}
