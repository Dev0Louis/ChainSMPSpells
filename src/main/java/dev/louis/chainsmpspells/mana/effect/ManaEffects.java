package dev.louis.chainsmpspells.mana.effect;

import net.minecraft.entity.effect.InstantStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ManaEffects {

    private static Potion MANA_INSTANT;
    private static Potion STRONG_MANA_INSTANT;
    private static Potion MANA_REGENERATION;
    private static Potion STRONG_MANA_REGENERATION;
    private static InstantStatusEffect MANA_INSTANT_EFFECT = new InstantManaStatusEffect();
    private static StatusEffect MANA_REGENERATION_EFFECT = new ManaRegenerationStatusEffect();


    public static void init() {
        registerPotionEffects();
        registerPotions();
        registerPotionRecipe();
    }

    private static void registerPotionEffects() {
        register("chainsmpspells:instant_mana", MANA_INSTANT_EFFECT);
        register("chainsmpspells:mana_regeneration", MANA_REGENERATION_EFFECT);
    }

    private static void registerPotions() {
        MANA_INSTANT = register("chainsmpspells:instant_mana", new Potion("Instant_Mana", new StatusEffectInstance(MANA_INSTANT_EFFECT, 1, 1)));
        STRONG_MANA_INSTANT = register("chainsmpspells:strong_instant_mana", new Potion("Strong_Instant_Mana", new StatusEffectInstance(MANA_INSTANT_EFFECT, 1, 2)));
        MANA_REGENERATION = register("chainsmpspells:mana_regeneration", new Potion("Mana_Regeneration", new StatusEffectInstance(MANA_REGENERATION_EFFECT, 5)));
        STRONG_MANA_REGENERATION = register("chainsmpspells:strong_mana_regeneration", new Potion("Strong_Mana_Regeneration", new StatusEffectInstance(MANA_REGENERATION_EFFECT, 5, 1)));

    }

    private static void registerPotionRecipe() {
        BrewingRecipeRegistry.registerPotionRecipe(Potions.AWKWARD, Items.CHORUS_FLOWER, MANA_INSTANT);
        BrewingRecipeRegistry.registerPotionRecipe(MANA_INSTANT, Items.CHORUS_FLOWER, STRONG_MANA_INSTANT);
    }




    private static Potion register(String name, Potion potion) {
        return Registry.register(Registries.POTION, name, potion);
    }



    private static StatusEffect register(String id, StatusEffect entry) {
        return Registry.register(Registries.STATUS_EFFECT, id, entry);
    }
}
