package vazkii.quark.content.tweaks.module;

import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.module.QuarkModule;
import vazkii.quark.base.module.config.Config;
import vazkii.quark.base.module.config.Config.Max;
import vazkii.quark.base.module.config.Config.Min;

@LoadModule(category = ModuleCategory.TWEAKS)
public class GoldToolsHaveFortuneModule extends QuarkModule {

	private static final Tier[] TIERS = new Tier[] {
			Tiers.WOOD, Tiers.STONE, Tiers.IRON, Tiers.DIAMOND, Tiers.NETHERITE
	};

	@Config
	public static int fortuneLevel = 2;

	@Config
	@Min(0)
	@Max(4)
	public static int harvestLevel = 2;

	private static boolean staticEnabled;

	@Override
	public void configChanged() {
		staticEnabled = enabled;
	}

	public static int getFortuneLevel(Enchantment enchant, ItemStack stack, int prev) {
		if(!staticEnabled || enchant != Enchantments.BLOCK_FORTUNE || prev > 0)
			return prev;

		if(stack.getItem() instanceof DiggerItem di) {
			Tier tier = di.getTier();
			
			if(tier == Tiers.GOLD)
				return 2;
		}

		return prev;
	}

	public static Tier getEffectiveTier(Item item, Tier realTier) {
		if(!staticEnabled || (realTier != Tiers.GOLD))
			return realTier;

		return TIERS[harvestLevel];
	}

}
