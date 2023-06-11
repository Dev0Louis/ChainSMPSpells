package dev.louis.chainsmpspells.blocks;

import dev.louis.chainsmpspells.screen.SpellTableScreenHandler;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
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
import org.jetbrains.annotations.Nullable;

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
        return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> new SpellTableScreenHandler(syncId, player.getInventory(), ScreenHandlerContext.create(world, pos)), TITLE);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(CHARGE);
    }

    public static class CustomScreenHandlerFactory implements NamedScreenHandlerFactory {


        @Override
        public Text getDisplayName() {
            return TITLE;
        }

        @Nullable
        @Override
        public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
            return null;
        }
    }
}
