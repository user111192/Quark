package vazkii.quark.base.handler.advancement;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import vazkii.quark.base.Quark;
import vazkii.quark.base.handler.GeneralConfig;

@EventBusSubscriber(bus = Bus.FORGE, modid = Quark.MOD_ID)
public final class QuarkAdvancementHandler {

	private static Multimap<ResourceLocation, AdvancementModifier> modifiers = HashMultimap.create();
	
	public static void addModifier(AdvancementModifier mod) {
		Set<ResourceLocation> targets = mod.getTargets();
		for(ResourceLocation r : targets)
			modifiers.put(r, mod);
	}
	
	public static QuarkGenericTrigger registerGenericTrigger(String name) {
		ResourceLocation resloc = new ResourceLocation(Quark.MOD_ID, name);
		QuarkGenericTrigger trigger = new QuarkGenericTrigger(resloc);
		CriteriaTriggers.register(trigger);
		
		return trigger;
	}

	@SubscribeEvent
	public static void addListener(AddReloadListenerEvent event) {
		ReloadableServerResources resources = event.getServerResources();
		ServerAdvancementManager advancementManager = resources.getAdvancements();

		event.addListener((barrier, manager, prepFiller, applyFiller, prepExec, applyExec) -> {
			return 
				CompletableFuture.completedFuture(null)
				.thenCompose(barrier::wait)
				.thenAccept(v -> {
					onAdvancementsLoaded(advancementManager);
				});
		});
	}
	
	private static void onAdvancementsLoaded(ServerAdvancementManager manager) {
		if(!GeneralConfig.enableAdvancementModification)
			return;
		
		for(ResourceLocation res : modifiers.keySet()) {
			Advancement adv = manager.getAdvancement(res);
			
			if(adv != null) {
				Collection<AdvancementModifier> found = modifiers.get(res);
				
				if(!found.isEmpty()) {
					int modifications = 0;
					MutableAdvancement mutable = new MutableAdvancement(adv);
					
					for(AdvancementModifier mod : found)
						if(mod.isActive() && mod.apply(res, mutable))
							modifications++;
							
					if(modifications > 0) {
						Quark.LOG.info("Modified advancement {} with {} patches", adv.getId(), modifications);
						mutable.commit();
					}
				}
			}
		}
	}
	
}
