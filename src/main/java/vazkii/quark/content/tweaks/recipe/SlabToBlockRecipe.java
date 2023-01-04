package vazkii.quark.content.tweaks.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.Level;
import vazkii.quark.content.tweaks.module.SlabsToBlocksModule;

public class SlabToBlockRecipe extends CustomRecipe {
	
	public static final SimpleRecipeSerializer<?> SERIALIZER = new SimpleRecipeSerializer<>(SlabToBlockRecipe::new);
	
	public SlabToBlockRecipe(ResourceLocation id) {
		super(id);
	}

	// TODO check for other potential matching recipes before accepting
	@Override
	public boolean matches(CraftingContainer container, Level level) {
		Item target = null;
		
		for(int i = 0; i < container.getContainerSize(); i++) {
			ItemStack stack = container.getItem(i);
			if(!stack.isEmpty()) {
				Item item = stack.getItem();
				
				if(target != null)
					return item == target;
				
				if(SlabsToBlocksModule.recipes.containsKey(item)) {
					target = item;
				} else return false;
			}
		}
		
		return false;
	}

	@Override
	public ItemStack assemble(CraftingContainer container) {
		for(int i = 0; i < container.getContainerSize(); i++) {
			ItemStack stack = container.getItem(i);
			if(!stack.isEmpty()) {
				Item item = stack.getItem();
				
				if(SlabsToBlocksModule.recipes.containsKey(item))
					return new ItemStack(SlabsToBlocksModule.recipes.get(item));
			}
		}
		
		return ItemStack.EMPTY;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}
	
	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return (width * height) >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}



}
