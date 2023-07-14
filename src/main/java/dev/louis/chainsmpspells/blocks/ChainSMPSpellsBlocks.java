package dev.louis.chainsmpspells.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ChainSMPSpellsBlocks {
    public static final Block SPELL_TABLE = Registry.register(Registries.BLOCK, new Identifier("chainsmpspells", "spell_table"), new SpellTableBlock(FabricBlockSettings.create().sounds(BlockSoundGroup.DEEPSLATE_BRICKS).mapColor(MapColor.BLACK).strength(4.0f).ticksRandomly().luminance(SpellTableBlock::getLightLevel)));
    public static void init(){
    }
}
