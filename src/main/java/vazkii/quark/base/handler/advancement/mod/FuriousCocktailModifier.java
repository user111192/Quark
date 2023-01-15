package vazkii.quark.base.handler.advancement.mod;

import java.util.Set;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.EffectsChangedTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import vazkii.quark.base.handler.advancement.AdvancementModifier;
import vazkii.quark.base.handler.advancement.MutableAdvancement;
import vazkii.quark.base.module.QuarkModule;

public class FuriousCocktailModifier extends AdvancementModifier {
	
	private static final ResourceLocation TARGET_AP = new ResourceLocation("nether/all_potions");
	private static final ResourceLocation TARGET_AE = new ResourceLocation("nether/all_effects");

	final Supplier<Boolean> isPotion;
	final Set<MobEffect> effects;
	
	public FuriousCocktailModifier(QuarkModule module, Supplier<Boolean> isPotion, Set<MobEffect> effects) {
		super(module);
		
		this.isPotion = isPotion;
		this.effects = effects;
	}

	@Override
	public Set<ResourceLocation> getTargets() {
		return ImmutableSet.of(TARGET_AP, TARGET_AE);
	}

	@Override
	public boolean apply(ResourceLocation res, MutableAdvancement adv) {
		if(!isPotion.get() && res.equals(TARGET_AP))
			return false;
		
		Criterion crit = adv.criteria.get("all_effects");
		if(crit != null && crit.getTrigger() instanceof EffectsChangedTrigger.TriggerInstance ect)  {
			for(MobEffect e : effects)
				ect.effects.and(e);
			
			return true;
		}
		
		return false;
	}

}
