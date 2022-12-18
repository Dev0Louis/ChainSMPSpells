package org.d1p4k.chainsmpspells;

import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;
import org.d1p4k.chainsmpspells.block.ManaFlowerBlock;
import org.d1p4k.chainsmpspells.block.ManaPlantBlock;

public class ChainSMPSpellBlocks {
        public static final Block MANA_PLANT;
        public static final Block MANA_FLOWER;

        static {
                MANA_PLANT = register("chainsmpspells:mana_plant", new ManaPlantBlock(AbstractBlock.Settings.of(Material.PLANT, MapColor.PURPLE).strength(0.4f).sounds(BlockSoundGroup.WOOD).nonOpaque()));
                MANA_FLOWER = register("chainsmpspells:mana_flower", new ManaFlowerBlock((ManaPlantBlock) MANA_PLANT, AbstractBlock.Settings.of(Material.PLANT, MapColor.PURPLE).ticksRandomly().strength(0.4f).sounds(BlockSoundGroup.WOOD).nonOpaque()));
        }

        private static Block register(String id, Block block) {
                return Registry.register(Registry.BLOCK, id, block);
        }

        public static void init(){}
}