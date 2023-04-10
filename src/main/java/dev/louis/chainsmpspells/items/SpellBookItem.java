package dev.louis.chainsmpspells.items;

import dev.louis.nebula.api.NebulaPlayer;
import dev.louis.nebula.spell.SpellType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class SpellBookItem extends Item {
    public SpellBookItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack itemStack = playerEntity.getStackInHand(hand);
        if(world.isClient())return TypedActionResult.pass(itemStack);
        if(hand != Hand.MAIN_HAND)return TypedActionResult.pass(itemStack);

        Optional<SpellType<?>> spell = getSpell(itemStack);
        System.out.println(spell);
        AtomicBoolean wasSuccessful = new AtomicBoolean(false);
        spell.ifPresent(spellType -> {
            NebulaPlayer.access(playerEntity).getSpellKnowledge().addCastableSpell(spellType);
            wasSuccessful.set(true);
        });
        if(wasSuccessful.get()) {
            itemStack.decrement(1);
            return TypedActionResult.consume(itemStack);
        }
        return TypedActionResult.fail(itemStack);
    }



    public Optional<SpellType<?>> getSpell(ItemStack itemStack) {
        if(itemStack.getNbt() == null)return Optional.empty();
        if(!itemStack.getNbt().contains("spell"))return Optional.empty();
        System.out.println(Identifier.tryParse(itemStack.getNbt().getString("spell")));
        return SpellType.get(Identifier.tryParse(itemStack.getNbt().getString("spell")));
    }

    public static ItemStack createSpellBook(SpellType<?> spellType) {
        ItemStack itemStack = new ItemStack(ChainSMPSpellsItems.SPELL_BOOK);
        itemStack.getNbt().putString("spell", SpellType.getId(spellType).toString());
        return itemStack;
    }
}