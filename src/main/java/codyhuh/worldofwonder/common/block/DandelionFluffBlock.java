package codyhuh.worldofwonder.common.block;

import codyhuh.worldofwonder.core.registry.WonderBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public class DandelionFluffBlock extends LeavesBlock {
    public DandelionFluffBlock() {
        super(Block.Properties.of().strength(0.2f).sound(SoundType.WOOL));
    }

    // todo - fix decaying
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource p_221382_) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (Direction direction : Direction.values()) {
            mutable.setWithOffset(pos, direction);
        }

        boolean flag = !level.getBlockState(pos.below(2)).is(WonderBlocks.DANDELION_FLUFF.get()) && !level.getBlockState(pos.above(2)).is(WonderBlocks.DANDELION_FLUFF.get());

        if (this.decaying(state)) {
            if (level.getBlockState(mutable).isAir() && flag) {
                dropResources(state, level, pos);
                level.removeBlock(pos, false);
            }
            if (!level.getBlockState(mutable).isAir()) {
                dropResources(state, level, pos);
                level.removeBlock(pos, false);
            }
        }

    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        super.fallOn(level, state, pos, entity, fallDistance * 0.5f);
    }
}
