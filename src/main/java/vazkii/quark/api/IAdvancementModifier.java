package vazkii.quark.api;

import java.util.Set;

import com.google.common.base.Supplier;

import net.minecraft.resources.ResourceLocation;

public interface IAdvancementModifier {

	IAdvancementModifier setCondition(Supplier<Boolean> cond);
	Set<ResourceLocation> getTargets();
	
}
