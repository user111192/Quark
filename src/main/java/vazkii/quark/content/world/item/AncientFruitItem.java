package vazkii.quark.content.world.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vazkii.quark.base.item.QuarkItem;
import vazkii.quark.base.module.QuarkModule;
import vazkii.quark.content.world.module.AncientWoodModule;

public class AncientFruitItem extends QuarkItem {

	public AncientFruitItem(QuarkModule module) {
		super("ancient_fruit", module, new Item.Properties().tab(CreativeModeTab.TAB_FOOD)
				.food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3F).alwaysEat().build()));
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
		ItemStack ret = super.finishUsingItem(stack, level, living);
		
		if(AncientWoodModule.ancientFruitExpValue > 0 && living instanceof Player player) {
			player.giveExperiencePoints(AncientWoodModule.ancientFruitExpValue);
			player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1F, 1F);
		}
		
		return ret;
	}

}
