package vazkii.quark.content.building.module;

import java.util.Optional;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import vazkii.quark.base.block.QuarkPaneBlock;
import vazkii.quark.base.handler.RenderLayerHandler.RenderTypeSkeleton;
import vazkii.quark.base.handler.StructureBlockReplacementHandler;
import vazkii.quark.base.handler.StructureBlockReplacementHandler.StructureHolder;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.module.QuarkModule;
import vazkii.quark.base.module.config.Config;

@LoadModule(category = ModuleCategory.BUILDING)
public class GoldBarsModule extends QuarkModule {

	@Config public static boolean generateInNetherFortress = true;

	public static boolean staticEnabled;

	public static Block gold_bars;

	@Override
	public void register() {
		gold_bars = new QuarkPaneBlock("gold_bars", this, Properties.copy(Blocks.IRON_BARS), RenderTypeSkeleton.CUTOUT);

		StructureBlockReplacementHandler.functions.add(GoldBarsModule::getGenerationBarBlockState);
	}

	@Override
	public void configChanged() {
		staticEnabled = enabled;
	}

	private static BlockState getGenerationBarBlockState(ServerLevelAccessor accessor, BlockState current, StructureHolder structure) {
		if(staticEnabled && generateInNetherFortress && current.getBlock() == Blocks.NETHER_BRICK_FENCE) {
			Optional<ResourceKey<Structure>> res = accessor.registryAccess().registry(Registry.STRUCTURE_REGISTRY).flatMap(
					(it) -> it.getResourceKey(structure.currentStructure));
			if (res.isEmpty())
				return null; // no change

			if(res.get().equals(BuiltinStructures.FORTRESS)) {
				return gold_bars.withPropertiesOf(current);
			}
		}

		return null; // no change
	}


}
