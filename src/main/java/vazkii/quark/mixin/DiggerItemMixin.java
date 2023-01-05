package vazkii.quark.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Tier;
import vazkii.quark.content.tweaks.module.GoldToolsHaveFortuneModule;

@Mixin(DiggerItem.class)
public class DiggerItemMixin {

	@Redirect(method = "isCorrectToolForDrops(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/block/state/BlockState;)Z",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/DiggerItem;getTier()Lnet/minecraft/world/item/Tier;"), 
			remap = false)
	private Tier getTier(DiggerItem item) {
		Tier realTier = item.getTier();
		
		return GoldToolsHaveFortuneModule.getEffectiveTier(item, realTier);
	}
	
}
