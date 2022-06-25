package vazkii.quark.integration.jei;

import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.block.Block;
import vazkii.quark.addons.oddities.util.Influence;
import vazkii.quark.content.experimental.module.EnchantmentsBegoneModule;
import vazkii.quark.content.tools.module.ColorRunesModule;

import java.util.List;

public class InfluenceEntry implements IRecipeCategoryExtension {

	private final ItemStack candleStack;
	private final ItemStack boost;
	private final ItemStack dampen;

	public InfluenceEntry(Block candle, Influence influence) {
		this.candleStack = new ItemStack(candle);
		this.boost = getEnchantedBook(influence.boost(), DyeColor.GREEN, ChatFormatting.GREEN, "quark.jei.boost_influence");
		this.dampen = getEnchantedBook(influence.dampen(), DyeColor.RED, ChatFormatting.RED, "quark.jei.dampen_influence");
	}

	public ItemStack getBoostBook() {
		return this.boost;
	}

	public ItemStack getDampenBook() {
		return this.dampen;
	}

	public ItemStack getCandleStack() {
		return candleStack;
	}

	private static ItemStack getEnchantedBook(List<Enchantment> enchantments, DyeColor runeColor, ChatFormatting chatColor, String locKey) {
		ItemStack stack = ItemStack.EMPTY;

		for (Enchantment enchantment : enchantments) {
			if (!EnchantmentsBegoneModule.shouldBegone(enchantment)) {
				if (stack.isEmpty())
					stack = ColorRunesModule.withRune(new ItemStack(Items.ENCHANTED_BOOK), runeColor)
						 .setHoverName(new TranslatableComponent(locKey).withStyle(chatColor));
				EnchantedBookItem.addEnchantment(stack, new EnchantmentInstance(enchantment, enchantment.getMaxLevel()));
			}
		}

		return stack;
	}

	public boolean hasAny() {
		return !boost.isEmpty() || !dampen.isEmpty();
	}
}
