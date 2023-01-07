package vazkii.quark.content.tweaks.module;

import net.minecraft.world.item.Items;
import vazkii.quark.base.handler.DyeHandler;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.module.QuarkModule;

@LoadModule(category = ModuleCategory.TWEAKS)
public class DyeableItemFramesModule extends QuarkModule {

	@Override
	public void register() {
		DyeHandler.addDyeable(Items.ITEM_FRAME, this);
		DyeHandler.addDyeable(Items.GLOW_ITEM_FRAME, this);
	}
	
}
