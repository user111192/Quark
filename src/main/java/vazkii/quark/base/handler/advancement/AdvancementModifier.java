package vazkii.quark.base.handler.advancement;

import java.util.Set;

import com.google.common.base.Supplier;

import net.minecraft.resources.ResourceLocation;
import vazkii.quark.base.module.QuarkModule;

public abstract class AdvancementModifier {

	public final QuarkModule module;
	private Supplier<Boolean> cond;
	
	public AdvancementModifier(QuarkModule module) {
		this.module = module;
	}
	
	public AdvancementModifier setCondition(Supplier<Boolean> cond) {
		this.cond = cond;
		return this;
	}
	
	public boolean isActive() {
		return module.enabled && (cond == null || cond.get());
	}
	
	public abstract Set<ResourceLocation> getTargets();
	public abstract boolean apply(ResourceLocation res, MutableAdvancement adv);
	
}
