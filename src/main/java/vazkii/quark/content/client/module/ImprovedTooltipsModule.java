package vazkii.quark.content.client.module;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.module.QuarkModule;
import vazkii.quark.base.module.config.Config;
import vazkii.quark.content.client.resources.AttributeTooltipManager;
import vazkii.quark.content.client.tooltip.*;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author WireSegal
 * Created at 6:19 PM on 8/31/19.
 */
@LoadModule(category = ModuleCategory.CLIENT, hasSubscriptions = true, subscribeOn = Dist.CLIENT)
public class ImprovedTooltipsModule extends QuarkModule {

	@Config
	public static boolean attributeTooltips = true;
	@Config
	public static boolean foodTooltips = true;
	@Config
	public static boolean shulkerTooltips = true;
	@Config
	public static boolean mapTooltips = true;
	@Config
	public static boolean enchantingTooltips = true;

	@Config
	public static boolean shulkerBoxUseColors = true;
	@Config
	public static boolean shulkerBoxRequireShift = false;
	@Config
	public static boolean mapRequireShift = false;

	@Config
	public static boolean showSaturation = true;
	@Config
	public static int foodCompressionThreshold = 4;

	@Config(description = "The value of each shank of food. " +
			"Tweak this when using mods like Hardcore Hunger which change that value.")
	public static int foodDivisor = 2;

	@Config
	public static List<String> enchantingStacks = Lists.newArrayList("minecraft:diamond_sword", "minecraft:diamond_pickaxe", "minecraft:diamond_shovel", "minecraft:diamond_axe", "minecraft:diamond_hoe",
			"minecraft:diamond_helmet", "minecraft:diamond_chestplate", "minecraft:diamond_leggings", "minecraft:diamond_boots",
			"minecraft:shears", "minecraft:bow", "minecraft:fishing_rod", "minecraft:crossbow", "minecraft:trident", "minecraft:elytra", "quark:pickarang");

	@Config(description = "A list of additional stacks to display on each enchantment\n"
			+ "The format is as follows:\n"
			+ "enchant_id=item1,item2,item3...\n"
			+ "So to display a carrot on a stick on a mending book, for example, you use:\n"
			+ "minecraft:mending=minecraft:carrot_on_a_stick")
	public static List<String> enchantingAdditionalStacks = Lists.newArrayList();

	private static final String IGNORE_TAG = "quark:no_tooltip";

	public static boolean staticEnabled;

	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientSetup() {
		register(AttributeTooltips.AttributeComponent.class);
		register(FoodTooltips.FoodComponent.class);
		register(ShulkerBoxTooltips.ShulkerComponent.class);
		register(MapTooltips.MapComponent.class);
		register(EnchantedBookTooltips.EnchantedBookComponent.class);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void registerReloadListeners(Consumer<PreparableReloadListener> registry) {
		registry.accept(new AttributeTooltipManager());
	}

	@Override
	public void configChanged() {
		staticEnabled = enabled;
		EnchantedBookTooltips.reloaded();
	}

	private static boolean ignore(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, IGNORE_TAG, false);
	}

	private static <T extends ClientTooltipComponent & TooltipComponent> void register(Class<T> clazz) {
		MinecraftForgeClient.registerTooltipComponentFactory(clazz, Function.identity());
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void makeTooltip(RenderTooltipEvent.GatherComponents event) {
		if(ignore(event.getItemStack()))
			return;

		if (attributeTooltips)
			AttributeTooltips.makeTooltip(event);
		if (foodTooltips || showSaturation)
			FoodTooltips.makeTooltip(event, foodTooltips, showSaturation);
		if (shulkerTooltips)
			ShulkerBoxTooltips.makeTooltip(event);
		if (mapTooltips)
			MapTooltips.makeTooltip(event);
		if (enchantingTooltips)
			EnchantedBookTooltips.makeTooltip(event);
	}
}
