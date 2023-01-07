package vazkii.quark.content.building.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import vazkii.quark.base.block.IQuarkBlock;
import vazkii.quark.base.block.QuarkPillarBlock;
import vazkii.quark.base.handler.RenderLayerHandler;
import vazkii.quark.base.handler.RenderLayerHandler.RenderTypeSkeleton;
import vazkii.quark.base.module.QuarkModule;

public class HollowLogBlock extends QuarkPillarBlock {

	private static final VoxelShape SHAPE_BOTTOM = Block.box(0F, 0F, 0F, 16F, 2F, 16F);
	private static final VoxelShape SHAPE_TOP = Block.box(0F, 14F, 0F, 16F, 16F, 16F);
	private static final VoxelShape SHAPE_NORTH = Block.box(0F, 0F, 0F, 2F, 16F, 16F);
	private static final VoxelShape SHAPE_SOUTH = Block.box(14F, 0F, 0F, 16F, 16F, 16F);
	private static final VoxelShape SHAPE_EAST = Block.box(0F, 0F, 0F, 16F, 16F, 2F);
	private static final VoxelShape SHAPE_WEST = Block.box(0F, 0F, 14F, 16F, 16F, 16F);

	private static final VoxelShape SHAPE_X = Shapes.or(SHAPE_BOTTOM, SHAPE_TOP, SHAPE_EAST, SHAPE_WEST);
	private static final VoxelShape SHAPE_Y = Shapes.or(SHAPE_NORTH, SHAPE_SOUTH, SHAPE_EAST, SHAPE_WEST);
	private static final VoxelShape SHAPE_Z = Shapes.or(SHAPE_BOTTOM, SHAPE_TOP, SHAPE_NORTH, SHAPE_SOUTH);

	public HollowLogBlock(Block sourceLog, QuarkModule module) {
		super(IQuarkBlock.inherit(sourceLog, "hollow_%s"), module, CreativeModeTab.TAB_DECORATIONS, 
				Properties.copy(sourceLog)
				.noOcclusion()
				.isSuffocating((s,g,p) -> false));

		RenderLayerHandler.setRenderType(this, RenderTypeSkeleton.CUTOUT_MIPPED);
	}

	@Override
	public boolean useShapeForLightOcclusion(BlockState p_56967_) {
		return false;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter p_60573_, BlockPos p_60574_, CollisionContext p_60575_) {
		return switch(state.getValue(AXIS)) {
		case X -> SHAPE_X;
		case Y -> SHAPE_Y;
		case Z -> SHAPE_Z;
		};
	}
	
}

