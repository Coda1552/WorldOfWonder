package codyhuh.worldofwonder.core.other;

import codyhuh.worldofwonder.core.registry.WonderFeatures;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;

public class WonderGeneration {
    public static void dandelionFields(BiomeGenerationSettings.Builder generation) {
        OverworldBiomes.globalOverworldGeneration(generation);
        BiomeDefaultFeatures.addPlainGrass(generation);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addDefaultSoftDisks(generation);
        BiomeDefaultFeatures.addDefaultFlowers(generation);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST);
        BiomeDefaultFeatures.addDefaultMushrooms(generation);
        BiomeDefaultFeatures.addDefaultExtraVegetation(generation);
        addDandelionFieldsVegetation(generation);
    }

    public static void addDandelionFieldsVegetation(BiomeGenerationSettings.Builder builder) {
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WonderFeatures.WonderPlacedFeatures.PLACED_DANDELION_FLOWERS);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WonderFeatures.WonderPlacedFeatures.PLACED_DANDELION_FLUFF);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WonderFeatures.WonderPlacedFeatures.PLACED_DANDELION);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WonderFeatures.WonderPlacedFeatures.PLACED_MELLOW_PETALS);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WonderFeatures.WonderPlacedFeatures.PLACED_LILAC);
    }
}
