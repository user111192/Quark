package vazkii.quark.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.level.biome.Biome;
import vazkii.quark.content.client.module.GreenerGrassModule;

@Mixin(Biome.class)
public class BiomeMixin {

	@Inject(method = "getWaterColor", at = @At("RETURN"), cancellable = true)
	private void getWaterColor(CallbackInfoReturnable<Integer> callbackInfoReturnable) {
		set(callbackInfoReturnable);
	}
	
	@Inject(method = "getWaterFogColor", at = @At("RETURN"), cancellable = true)
	private void getWaterFogColor(CallbackInfoReturnable<Integer> callbackInfoReturnable) {
		set(callbackInfoReturnable);
	}
	
	private static void set(CallbackInfoReturnable<Integer> callbackInfoReturnable) {
		callbackInfoReturnable.setReturnValue(GreenerGrassModule.getWaterColor(callbackInfoReturnable.getReturnValueI()));	
	}
	
}
