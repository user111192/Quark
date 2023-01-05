package vazkii.quark.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseCoralPlantTypeBlock;
import net.minecraft.world.level.block.state.BlockState;
import vazkii.quark.content.tweaks.module.CoralOnCactusModule;

@Mixin(BaseCoralPlantTypeBlock.class)
public class BaseCoralPlantTypeBlockMixin {

	@Inject(method = "scanForWater", at = @At("RETURN"), cancellable = true)
	private static void scanForWater(BlockState state, BlockGetter getter, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue((Boolean) CoralOnCactusModule.scanForWater(state, getter, pos, cir.getReturnValueZ()));
	}
	
	@Inject(method = "canSurvive", at = @At("RETURN"), cancellable = true)
	private void canSurvive(BlockState state, LevelReader getter, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue((Boolean) CoralOnCactusModule.scanForWater(state, getter, pos, cir.getReturnValueZ()));
	}
	
}
