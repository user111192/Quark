package vazkii.quark.base.proxy;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.config.ModConfig.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import vazkii.arl.util.ClientTicker;
import vazkii.quark.base.Quark;
import vazkii.quark.base.moduleloader.ModuleLoader;

public class CommonProxy {

	private int lastConfigChange = 0;
	
	public void start() {
		ModuleLoader.INSTANCE.start();
		
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		registerListeners(bus);
	}
	
	public void registerListeners(IEventBus bus) {
		bus.addListener(this::setup);
		bus.addListener(this::loadComplete);
		bus.addListener(this::configChanged);
	}
	
	public final void setup(FMLCommonSetupEvent event) {
		ModuleLoader.INSTANCE.setup();
	}
	
	public final void loadComplete(FMLLoadCompleteEvent event) {
		ModuleLoader.INSTANCE.loadComplete();
	}
	
	public final void configChanged(ModConfigEvent event) {
		if(event.getConfig().getModId().equals(Quark.MOD_ID) && ClientTicker.ticksInGame - lastConfigChange > 10) { 
			handleQuarkConfigChange();
			lastConfigChange = ClientTicker.ticksInGame;
		}
	}
	
	public void handleQuarkConfigChange() {
		ModuleLoader.INSTANCE.configChanged();
	}
	
}