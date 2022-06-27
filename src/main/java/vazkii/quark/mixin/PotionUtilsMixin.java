package vazkii.quark.mixin;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import vazkii.quark.content.client.tooltip.AttributeTooltips;

import java.util.Collections;
import java.util.Map;

@Mixin(PotionUtils.class)
public class PotionUtilsMixin {

	@ModifyVariable(method = "addPotionTooltip", at = @At("STORE"))
	private static Map<Attribute, AttributeModifier> overrideAttributeTooltips(Map<Attribute, AttributeModifier> attributes) {
		if (AttributeTooltips.shouldHideAttributes()) return Collections.emptyMap();
		return attributes;
	}
}
