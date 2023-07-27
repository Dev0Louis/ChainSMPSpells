package dev.louis.chainsmpspells.items;

import dev.louis.chainsmpspells.ChainSMPSpells;
import dev.louis.nebula.spell.SpellType;
import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.item.PolymerItemUtils;
import eu.pb4.polymer.core.api.utils.PolymerClientDecoded;
import eu.pb4.polymer.core.api.utils.PolymerKeepModel;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SpellBookItem extends Item implements PolymerItem, PolymerKeepModel, PolymerClientDecoded {
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

    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        if(ChainSMPSpells.isClientVanilla(player))return Items.BOOK;
        return itemStack.getItem();
    }

    @Override
    public ItemStack getPolymerItemStack(ItemStack itemStack, TooltipContext context, @Nullable ServerPlayerEntity player) {
        if(ChainSMPSpells.isClientVanilla(player))return PolymerItemUtils.createItemStack(itemStack, context, player);
        return itemStack;
    }
}