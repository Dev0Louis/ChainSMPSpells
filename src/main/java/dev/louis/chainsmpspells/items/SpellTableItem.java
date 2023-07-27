package dev.louis.chainsmpspells.items;

import dev.louis.chainsmpspells.ChainSMPSpells;
import eu.pb4.polymer.core.api.item.PolymerBlockItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

public class SpellTableItem extends PolymerBlockItem {
    public SpellTableItem(Block block, Settings settings) {
        super(block, settings, Items.ENCHANTING_TABLE);
    }


    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        if(ChainSMPSpells.isClientVanilla(player))return super.getPolymerItem(itemStack, player);
        return this;
    }

}
