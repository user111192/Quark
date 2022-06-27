package vazkii.quark.content.client.hax;

import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import vazkii.quark.content.client.resources.AttributeSlot;

import java.util.List;
import java.util.Map;

// This is extremely jank. Please do not use this as an example for anything.
public interface PseudoAccessorItemStack {

	Map<AttributeSlot, Multimap<Attribute, AttributeModifier>> quark$getCapturedAttributes();

	void quark$capturePotionAttributes(List<Pair<Attribute, AttributeModifier>> attributes);
}
