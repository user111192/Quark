package vazkii.quark.base.module.config.type;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public interface IBiomeConfig extends IConfigType {

	default boolean canSpawn(Holder<Biome> b) {
		return b.unwrap().map(
				rk -> canSpawn(rk.location()),
				bm -> false);
	}

	boolean canSpawn(ResourceLocation b);

}
