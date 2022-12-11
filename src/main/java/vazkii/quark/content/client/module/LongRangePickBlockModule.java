package vazkii.quark.content.client.module;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.quark.base.handler.RayTraceHandler;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.module.QuarkModule;

@LoadModule(category = ModuleCategory.CLIENT)
public class LongRangePickBlockModule extends QuarkModule {

	public static boolean staticEnabled;
	
	private static HitResult savedHitResult;
	
	@Override
	public void configChanged() {
		staticEnabled = enabled;
	}
		
	@OnlyIn(Dist.CLIENT)
	public static HitResult transformHitResult(HitResult hitResult) {
		savedHitResult = hitResult;
		
		if(staticEnabled) {
			Minecraft mc = Minecraft.getInstance();
			Player player = mc.player;
			Level level = mc.level;
			
			HitResult result = RayTraceHandler.rayTrace(player, level, player, Block.OUTLINE, Fluid.NONE, 200);
			if(result != null && result.getType() == Type.BLOCK)
				return result;
		}
		
		return savedHitResult;
	}
	
	@OnlyIn(Dist.CLIENT)
	public static HitResult getSavedHitResult() {
		return savedHitResult;
	}
	
}
