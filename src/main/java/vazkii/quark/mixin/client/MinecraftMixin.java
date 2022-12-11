package vazkii.quark.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import vazkii.quark.content.client.module.LongRangePickBlockModule;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	
	@Inject(method = "pickBlock", at = @At("HEAD"))
	private void pickBlockHead(CallbackInfo ci) {
		((Minecraft) (Object) this).hitResult = LongRangePickBlockModule.transformHitResult(((Minecraft) (Object) this).hitResult);
	}
	
	@Inject(method = "pickBlock", at = @At("RETURN"))
	private void pickBlockReturn(CallbackInfo ci) {
		((Minecraft) (Object) this).hitResult = LongRangePickBlockModule.getSavedHitResult();
	}

}
