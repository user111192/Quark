package vazkii.quark.base.handler.advancement.mod;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.handler.advancement.AdvancementModifier;
import vazkii.quark.base.handler.advancement.MutableAdvancement;
import vazkii.quark.base.module.QuarkModule;

public class BalancedDietModifier extends AdvancementModifier {

	private static final ResourceLocation TARGET = new ResourceLocation("husbandry/balanced_diet");
	
	private final Set<Item> items;
	
	public BalancedDietModifier(QuarkModule module, Set<Item> items) {
		super(module);
		this.items = items;
	}

	@Override
	public Set<ResourceLocation> getTargets() {
		return ImmutableSet.of(TARGET);
	}

	@Override
	public boolean apply(ResourceLocation res, MutableAdvancement adv) {
		for(Item item : items) {
			String name = RegistryHelper.getInternalName(item).toString();
			
			Criterion criterion = new Criterion(ConsumeItemTrigger.TriggerInstance.usedItem(item));
			adv.addRequiredCriterion(name, criterion);
		}
		
		return true;
	}

}
