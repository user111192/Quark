package vazkii.quark.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.entity.vehicle.Boat;
import vazkii.quark.content.experimental.module.GameNerfsModule;

@Mixin(Boat.class)
public class BoatMixin {

	@Inject(method = "getGroundFriction", at = @At("RETURN"), cancellable = true)
	private void getGroundFriction(CallbackInfoReturnable<Float> callbackInfoReturnable) {
		callbackInfoReturnable.setReturnValue(GameNerfsModule.getBoatFriction(callbackInfoReturnable.getReturnValueF()));
	}

}
