package vazkii.quark.content.tools.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import vazkii.quark.base.item.QuarkItem;
import vazkii.quark.base.module.QuarkModule;
import vazkii.quark.content.tools.module.SawModule;

public class SawItem extends QuarkItem {
	
	public SawItem(QuarkModule module) {
		super("saw", module, new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_TOOLS));
	}
	
}
