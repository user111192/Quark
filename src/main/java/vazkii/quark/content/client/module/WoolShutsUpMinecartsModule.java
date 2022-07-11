package vazkii.quark.content.client.module;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.module.QuarkModule;

@LoadModule(category = ModuleCategory.CLIENT)
public class WoolShutsUpMinecartsModule extends QuarkModule {

	private static boolean staticEnabled;
	
	@Override
	public void configChanged() {
		staticEnabled = enabled;
	}
	
	@OnlyIn(Dist.CLIENT)
	public static boolean canPlay(AbstractMinecart cart) {
		return !staticEnabled || !cart.level.getBlockState(cart.blockPosition().below()).is(BlockTags.DAMPENS_VIBRATIONS);
	}
	
}
