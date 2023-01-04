package vazkii.quark.base.handler;

import java.util.Collection;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import vazkii.quark.api.event.RecipeCrawlEvent;
import vazkii.quark.api.event.RecipeCrawlEvent.Visit;
import vazkii.quark.base.Quark;

@EventBusSubscriber(bus = Bus.FORGE, modid = Quark.MOD_ID)
public class RecipeCrawlHandler {

	private static boolean lock = false;

	// TODO add reset on starting reload
	
	@SubscribeEvent
	public static void tick(LevelTickEvent event) {
		if(!lock && event.phase == Phase.END) {
			lock = true;
			load(event.level);
		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void tick(ClientTickEvent event) {
		if(Minecraft.getInstance().level == null)
			clear();
	}

	private static void clear() {
		if(lock) {
			lock = false;
			
			MinecraftForge.EVENT_BUS.post(new RecipeCrawlEvent.Reset());
		}
	}

	private static void load(Level level) {
		RecipeManager manager = level.getRecipeManager();
		if(!manager.getRecipes().isEmpty()) {
			MinecraftForge.EVENT_BUS.post(new RecipeCrawlEvent.CrawlStarting());
			Collection<Recipe<?>> recipes = manager.getRecipes();

			for(Recipe<?> recipe : recipes) {
				if(recipe == null || recipe.getResultItem() == null || recipe.getIngredients() == null)
					continue;
				
				RecipeCrawlEvent.Visit<?> event;
				
				if(recipe instanceof ShapedRecipe sr)
					event = new Visit.Shaped(sr);
				else if(recipe instanceof ShapelessRecipe sr)
					event = new Visit.Shapeless(sr);
				else if(recipe instanceof CustomRecipe cr)
					event = new Visit.Custom(cr);
				else if(recipe instanceof AbstractCookingRecipe acr)
					event = new Visit.Cooking(acr);
				else 
					event = new Visit.Misc(recipe);
				
				MinecraftForge.EVENT_BUS.post(event);
			}
			
			MinecraftForge.EVENT_BUS.post(new RecipeCrawlEvent.CrawlEnded());
		}
	}

}
