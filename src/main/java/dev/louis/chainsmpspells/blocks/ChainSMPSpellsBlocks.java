package dev.louis.chainsmpspells.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ChainSMPSpellsBlocks {
    public static final Block SPELL_TABLE = Registry.register(Registries.BLOCK, new Identifier("chainsmpspells", "spell_table"), new SpellTableBlock(FabricBlockSettings.of(Material.WOOD).strength(4.0f)));
    public static void init(){
    }
}
