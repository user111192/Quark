package vazkii.quark.content.tweaks.module;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.module.QuarkModule;
import vazkii.quark.base.module.config.Config;

@LoadModule(category = ModuleCategory.TWEAKS, hasSubscriptions = true)
public class SafeRabbitsModule extends QuarkModule {

	@Config(description = "How many blocks should be subtracted from the rabbit fall height when calculating fall damage. 5 is the same value as vanilla frogs") 
	public double heightReduction = 5.0;
	
	@SubscribeEvent
	public void onFall(LivingFallEvent event) {
		if(event.getEntity().getType() == EntityType.RABBIT)
			event.setDistance(Math.max(0, event.getDistance() - (float) heightReduction));
	}
	
}
