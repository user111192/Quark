package vazkii.quark.content.world.module;

import com.google.common.base.Functions;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MaterialColor;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.block.QuarkLeavesBlock;
import vazkii.quark.base.handler.VariantHandler;
import vazkii.quark.base.handler.WoodSetHandler;
import vazkii.quark.base.handler.WoodSetHandler.WoodSet;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.module.QuarkModule;
import vazkii.quark.base.module.config.Config;
import vazkii.quark.base.module.config.Config.Min;
import vazkii.quark.content.world.block.AncientSaplingBlock;
import vazkii.quark.content.world.item.AncientFruitItem;

@LoadModule(category = ModuleCategory.WORLD)
public class AncientWoodModule extends QuarkModule {

	@Config(description = "Set to 0 to disable Ancient Fruit giving exp")
	@Min(0)
	public static int ancientFruitExpValue = 10;
	
	public static WoodSet woodSet;
	public static Block ancient_leaves;
	public static Item ancient_fruit;
	
	@Override
	public void register() {
		woodSet = WoodSetHandler.addWoodSet(this, "ancient", MaterialColor.TERRACOTTA_WHITE, MaterialColor.TERRACOTTA_WHITE);
		ancient_leaves = new QuarkLeavesBlock(woodSet.name, this, MaterialColor.PLANT);
		ancient_fruit = new AncientFruitItem(this);
		
		AncientSaplingBlock sapling = new AncientSaplingBlock(this);
		VariantHandler.addFlowerPot(sapling, RegistryHelper.getInternalName(sapling).getPath(), Functions.identity());
	}
	
}
