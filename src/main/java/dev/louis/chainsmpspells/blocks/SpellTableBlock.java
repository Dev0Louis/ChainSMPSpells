package dev.louis.chainsmpspells.blocks;

import dev.louis.chainsmpspells.screen.SpellTableScreenHandler;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
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
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class SpellTableBlock extends Block {
    private static final Text TITLE = Text.translatable("container.spell_crafting");
    public static final int MAX_CHARGE = 32;
    public static final int MIN_CHARGE = 0;
    public static final IntProperty CHARGE = IntProperty.of("charge", MIN_CHARGE, MAX_CHARGE);
    public SpellTableBlock(AbstractBlock.Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(CHARGE, MIN_CHARGE));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient)return ActionResult.SUCCESS;
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

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        final int charge = state.get(CHARGE);
        if(charge == MIN_CHARGE)return;
        final int r = random.nextInt(MAX_CHARGE);
        //System.out.println(r + " <= " + charge + " = " + (r <= charge));
        if(r <= charge) {
            spawnRandomParticle(random, world, pos);
            spawnRandomParticle(random, world, pos);
            spawnRandomParticle(random, world, pos);
        }
    }

    private void spawnRandomParticle(Random random, World world, BlockPos pos) {

        double offsetX = random.nextTriangular(0.5, 0.25);
        double offsetZ = random.nextTriangular(0.5, 0.25);

        world.addParticle(ParticleTypes.SOUL, pos.getX()+offsetX, pos.getY()+0.75, pos.getZ()+offsetZ, 0, 0.2*random.nextDouble(), 0);
    }
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Blocks.ENCHANTING_TABLE.getOutlineShape(state, world, pos, context);
    }

    public static int getLightLevel(BlockState state) {
        return state.get(CHARGE) / 3 + 4;
    }
}

