package dev.louis.chainsmpspells.items;

import dev.louis.nebula.spell.SpellType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Optional;

public class SpellBookItem extends Item {
    public SpellBookItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack itemStack = playerEntity.getStackInHand(hand);
        if(world.isClient())return TypedActionResult.pass(itemStack);
        if(hand != Hand.MAIN_HAND)return TypedActionResult.pass(itemStack);

        Optional<SpellType<?>> optionalSpellType = getSpellType(itemStack);
        if(optionalSpellType.isPresent()) {
            playerEntity.getSpellManager().addSpell(optionalSpellType.get());
            itemStack.decrement(1);
            return TypedActionResult.consume(itemStack);
        }
        return TypedActionResult.fail(itemStack);
    }



    public static Optional<SpellType<?>> getSpellType(ItemStack itemStack) {
        if(!itemStack.hasNbt() || itemStack.getNbt() == null)return Optional.empty();
        String spell = itemStack.getNbt().getString("spell");
        return SpellType.get(Identifier.tryParse(spell));
    }

    public static ItemStack createSpellBook(SpellType<?> spellType) {
        ItemStack itemStack = new ItemStack(ChainSMPSpellsItems.SPELL_BOOK);
        itemStack.getOrCreateNbt().putString("spell", spellType.getId().toString());
        return itemStack;
    }
}