package codyhuh.worldofwonder.common.levelgen;

import codyhuh.worldofwonder.common.block.MellowPetalsBlock;
import codyhuh.worldofwonder.common.levelgen.config.MellowPatchConfig;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import codyhuh.worldofwonder.common.levelgen.config.MellowPatchConfig;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.material.Fluids;

public class MellowPatchFeature extends Feature<MellowPatchConfig> {
    public MellowPatchFeature(Codec<MellowPatchConfig> p_66605_) {
        super(p_66605_);
    }

    public boolean place(FeaturePlaceContext<MellowPatchConfig> context) {
        MellowPatchConfig config = context.config();
        RandomSource random = context.random();
        BlockPos origin = context.origin();
        WorldGenLevel level = context.level();

        int i = 0;
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        int j = config.xzSpread() + 1;
        int k = config.ySpread() + 1;

        PlacedFeature placedFeature = config.feature().value();
        ConfiguredFeature<?, ?> configuredFeature = placedFeature.feature().value();
        SimpleBlockConfiguration simpleBlockConfig = (SimpleBlockConfiguration) configuredFeature.config();

        for(int l = 0; l < config.tries(); ++l) {

            mutablePos.setWithOffset(origin, random.nextInt(j) - random.nextInt(j), random.nextInt(k) - random.nextInt(k), random.nextInt(j) - random.nextInt(j));
            BlockState stateToBe = simpleBlockConfig.toPlace().getState(random, mutablePos);

            if (!level.getFluidState(mutablePos).is(Fluids.EMPTY) || !level.getBlockState(mutablePos).canBeReplaced()) {
                continue;
            }
            boolean waterborne = level.getFluidState(mutablePos.below()).is(Fluids.WATER);
            boolean earthborne = level.getBlockState(mutablePos.below()).is(BlockTags.DIRT)
                    && level.getBlockState(mutablePos).isAir() && level.getBlockState(mutablePos.above()).canBeReplaced();
            if (waterborne) {
                stateToBe = stateToBe.setValue(MellowPetalsBlock.WATERBORNE, true);
            }


            if (earthborne || waterborne) {
                if (level.setBlock(mutablePos, stateToBe, 3)) {
                    ++i;
                }
            }
        }

        return i > 0;
    }
}