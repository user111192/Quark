package vazkii.quark.content.world.block;

import java.util.OptionalInt;

import javax.annotation.Nonnull;

import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import vazkii.quark.base.block.QuarkSaplingBlock;
import vazkii.quark.base.module.QuarkModule;
import vazkii.quark.content.world.module.AncientWoodModule;

public class AncientSaplingBlock extends QuarkSaplingBlock {

	public AncientSaplingBlock(QuarkModule module) {
		super("ancient", module, new AncientTree());
	}
	
	public static class AncientTree extends AbstractTreeGrower {
		
		public final TreeConfiguration config;
		
		// TODO currently just a blossom tree copy
		public AncientTree() {
			config = (new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(AncientWoodModule.woodSet.log),
					new FancyTrunkPlacer(8, 10, 10),
					BlockStateProvider.simple(AncientWoodModule.ancient_leaves),
					new FancyFoliagePlacer(ConstantInt.of(3), ConstantInt.of(1), 4),
					new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))))
					.ignoreVines()
					.build();
		}

		@Override
		protected Holder<ConfiguredFeature<TreeConfiguration, ?>> getConfiguredFeature(@Nonnull RandomSource rand, boolean hjskfsd) {
			return Holder.direct(new ConfiguredFeature<>(Feature.TREE, config));
		}

	}

}
