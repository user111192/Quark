package vazkii.quark.content.building.module;

import net.minecraft.world.level.material.MaterialColor;
import vazkii.quark.base.handler.WoodSetHandler;
import vazkii.quark.base.handler.WoodSetHandler.WoodSet;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.module.QuarkModule;

@LoadModule(category = ModuleCategory.BUILDING)
public class BambooBackportModule extends QuarkModule {

	public static WoodSet woodSet;
	
	@Override
	public void register() {
		woodSet = WoodSetHandler.addWoodSet(this, "bamboo", MaterialColor.COLOR_YELLOW, MaterialColor.COLOR_YELLOW, false);
	}
	
}
