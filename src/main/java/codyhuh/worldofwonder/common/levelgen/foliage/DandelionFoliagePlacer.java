package codyhuh.worldofwonder.common.levelgen.foliage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import codyhuh.worldofwonder.core.registry.WonderFoliagePlacers;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public class DandelionFoliagePlacer extends FoliagePlacer {
	public static final Codec<DandelionFoliagePlacer> CODEC = RecordCodecBuilder.create(placerInstance -> foliagePlacerParts(placerInstance).apply(placerInstance, DandelionFoliagePlacer::new));

	public DandelionFoliagePlacer(IntProvider radius, IntProvider offset) {
		super(radius, offset);
	}

	@Override
	protected FoliagePlacerType<?> type() {
		return WonderFoliagePlacers.DANDELION.get();
	}

	@Override
	protected void createFoliage(LevelSimulatedReader level, FoliageSetter blockSetter, RandomSource random, TreeConfiguration config, int maxTreeHeight, FoliageAttachment attachment, int foliageHeight, int foliageRadius, int offset) {
		for (int i = 0; i < 3; i++) {
			placeLeavesRow(level, blockSetter, random, config, attachment.pos(), 3, i, attachment.doubleTrunk());
		}
	}

	@Override
	public int foliageHeight(RandomSource random, int height, TreeConfiguration config) {
		return height - 1;
	}

	@Override
	protected boolean shouldSkipLocation(RandomSource random, int localX, int localY, int localZ, int range, boolean large) {
		boolean skip = true;
		int distance = localX * localX + localZ * localZ;
		switch (localY) {
			case 0, 2 -> {
				skip = distance > 4;
			}
			case 1 -> {
				int distanceX = Math.abs(localX);
				int distanceZ = Math.abs(localZ);
				skip = distance > 9;
				if (distanceX == 3 && distanceZ == 3) skip = true;
			}
			case 3 -> {
				if (localX == 0 && localZ == 0) {
					skip = false;
					break;
				}
				skip = DandelionFluffFoliagePlacer.isNotCenterEdge(localX, localZ, 1);
			}
		}
		//		if (!skip && potentialDecay) {
		//			skip = random.nextInt(6) == 0;
		//		}
		return skip;
	}
}
