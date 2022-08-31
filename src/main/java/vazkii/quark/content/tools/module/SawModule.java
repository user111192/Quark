package vazkii.quark.content.tools.module;

import java.util.Arrays;

import net.minecraft.world.item.Item;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.module.QuarkModule;
import vazkii.quark.base.module.config.Config;
import vazkii.quark.content.tools.config.BlockSuffixConfig;
import vazkii.quark.content.tools.item.SawItem;

@LoadModule(category = ModuleCategory.TOOLS, hasSubscriptions = true)
public class SawModule extends QuarkModule {

	@Config 
	public static BlockSuffixConfig variants = new BlockSuffixConfig(
			Arrays.asList("slab", "stairs", "wall", "fence", "vertical_slab"), 
			Arrays.asList("quark"));
	
	public static Item saw;
	
	@Override
	public void register() {
		saw = new SawItem(this);
	}
	

	
}
