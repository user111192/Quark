package vazkii.quark.base.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.quark.base.Quark;
import vazkii.quark.base.module.QuarkModule;
import vazkii.quark.base.recipe.DyeRecipe;

public final class DyeHandler {
	
	private static final Map<Item, Supplier<Boolean>> dyeableConditions = new HashMap<>();
	private static final DyeSurrogate SURROGATE = new DyeSurrogate();
	
	private static boolean registered = false;
	
	public static synchronized void register() {
		if(!registered) {
			registered = true;
			
			ForgeRegistries.RECIPE_SERIALIZERS.register(Quark.MOD_ID + ":dye_item", DyeRecipe.SERIALIZER);
		}
	}
	 
	public static void addAlwaysDyeable(Item item) {
		addDyeable(item, () -> true);
	}
	
	public static void addDyeable(Item item, QuarkModule module) {
		addDyeable(item, () -> module.enabled);
	}
	
	public static void addDyeable(Item item, Supplier<Boolean> cond) {
		register();
		dyeableConditions.put(item, cond);
	}

	public static boolean isDyeable(ItemStack stack) {
		Item item = stack.getItem();
		return dyeableConditions.containsKey(item) && dyeableConditions.get(item).get();
	}
	
	// Copy of DyeableLeatherItem but for our system
	public static ItemStack dyeItem(ItemStack stack, List<DyeItem> dyes) {
	      ItemStack itemstack = ItemStack.EMPTY;
	      int[] aint = new int[3];
	      int i = 0;
	      int j = 0;

	      if(isDyeable(stack)) {
	         itemstack = stack.copy();
	         itemstack.setCount(1);
	         if (SURROGATE.hasCustomColor(stack)) {
	            int k = SURROGATE.getColor(itemstack);
	            float f = (float)(k >> 16 & 255) / 255.0F;
	            float f1 = (float)(k >> 8 & 255) / 255.0F;
	            float f2 = (float)(k & 255) / 255.0F;
	            i += (int)(Math.max(f, Math.max(f1, f2)) * 255.0F);
	            aint[0] += (int)(f * 255.0F);
	            aint[1] += (int)(f1 * 255.0F);
	            aint[2] += (int)(f2 * 255.0F);
	            ++j;
	         }

	         for(DyeItem dyeitem : dyes) {
	            float[] afloat = dyeitem.getDyeColor().getTextureDiffuseColors();
	            int i2 = (int)(afloat[0] * 255.0F);
	            int l = (int)(afloat[1] * 255.0F);
	            int i1 = (int)(afloat[2] * 255.0F);
	            i += Math.max(i2, Math.max(l, i1));
	            aint[0] += i2;
	            aint[1] += l;
	            aint[2] += i1;
	            ++j;
	         }
	         
	         int j1 = aint[0] / j;
	         int k1 = aint[1] / j;
	         int l1 = aint[2] / j;
	         float f3 = (float)i / (float)j;
	         float f4 = (float)Math.max(j1, Math.max(k1, l1));
	         j1 = (int)((float)j1 * f3 / f4);
	         k1 = (int)((float)k1 * f3 / f4);
	         l1 = (int)((float)l1 * f3 / f4);
	         int j2 = (j1 << 8) + k1;
	         j2 = (j2 << 8) + l1;
	         SURROGATE.setColor(itemstack, j2);
	         
	         return itemstack;
	      }
	      
	      return ItemStack.EMPTY;
	   }
	
	private static class DyeSurrogate implements DyeableLeatherItem {}
	
}
