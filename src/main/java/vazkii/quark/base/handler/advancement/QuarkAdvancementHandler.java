package vazkii.quark.base.handler.advancement;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.google.common.base.Supplier;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import vazkii.quark.api.IAdvancementModifier;
import vazkii.quark.api.IAdvancementModifierDelegate;
import vazkii.quark.api.event.GatherAdvancementModifiersEvent;
import vazkii.quark.base.Quark;
import vazkii.quark.base.handler.GeneralConfig;
import vazkii.quark.base.handler.advancement.mod.AdventuringTimeModifier;
import vazkii.quark.base.handler.advancement.mod.BalancedDietModifier;
import vazkii.quark.base.handler.advancement.mod.FuriousCocktailModifier;
import vazkii.quark.base.handler.advancement.mod.MonsterHunterModifier;
import vazkii.quark.base.handler.advancement.mod.TwoByTwoModifier;
import vazkii.quark.base.handler.advancement.mod.WaxModifier;

@EventBusSubscriber(bus = Bus.FORGE, modid = Quark.MOD_ID)
public final class QuarkAdvancementHandler {

	private static Multimap<ResourceLocation, AdvancementModifier> modifiers = HashMultimap.create();
	private static boolean first = true;
	private static boolean gatheredAddons = false;
	
	public static void addModifier(AdvancementModifier mod) {
		Set<ResourceLocation> targets = mod.getTargets();
		for(ResourceLocation r : targets)
			modifiers.put(r, mod);
	}
	
	public static QuarkGenericTrigger registerGenericTrigger(String name) {
		if(first) {
			first = false;
			registerGenericTrigger("none"); // temporary fallback for wip advancements
		}
		
		ResourceLocation resloc = new ResourceLocation(Quark.MOD_ID, name);
		QuarkGenericTrigger trigger = new QuarkGenericTrigger(resloc);
		CriteriaTriggers.register(trigger);
		
		return trigger;
	}

	@SubscribeEvent
	public static void addListener(AddReloadListenerEvent event) {
		ReloadableServerResources resources = event.getServerResources();
		ServerAdvancementManager advancementManager = resources.getAdvancements();

		if(!gatheredAddons) {
			MinecraftForge.EVENT_BUS.post(new GatherAdvancementModifiersEvent(new Delegate()));
			gatheredAddons = true;
		}
		
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
	
	private static class Delegate implements IAdvancementModifierDelegate {

		@Override
		public IAdvancementModifier modifyAdventuringTime(Set<ResourceKey<Biome>> locations) {
			return new AdventuringTimeModifier(null, locations);
		}

		@Override
		public IAdvancementModifier modifyBalancedDiet(Set<Item> items) {
			return new BalancedDietModifier(null, items);
		}

		@Override
		public IAdvancementModifier modifyFuriousCocktail(Supplier<Boolean> isPotion, Set<MobEffect> effects) {
			return new FuriousCocktailModifier(null, isPotion, effects);
		}

		@Override
		public IAdvancementModifier modifyMonsterHunter(Set<EntityType<?>> types) {
			return new MonsterHunterModifier(null, types);
		}

		@Override
		public IAdvancementModifier modifyTwoByTwo(Set<EntityType<?>> types) {
			return new TwoByTwoModifier(null, types);
		}

		@Override
		public IAdvancementModifier modifyWaxOnWaxOff(Set<Block> unwaxed, Set<Block> waxed) {
			return new WaxModifier(null, unwaxed, waxed);
		}
		
		
	}
	
}
