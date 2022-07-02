package vazkii.quark.base.module.config.type;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import vazkii.quark.base.module.config.Config;
import vazkii.quark.base.module.config.ConfigFlagManager;

public class BiomeTagConfig extends AbstractConfigType implements IBiomeConfig {

	private final Object mutex = new Object();
	
	@Config(name = "Biome Tags")
	private List<String> biomeTagStrings;

	@Config
	private boolean isBlacklist;

	private List<TagKey<Biome>> tags;

	@SafeVarargs
	protected BiomeTagConfig(boolean isBlacklist, TagKey<Biome>... tagsIn) {
		this.isBlacklist = isBlacklist;

		biomeTagStrings = new LinkedList<>();
		for(TagKey<Biome> t : tagsIn)
			biomeTagStrings.add(t.location().toString());
	}

	protected BiomeTagConfig(boolean isBlacklist, String... types) {
		this.isBlacklist = isBlacklist;

		biomeTagStrings = new LinkedList<>();
		biomeTagStrings.addAll(Arrays.asList(types));
	}
	
	@Override
	public boolean canSpawn(ResourceLocation resource) {
		if(resource == null)
			return false;
		
		ResourceKey<Biome> key = ResourceKey.create(Registry.BIOME_REGISTRY, resource);
		Set<TagKey<Biome>> biomeTags = new HashSet<>(); // TODO 1.19: implement

		synchronized (mutex) {
			if(tags == null)
				updateTypes();
			
			for(TagKey<Biome> type : biomeTags) {
				for(TagKey<Biome> type2 : tags)
					if(type2.equals(type)) {
						return !isBlacklist;
					}
			}

			return isBlacklist;
		}
	}

	@Override
	public void onReload(ConfigFlagManager flagManager) {
		synchronized (mutex) {
			updateTypes();
		}
	}
	
	public void updateTypes() {
		tags = new LinkedList<>();
		for (String s : biomeTagStrings) {
			TagKey<Biome> tag = null; // TODO 1.19: implement
			
			if (tag != null)
				tags.add(tag);
		}
	}

}
