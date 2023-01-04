package vazkii.quark.addons.oddities.magnetsystem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.collect.Streams;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.quark.addons.oddities.block.be.MagnetBlockEntity;
import vazkii.quark.addons.oddities.module.MagnetsModule;
import vazkii.quark.api.IMagnetMoveAction;
import vazkii.quark.api.IMagnetTracker;
import vazkii.quark.api.QuarkCapabilities;
import vazkii.quark.base.handler.MiscUtil;

public class MagnetSystem {

	private static final HashSet<Block> magnetizableBlocks = new HashSet<>();
	private static  Multimap<Item, Item> recipeDigestion = HashMultimap.create();

	private static final HashMap<Block, IMagnetMoveAction> BLOCK_MOVE_ACTIONS = new HashMap<>();

	static {
		DefaultMoveActions.addActions(BLOCK_MOVE_ACTIONS);
	}

	public static IMagnetMoveAction getMoveAction(Block block) {
		return BLOCK_MOVE_ACTIONS.get(block);
	}

	public static LazyOptional<IMagnetTracker> getCapability(Level world) {
		return world.getCapability(QuarkCapabilities.MAGNET_TRACKER_CAPABILITY);
	}

	public static void tick(boolean start, Level level) {
		if (start) {
			getCapability(level).ifPresent(IMagnetTracker::clear);
		} else {
			getCapability(level).ifPresent(magnetTracker -> {
				for (BlockPos pos : magnetTracker.getTrackedPositions())
					magnetTracker.actOnForces(pos);
				magnetTracker.clear();
			});
		}
	}

	public static void onRecipeReset() {
		magnetizableBlocks.clear();
	}
	
	public static void onStartCrawl() {
		recipeDigestion.clear();
	}

	public static void onVisit(Recipe<?> recipe) {
		Item out = recipe.getResultItem().getItem();
		
		NonNullList<Ingredient> ingredients = recipe.getIngredients();
		for(Ingredient ingredient : ingredients) {
			for (ItemStack inStack : ingredient.getItems())
				recipeDigestion.put(inStack.getItem(), out);
		}
	}
	
	public static void onFinishCrawl() {
		List<Item> magneticDerivationList = MiscUtil.massRegistryGet(MagnetsModule.magneticDerivationList, ForgeRegistries.ITEMS);
		List<Item> magneticWhitelist = MiscUtil.massRegistryGet(MagnetsModule.magneticWhitelist, ForgeRegistries.ITEMS);
		List<Item> magneticBlacklist = MiscUtil.massRegistryGet(MagnetsModule.magneticBlacklist, ForgeRegistries.ITEMS);

		Streams.concat(magneticDerivationList.stream(), magneticWhitelist.stream())
		.filter(i -> i instanceof BlockItem)
		.map(i -> ((BlockItem) i).getBlock())
		.forEach(magnetizableBlocks::add);

		Set<Item> scanned = Sets.newHashSet(magneticDerivationList);
		List<Item> magnetizableToScan = Lists.newArrayList(magneticDerivationList);

		while (!magnetizableToScan.isEmpty()) {
			Item scan = magnetizableToScan.remove(0);

			if (recipeDigestion.containsKey(scan)) {
				for (Item candidate : recipeDigestion.get(scan)) {
					if (!scanned.contains(candidate)) {
						scanned.add(candidate);
						magnetizableToScan.add(candidate);

						if(candidate instanceof BlockItem blockItem && !magneticBlacklist.contains(candidate))
							magnetizableBlocks.add(blockItem.getBlock());
					}
				}
			}
		}
		
		System.out.println("Magnetizable Blocks is " + magnetizableBlocks);
	}
	
	public static void applyForce(Level world, BlockPos pos, int magnitude, boolean pushing, Direction dir, int distance, BlockPos origin) {
		getCapability(world).ifPresent(magnetTracker ->
		magnetTracker.applyForce(pos, magnitude, pushing, dir, distance, origin));
	}

	public static PushReaction getPushAction(MagnetBlockEntity magnet, BlockPos pos, BlockState state, Direction moveDir) {
		Level world = magnet.getLevel();
		if(world != null && isBlockMagnetic(state)) {
			BlockPos targetLocation = pos.relative(moveDir);
			BlockState stateAtTarget = world.getBlockState(targetLocation);
			if (stateAtTarget.isAir())
				return PushReaction.IGNORE;
			else if (stateAtTarget.getPistonPushReaction() == PushReaction.DESTROY)
				return PushReaction.DESTROY;
		}

		return PushReaction.BLOCK;
	}

	public static boolean isBlockMagnetic(BlockState state) {
		Block block = state.getBlock();

		if (block == Blocks.PISTON || block == Blocks.STICKY_PISTON) {
			if (state.getValue(PistonBaseBlock.EXTENDED))
				return false;
		}

		return block != MagnetsModule.magnet && (magnetizableBlocks.contains(block) || BLOCK_MOVE_ACTIONS.containsKey(block) || block instanceof IMagnetMoveAction);
	}
}
