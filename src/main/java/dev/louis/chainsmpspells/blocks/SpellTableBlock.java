package dev.louis.chainsmpspells.blocks;

import dev.louis.chainsmpspells.screen.SpellTableScreenHandler;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpellTableBlock extends Block {
    private static final Text TITLE = Text.translatable("container.spell_crafting");
    public static final IntProperty CHARGE = IntProperty.of("charge",0, 32);
    public SpellTableBlock(AbstractBlock.Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(CHARGE, 0));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient)return ActionResult.SUCCESS;

        if(player.getStackInHand(hand).isOf(Items.GLOWSTONE)) {
            int oldCharge = state.get(CHARGE);
            if(oldCharge == 32)return ActionResult.FAIL;
            world.setBlockState(pos, state.with(CHARGE, oldCharge+1));
            return ActionResult.CONSUME;
        }
        player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
        //player.incrementStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
        return ActionResult.CONSUME;
    }

    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        final Property property = new Property() {

            @Override
            public int get() {
                return world.getBlockState(pos).get(CHARGE);
            }

            @Override
            public void set(int value) {
                world.setBlockState(pos, state.with(CHARGE, value));
            }
        };

        return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> new SpellTableScreenHandler(syncId, player.getInventory(), property, ScreenHandlerContext.create(world, pos)), TITLE);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(CHARGE);
    }
}

