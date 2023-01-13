package vazkii.quark.integration.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.compress.utils.Lists;
import vazkii.quark.content.tweaks.module.SlabsToBlocksModule;
import vazkii.quark.content.tweaks.recipe.SlabToBlockRecipe;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public record SlabToBlockExtension(SlabToBlockRecipe recipe) implements ICraftingCategoryExtension {
	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull ICraftingGridHelper craftingGridHelper, @Nonnull IFocusGroup focuses) {
		List<ItemStack> input1 = Lists.newArrayList();
		List<ItemStack> input2 = Lists.newArrayList();
		List<ItemStack> outputs = Lists.newArrayList();
		for (var recipe : SlabsToBlocksModule.recipes.entrySet()) {
			input1.add(new ItemStack(recipe.getKey()));
			input2.add(new ItemStack(recipe.getKey()));
			outputs.add(new ItemStack(recipe.getValue()));
		}

		var allSlots = craftingGridHelper.createAndSetInputs(builder, Arrays.asList(input1, input2), 0, 0);
		allSlots.add(craftingGridHelper.createAndSetOutputs(builder, outputs));

		builder.createFocusLink(allSlots.toArray(new IRecipeSlotBuilder[0]));
	}

	@Override
	public ResourceLocation getRegistryName() {
		return recipe.getId();
	}
}
