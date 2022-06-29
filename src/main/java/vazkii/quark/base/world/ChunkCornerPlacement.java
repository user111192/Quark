package vazkii.quark.base.world;

import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class ChunkCornerPlacement extends PlacementModifier {

	private static final ChunkCornerPlacement INSTANCE = new ChunkCornerPlacement();
	public static final Codec<ChunkCornerPlacement> CODEC = Codec.unit(() -> INSTANCE);

	@Nonnull
	@Override // getPositions
	public Stream<BlockPos> getPositions(@Nonnull PlacementContext wdc, @Nonnull RandomSource random, @Nonnull BlockPos pos) {
		return ImmutableSet.of(pos).stream();
	}

	@Nonnull
	@Override
	public PlacementModifierType<?> type() {
		return WorldGenHandler.CHUNK_CORNER_PLACEMENT_TYPE;
	}

}
