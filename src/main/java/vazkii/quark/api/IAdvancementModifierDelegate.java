package vazkii.quark.api;

import java.util.Set;

import com.google.common.base.Supplier;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public interface IAdvancementModifierDelegate {

	IAdvancementModifier modifyAdventuringTime(Set<ResourceKey<Biome>> locations);
	IAdvancementModifier modifyBalancedDiet(Set<Item> items);
	IAdvancementModifier modifyFuriousCocktail(Supplier<Boolean> isPotion, Set<MobEffect> effects);
	IAdvancementModifier modifyMonstersHunted(Set<EntityType<?>> types);
	IAdvancementModifier modifyTwoByTwo(Set<EntityType<?>> types);
	IAdvancementModifier modifyWaxOnWaxOff(Set<Block> unwaxed, Set<Block> waxed);
	
}
