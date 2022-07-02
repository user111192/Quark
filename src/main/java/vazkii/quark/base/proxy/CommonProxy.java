package vazkii.quark.base.proxy;

import java.time.LocalDateTime;
import java.time.Month;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import vazkii.arl.util.ClientTicker;
import vazkii.quark.base.Quark;
import vazkii.quark.base.capability.CapabilityHandler;
import vazkii.quark.base.handler.BrewingHandler;
import vazkii.quark.base.handler.ContributorRewardHandler;
import vazkii.quark.base.handler.FuelHandler;
import vazkii.quark.base.handler.QuarkSounds;
import vazkii.quark.base.handler.UndergroundBiomeHandler;
import vazkii.quark.base.handler.WoodSetHandler;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.base.module.config.IConfigCallback;
import vazkii.quark.base.network.QuarkNetwork;
import vazkii.quark.base.recipe.DataMaintainingCampfireRecipe;
import vazkii.quark.base.recipe.DataMaintainingRecipe;
import vazkii.quark.base.recipe.DataMaintainingSmeltingRecipe;
import vazkii.quark.base.recipe.DataMaintainingSmokingRecipe;
import vazkii.quark.base.recipe.ExclusionRecipe;
import vazkii.quark.base.world.EntitySpawnHandler;
import vazkii.quark.base.world.WorldGenHandler;

public class CommonProxy {

	private int lastConfigChange = -11;
	public static boolean jingleTheBells = false;
	private boolean configGuiSaving = false;

	public void start() {
		ForgeRegistries.RECIPE_SERIALIZERS.register(Quark.MOD_ID + ":exclusion", ExclusionRecipe.SERIALIZER);
		ForgeRegistries.RECIPE_SERIALIZERS.register(Quark.MOD_ID + ":maintaining", DataMaintainingRecipe.SERIALIZER);
		ForgeRegistries.RECIPE_SERIALIZERS.register(Quark.MOD_ID + ":maintaining_smelting", DataMaintainingSmeltingRecipe.SERIALIZER);
		ForgeRegistries.RECIPE_SERIALIZERS.register(Quark.MOD_ID + ":maintaining_campfire", DataMaintainingCampfireRecipe.SERIALIZER);
		ForgeRegistries.RECIPE_SERIALIZERS.register(Quark.MOD_ID + ":maintaining_smoking", DataMaintainingSmokingRecipe.SERIALIZER);

		QuarkSounds.start();
		ModuleLoader.INSTANCE.start();

		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		registerListeners(bus);

		LocalDateTime now = LocalDateTime.now();
		if (now.getMonth() == Month.DECEMBER && now.getDayOfMonth() >= 16 || now.getMonth() == Month.JANUARY && now.getDayOfMonth() <= 2)
			jingleTheBells = true;
	}

	public void registerListeners(IEventBus bus) {
		bus.addListener(this::setup);
		bus.addListener(this::loadComplete);
		bus.addListener(this::configChanged);
		bus.addListener(this::registerCapabilities);
		
		WorldGenHandler.registerBiomeModifier(bus);

		bus.register(RegistryListener.class);
	}

	public void setup(FMLCommonSetupEvent event) {
		QuarkNetwork.setup();
		BrewingHandler.setup();
		ModuleLoader.INSTANCE.setup(event);
		initContributorRewards();

		WoodSetHandler.setup(event);
	}

	public void loadComplete(FMLLoadCompleteEvent event) {
		ModuleLoader.INSTANCE.loadComplete(event);

		FuelHandler.addAllWoods();
		UndergroundBiomeHandler.init(event);
	}

	public void configChanged(ModConfigEvent event) {
		if(event.getConfig().getModId().equals(Quark.MOD_ID)
				&& ClientTicker.ticksInGame - lastConfigChange > 10
				&& !configGuiSaving) {
			lastConfigChange = ClientTicker.ticksInGame;
			handleQuarkConfigChange();
		}
	}

	public void setConfigGuiSaving(boolean saving) {
		configGuiSaving = saving;
		lastConfigChange = ClientTicker.ticksInGame;
	}

	public void registerCapabilities(RegisterCapabilitiesEvent event) {
		CapabilityHandler.registerCapabilities(event);
	}

	public void handleQuarkConfigChange() {
		ModuleLoader.INSTANCE.configChanged();
		EntitySpawnHandler.refresh();
	}

	public InteractionResult clientUseItem(Player player, Level level, InteractionHand hand, BlockHitResult hit) {
		return InteractionResult.PASS;
	}

	protected void initContributorRewards() {
		ContributorRewardHandler.init();
	}

	public IConfigCallback getConfigCallback() {
		return new IConfigCallback.Dummy();
	}

	public boolean isClientPlayerHoldingShift() {
		return false;
	}

	public static final class RegistryListener {

		private static boolean registerDone;

		@SubscribeEvent(priority = EventPriority.HIGHEST)
		public static void registerContent(RegisterEvent event) {
			if(registerDone)
				return;
			registerDone = true;

			ModuleLoader.INSTANCE.register();
			WoodSetHandler.register();
			WorldGenHandler.register();
		}

	}

}
