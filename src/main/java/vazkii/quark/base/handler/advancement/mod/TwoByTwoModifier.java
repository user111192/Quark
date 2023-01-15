package vazkii.quark.base.handler.advancement.mod;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.BredAnimalsTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.handler.advancement.AdvancementModifier;
import vazkii.quark.base.handler.advancement.MutableAdvancement;
import vazkii.quark.base.module.QuarkModule;

public class TwoByTwoModifier extends AdvancementModifier {

	private static final ResourceLocation TARGET = new ResourceLocation("husbandry/bred_all_animals");
	
	final Set<EntityType<?>> entityTypes;
	
	public TwoByTwoModifier(QuarkModule module, Set<EntityType<?>> entityTypes) {
		super(module);
		
		this.entityTypes = entityTypes;
	}

	@Override
	public Set<ResourceLocation> getTargets() {
		return ImmutableSet.of(TARGET);
	}

	@Override
	public boolean apply(ResourceLocation res, MutableAdvancement adv) {
		for(EntityType<?> type : entityTypes) {
			Criterion criterion = new Criterion(BredAnimalsTrigger.TriggerInstance.bredAnimals(EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(type))));
			
			String name = RegistryHelper.getInternalName(type).toString();
			adv.addRequiredCriterion(name, criterion);
		}
		
		return true;
	}

}
