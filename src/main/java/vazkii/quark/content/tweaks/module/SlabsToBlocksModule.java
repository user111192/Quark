package vazkii.quark.content.tweaks.module;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.quark.api.event.RecipeCrawlEvent;
import vazkii.quark.base.Quark;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.module.QuarkModule;
import vazkii.quark.content.tweaks.recipe.SlabToBlockRecipe;

@LoadModule(category = ModuleCategory.TWEAKS, hasSubscriptions = true)
public class SlabsToBlocksModule extends QuarkModule {

	public static Map<Item, Item> recipes = new HashMap<>();

	@Override
	public void register() {
		ForgeRegistries.RECIPE_SERIALIZERS.register(Quark.MOD_ID + ":slab_to_block", SlabToBlockRecipe.SERIALIZER);
	}
	
	@SubscribeEvent
	public void onReset(RecipeCrawlEvent.Reset event) {
		recipes.clear();
	}
	
	@SubscribeEvent
	public void onVisitShaped(RecipeCrawlEvent.Visit.Shaped visit) {
		if(visit.ingredients.size() == 3
				&& visit.recipe.getHeight() == 1 
				&& visit.recipe.getWidth() == 3 
				&& visit.output.getItem() instanceof BlockItem bi 
				&& bi.getBlock() instanceof SlabBlock) {
			
			Item a = visit.ingredients.get(0).getItems()[0].getItem();
			Item b = visit.ingredients.get(1).getItems()[0].getItem();
			Item c = visit.ingredients.get(2).getItems()[0].getItem();
			
			if(a == b && b == c)
				recipes.put(bi, a);
		}
	}
	
}
