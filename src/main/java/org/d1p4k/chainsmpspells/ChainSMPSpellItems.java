package org.d1p4k.chainsmpspells;

import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import org.d1p4k.chainsmpspells.knowledge.items.SpellBookItem;

public class ChainSMPSpellItems {
        public static final Item SPELL_BOOK;
        public static Item MANA_FRUIT;

        public static final Item MANA_PLANT;
        public static final Item MANA_FLOWER;


        static {
                MANA_FLOWER = register(ChainSMPSpellBlocks.MANA_FLOWER, ItemGroup.DECORATIONS);
                MANA_PLANT = register(ChainSMPSpellBlocks.MANA_PLANT, ItemGroup.DECORATIONS);
                MANA_FRUIT = register("chainsmpspells:mana_fruit",new ChorusFruitItem(new Item.Settings().group(ItemGroup.MATERIALS).food(FoodComponents.CHORUS_FRUIT)));
                SPELL_BOOK = register(new Identifier("chainsmpspells:spell_book"), new SpellBookItem(new Item.Settings().group(ItemGroup.MISC).fireproof().rarity(Rarity.RARE)));

        }



        private static Item register(Block block, ItemGroup group) {
                return register(new BlockItem(block, new Item.Settings().group(group)));
        }

        private static Item register(BlockItem item) {
                return register(item.getBlock(), item);
        }

        protected static Item register(Block block, Item item) {
                return register(Registry.BLOCK.getId(block), item);
        }


        private static Item register(Identifier id, Item item) {
                if (item instanceof BlockItem) {
                        ((BlockItem)item).appendBlocks(Item.BLOCK_ITEMS, item);
                }
                return Registry.register(Registry.ITEM, id, item);
        }

        private static Item register(String id, Item item) {
                return register(new Identifier(id), item);
        }
        public static void init(){}

}