package vazkii.quark.base.handler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;

public class StructureBlockReplacementHandler {

	private static Set<StructureFunction> functions = new HashSet<>();

	private static final ThreadLocal<StructureHolder> structureHolder = new ThreadLocal<>();

	public static void addReplacement(StructureFunction func) {
		functions.add(func);
	}
	
	public static BlockState getResultingBlockState(ServerLevelAccessor level, BlockState blockstate) {
		StructureHolder curr = getCurrentStructureHolder();

		if(curr != null && curr.currentStructure != null)
			for(StructureFunction fun : functions) {

				BlockState res = fun.transformBlockstate(level, blockstate, curr);
				if(res != null)
					return res;
			}

		return blockstate;
	}

	private static StructureHolder getCurrentStructureHolder() {
		return structureHolder.get();
	}

	public static void setActiveStructure(Structure structure, PiecesContainer components) {
		StructureHolder curr = getCurrentStructureHolder();
		if(curr == null) {
			curr = new StructureHolder();
			structureHolder.set(curr);
		}

		curr.currentStructure = structure;
		curr.currentComponents = components == null ? null : components.pieces();
	}

	@FunctionalInterface
	public interface StructureFunction {
		BlockState transformBlockstate(ServerLevelAccessor level, BlockState state, StructureHolder structureHolder);
	}

	public static class StructureHolder {
		public Structure currentStructure;
		public List<StructurePiece> currentComponents;
	}

}
