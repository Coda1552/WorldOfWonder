package codyhuh.worldofwonder.core.registry;

import codyhuh.worldofwonder.WorldOfWonder;
import codyhuh.worldofwonder.common.block.MellowPetalsBlock;
import codyhuh.worldofwonder.common.levelgen.MellowPatchFeature;
import codyhuh.worldofwonder.common.levelgen.config.MellowPatchConfig;
import codyhuh.worldofwonder.common.levelgen.foliage.DandelionFluffFoliagePlacer;
import codyhuh.worldofwonder.common.levelgen.foliage.DandelionFoliagePlacer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.RandomPatchFeature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

@Mod.EventBusSubscriber(modid = WorldOfWonder.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WonderFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, WorldOfWonder.MOD_ID);
    public static final RegistryObject<Feature<TreeConfiguration>> DANDELION_TREE = FEATURES.register("dandelion_tree", () -> new TreeFeature(TreeConfiguration.CODEC));
    public static final RegistryObject<Feature<TreeConfiguration>> FLUFFY_DANDELION_TREE = FEATURES.register("fluffy_dandelion_tree", () -> new TreeFeature(TreeConfiguration.CODEC));
    public static final RegistryObject<Feature<RandomPatchConfiguration>> DANDELION_PATCH = FEATURES.register("dandelion_patch", () -> new RandomPatchFeature(RandomPatchConfiguration.CODEC));
    public static final RegistryObject<Feature<MellowPatchConfig>> MELLOW_FLOWER_PATCH = FEATURES.register("mellow_flower_patch", () -> new MellowPatchFeature(MellowPatchConfig.CODEC));
    public static final RegistryObject<Feature<RandomPatchConfiguration>> LILAC_PATCH = FEATURES.register("lilac_patch", () -> new RandomPatchFeature(RandomPatchConfiguration.CODEC));

    public static final class WonderConfiguredFeatures {
        public static final ResourceKey<ConfiguredFeature<?, ?>> DANDELION = createKey("dandelion");
        public static final ResourceKey<ConfiguredFeature<?, ?>> DANDELION_FLUFF = createKey("dandelion_fluff");
        public static final ResourceKey<ConfiguredFeature<?, ?>> DANDELION_FLOWERS = createKey("dandelion_flowers");
        public static final ResourceKey<ConfiguredFeature<?, ?>> MELLOW_PETALS = createKey("mellow_petals");
        public static final ResourceKey<ConfiguredFeature<?, ?>> LILAC = createKey("lilac");

        public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
//            HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);

            register(context, DANDELION, WonderFeatures.DANDELION_TREE.get(), new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(WonderBlocks.STEM_LOG.get().defaultBlockState()),
                    new StraightTrunkPlacer(4, 2, 0),
                    BlockStateProvider.simple(WonderBlocks.DANDELION_PETALS.get().defaultBlockState()),
                    new DandelionFoliagePlacer(UniformInt.of(3, 3), UniformInt.of(0, 0)),
                    new TwoLayersFeatureSize(1, 0, 1)).ignoreVines().build());
            register(context, DANDELION_FLUFF, WonderFeatures.FLUFFY_DANDELION_TREE.get(),
                    new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(WonderBlocks.STEM_LOG.get().defaultBlockState()),
                            new StraightTrunkPlacer(6, 2, 0),
                            BlockStateProvider.simple(WonderBlocks.DANDELION_FLUFF.get().defaultBlockState()),
                            new DandelionFluffFoliagePlacer(UniformInt.of(2, 2), UniformInt.of(0, 0)),
                            new TwoLayersFeatureSize(1, 0, 1)).ignoreVines().build());
            register(context, DANDELION_FLOWERS, WonderFeatures.DANDELION_PATCH.get(),
                    grassPatch(BlockStateProvider.simple(Blocks.DANDELION), 32));
            register(context, LILAC, WonderFeatures.LILAC_PATCH.get(),
                    grassPatch(BlockStateProvider.simple(Blocks.LILAC), 32));

            SimpleWeightedRandomList.Builder<BlockState> petalsListBuilder = SimpleWeightedRandomList.builder();

            for(int i = 1; i <= 4; ++i) {
                for(Direction direction : Direction.Plane.HORIZONTAL) {
                    petalsListBuilder.add(WonderBlocks.MELLOW_PETALS.get().defaultBlockState()
                            .setValue(MellowPetalsBlock.AMOUNT, Integer.valueOf(i)).setValue(MellowPetalsBlock.FACING, direction), 1);
                }
            }

            register(context, MELLOW_PETALS, WonderFeatures.MELLOW_FLOWER_PATCH.get(),
                    new MellowPatchConfig(96, 6, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                                    new SimpleBlockConfiguration(new WeightedStateProvider(petalsListBuilder)))));
        }

        private static RandomPatchConfiguration grassPatch(BlockStateProvider p_195203_, int p_195204_) {
            return FeatureUtils.simpleRandomPatchConfiguration(p_195204_, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(p_195203_)));
        }

        public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String id) {
            return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(WorldOfWonder.MOD_ID, id));
        }

        public static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC config) {
            context.register(key, new ConfiguredFeature<>(feature, config));
        }
    }

    public static final class WonderPlacedFeatures {
        public static final ResourceKey<PlacedFeature> PLACED_DANDELION = createKey("placed_dandelion");
        public static final ResourceKey<PlacedFeature> PLACED_DANDELION_FLUFF = createKey("placed_dandelion_fluff");
        public static final ResourceKey<PlacedFeature> PLACED_DANDELION_FLOWERS = createKey("placed_dandelion_flowers");
        public static final ResourceKey<PlacedFeature> PLACED_MELLOW_PETALS = createKey("placed_mellow_petals");
        public static final ResourceKey<PlacedFeature> PLACED_LILAC = createKey("placed_lilac");

        public static void bootstrap(BootstapContext<PlacedFeature> context) {
            PlacementModifier placementmodifier = SurfaceWaterDepthFilter.forMaxDepth(0);
            register(context, PLACED_DANDELION_FLOWERS, WonderConfiguredFeatures.DANDELION_FLOWERS, NoiseThresholdCountPlacement.of(-0.8D, 15, 4), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());
            register(context, PLACED_DANDELION, WonderConfiguredFeatures.DANDELION, PlacementUtils.countExtra(1, 0.05F, 1), InSquarePlacement.spread(), placementmodifier, PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(Blocks.OAK_SAPLING.defaultBlockState(), BlockPos.ZERO)), BiomeFilter.biome());
            register(context, PLACED_DANDELION_FLUFF, WonderConfiguredFeatures.DANDELION_FLUFF, PlacementUtils.countExtra(1, 0.05F, 1), InSquarePlacement.spread(), placementmodifier, PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(Blocks.OAK_SAPLING.defaultBlockState(), BlockPos.ZERO)), BiomeFilter.biome());
            register(context, PLACED_MELLOW_PETALS, WonderConfiguredFeatures.MELLOW_PETALS,
                    RarityFilter.onAverageOnceEvery(2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome(), BlockPredicateFilter.forPredicate(BlockPredicate.anyOf(BlockPredicate.ONLY_IN_AIR_PREDICATE)));
            register(context, PLACED_LILAC, WonderConfiguredFeatures.LILAC, PlacementUtils.countExtra(1, 0.05F, 1), InSquarePlacement.spread(), placementmodifier, PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(Blocks.LILAC.defaultBlockState(), BlockPos.ZERO)), BiomeFilter.biome());
        }

        public static ResourceKey<PlacedFeature> createKey(String name) {
            return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(WorldOfWonder.MOD_ID, name));
        }

        public static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, ResourceKey<ConfiguredFeature<?, ?>> feature, List<PlacementModifier> modifiers) {
            context.register(key, new PlacedFeature(context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(feature), modifiers));
        }

        public static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, ResourceKey<ConfiguredFeature<?, ?>> feature, PlacementModifier... modifiers) {
            register(context, key, feature, List.of(modifiers));
        }
    }
}
