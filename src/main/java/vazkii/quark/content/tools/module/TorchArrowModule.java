package vazkii.quark.content.tools.module;

import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.Item;
import vazkii.quark.base.item.QuarkArrowItem;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.module.QuarkModule;

@LoadModule(category = ModuleCategory.TOOLS)
public class TorchArrowModule extends QuarkModule {

	public static Item torch_arrow;
	
	@Override
	public void register() {
		torch_arrow = new QuarkArrowItem.Impl("torch_arrow", this, (level, stack, living) -> {
			Arrow arrow = new Arrow(level, living);
			arrow.setSecondsOnFire(100); // TODO
			return arrow;
		});
	}
	
}
