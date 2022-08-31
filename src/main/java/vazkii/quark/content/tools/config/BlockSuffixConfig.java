package vazkii.quark.content.tools.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import vazkii.quark.base.module.config.Config;
import vazkii.quark.base.module.config.ConfigFlagManager;
import vazkii.quark.base.module.config.type.AbstractConfigType;

public class BlockSuffixConfig extends AbstractConfigType {

	private static final VariantMap EMPTY_VARIANT_MAP = new VariantMap(new HashMap<>());
	
	@Config
	public List<String> knownSuffixes;
	
	@Config
	public List<String> testedMods;
	
	private Map<Block, VariantMap> blockVariants = new HashMap<>();
	
	public BlockSuffixConfig(List<String> knownSuffixes, List<String> testedMods) {
		this.knownSuffixes = knownSuffixes;
		this.testedMods = testedMods;
	}
	
	@Override
	public void onReload(ConfigFlagManager flagManager) {
		blockVariants.clear();
	}
	
	public String getVariantForBlock(Block block) {
		String name = Registry.BLOCK.getKey(block).getPath();
		
		for(String s : knownSuffixes) {
			String check = String.format("_%s", s);
			if(name.endsWith(check))
				return s;
		}
		
		return null;
	}
	
	public Block getBlockForTarget(Block block, Block target) {
		return getBlockForVariant(block, getVariantForBlock(target));
	}
	
	public Block getBlockForVariant(Block block, String variant) {
		blockVariants.clear(); // TODO test
		if(variant == null || !knownSuffixes.contains(variant))
			return block;
		
		VariantMap map = getVariants(block);
		Block ret = map.variants.get(variant);
		if(ret != null)
			return ret;
		
		return block;
	}
	
	private VariantMap getVariants(Block block) {
		if(blockVariants.containsKey(block))
			return blockVariants.get(block);
		
		Map<String, Block> newVariants = new HashMap<>();
		
		for(String s : knownSuffixes) {
			Block suffixed = getSuffixedBlock(block, s);
			if(suffixed != null)
				newVariants.put(s, suffixed);
		}
		
		if(newVariants.isEmpty())
			blockVariants.put(block, EMPTY_VARIANT_MAP);
		else blockVariants.put(block, new VariantMap(newVariants));
		
		return getVariants(block);
	}
	
	private Block getSuffixedBlock(Block ogBlock, String suffix) {
		ResourceLocation resloc = Registry.BLOCK.getKey(ogBlock);
		String namespace = resloc.getNamespace();
		String name = resloc.getPath();
		
		Block ret = getSuffixedBlock(namespace, name, suffix);
		if(ret != null)
			return ret;
		
		for(String mod : testedMods) {
			ret = getSuffixedBlock(mod, name, suffix);
			if(ret != null)
				return ret;
		}
		
		return null;
	}
	
	private Block getSuffixedBlock(String namespace, String name, String suffix) {
		if(name.endsWith("planks")) {
			String singular = name.substring(0, name.length() - 7);
			Block singularAttempt = getSuffixedBlock(namespace, singular, suffix);
			if(singularAttempt != null)
				return singularAttempt;
		}
		
		if(name.endsWith("s")) {
			String singular = name.substring(0, name.length() - 1);
			Block singularAttempt = getSuffixedBlock(namespace, singular, suffix);
			if(singularAttempt != null)
				return singularAttempt;
		}
		
		String targetStr = String.format("%s:%s_%s", namespace, name, suffix);
		ResourceLocation target = new ResourceLocation(targetStr);
		Block ret = Registry.BLOCK.get(target);
		
		if(ret == Blocks.AIR)
			return null;
		
		return ret;
	}
	
	private record VariantMap(Map<String, Block> variants) { }
	
}
