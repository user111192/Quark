package vazkii.quark.content.building.module;

import net.minecraft.world.level.block.Blocks;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.module.QuarkModule;
import vazkii.quark.content.building.block.HollowLogBlock;

@LoadModule(category = ModuleCategory.BUILDING)
public class HollowLogsModule extends QuarkModule {

	@Override
	public void register() {
		new HollowLogBlock(Blocks.OAK_LOG, this);
	}
	
	
	
}
