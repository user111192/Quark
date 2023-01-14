package vazkii.quark.base.handler.advancement;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import vazkii.quark.base.Quark;

@EventBusSubscriber(bus = Bus.FORGE, modid = Quark.MOD_ID)
public final class AdvancementModificationHandler {

	private static Multimap<ResourceLocation, AdvancementModifier> modifiers = HashMultimap.create();
	
	public static void addModifier(AdvancementModifier mod) {
		Set<ResourceLocation> targets = mod.getTargets();
		for(ResourceLocation r : targets)
			modifiers.put(r, mod);
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
		for(ResourceLocation res : modifiers.keySet()) {
			Advancement adv = manager.getAdvancement(res);
			
			if(adv != null) {
				Collection<AdvancementModifier> found = modifiers.get(res);
				
				if(!found.isEmpty()) {
					boolean didAnything = false;
					MutableAdvancement mutable = new MutableAdvancement(adv);
					
					for(AdvancementModifier mod : found)
						if(mod.isActive() && mod.apply(res, mutable))
							didAnything = true;
					
					if(didAnything)
						mutable.commit();
				}
				
			}
		}
	}
	
}
