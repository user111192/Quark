package vazkii.quark.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import vazkii.quark.content.client.module.GreenerGrassModule;

@Mixin(BiomeColors.class)
public class BiomeColorsMixin {

	@Inject(method = "getAverageWaterColor", at = @At("RETURN"), cancellable = true)
	private static void getAverageWaterColor(BlockAndTintGetter getter, BlockPos pos, CallbackInfoReturnable<Integer> callbackInfoReturnable) {
		callbackInfoReturnable.setReturnValue(GreenerGrassModule.getWaterColor(callbackInfoReturnable.getReturnValueI()));
	}
	
}
