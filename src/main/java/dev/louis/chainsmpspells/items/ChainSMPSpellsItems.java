package dev.louis.chainsmpspells.items;

import dev.louis.chainsmpspells.blocks.ChainSMPSpellsBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class ChainSMPSpellsItems {
    public static final Item SPELL_BOOK = Items.register(new Identifier("chainsmpspells:spell_book"), new SpellBookItem(new Item.Settings().fireproof().rarity(Rarity.RARE)));
    public static final Item SPELL_TABLE = Items.register(new Identifier("chainsmpspells:spell_table"), new SpellTableItem(ChainSMPSpellsBlocks.SPELL_TABLE, new Item.Settings().rarity(Rarity.EPIC)));
    public static void init() {
    }
}
