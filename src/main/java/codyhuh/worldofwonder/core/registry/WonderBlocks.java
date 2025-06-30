package codyhuh.worldofwonder.core.registry;

import codyhuh.worldofwonder.WorldOfWonder;
import codyhuh.worldofwonder.common.block.*;
import codyhuh.worldofwonder.core.other.WonderProperties;
import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.common.block.LogBlock;
import com.teamabnormals.blueprint.common.block.sign.BlueprintCeilingHangingSignBlock;
import com.teamabnormals.blueprint.common.block.sign.BlueprintStandingSignBlock;
import com.teamabnormals.blueprint.common.block.sign.BlueprintWallHangingSignBlock;
import com.teamabnormals.blueprint.common.block.sign.BlueprintWallSignBlock;
import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import static net.minecraft.world.item.CreativeModeTabs.*;
import static net.minecraft.world.item.crafting.Ingredient.of;

@Mod.EventBusSubscriber(modid = WorldOfWonder.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WonderBlocks {
    public static final BlockSubRegistryHelper HELPER = WorldOfWonder.REGISTRY_HELPER.getBlockSubHelper();

    public static final RegistryObject<Block> STRIPPED_STEM_LOG = HELPER.createBlock("stripped_stem_log", () -> new RotatedPillarBlock(WonderProperties.STEM.log()));
    public static final RegistryObject<Block> STRIPPED_STEM_WOOD = HELPER.createBlock("stripped_stem_wood", () -> new RotatedPillarBlock(WonderProperties.STEM.log()));
    public static final RegistryObject<Block> STEM_LOG = HELPER.createBlock("stem_log", () -> new LogBlock(STRIPPED_STEM_LOG, WonderProperties.STEM.log()));
    public static final RegistryObject<Block> STEM_WOOD = HELPER.createBlock("stem_wood", () -> new LogBlock(STRIPPED_STEM_WOOD, WonderProperties.STEM.log()));
    public static final RegistryObject<Block> DANDELION_FLUFF = HELPER.createBlock("dandelion_fluff", DandelionFluffBlock::new, new Item.Properties());
    public static final RegistryObject<Block> DANDELION_PETALS = HELPER.createBlock("dandelion_petals", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_YELLOW).strength(0.2F).sound(SoundType.GRASS)), new Item.Properties());
    public static final RegistryObject<Block> STEM_PLANKS = HELPER.createBlock("stem_planks", () -> new Block(WonderProperties.STEM.planks()));
    public static final RegistryObject<Block> STEM_STAIRS = HELPER.createBlock("stem_stairs", () -> new StairBlock(() -> STEM_PLANKS.get().defaultBlockState(), WonderProperties.STEM.planks()));
    public static final RegistryObject<Block> STEM_SLAB = HELPER.createBlock("stem_slab", () -> new SlabBlock(WonderProperties.STEM.planks()));
    public static final RegistryObject<Block> STEM_PRESSURE_PLATE = HELPER.createBlock("stem_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, WonderProperties.STEM.pressurePlate(), WonderProperties.STEM_BLOCK_SET));
    public static final RegistryObject<Block> STEM_BUTTON = HELPER.createBlock("stem_button", () -> new ButtonBlock(WonderProperties.STEM.button(), WonderProperties.STEM_BLOCK_SET, 30, true));
    public static final RegistryObject<Block> STEM_FENCE = HELPER.createFuelBlock("stem_fence", () -> new FenceBlock(WonderProperties.STEM.planks()), 300);
    public static final RegistryObject<Block> STEM_FENCE_GATE = HELPER.createFuelBlock("stem_fence_gate", () -> new FenceGateBlock(WonderProperties.STEM.planks(), WonderProperties.STEM_WOOD_TYPE), 300);
    public static final RegistryObject<Block> STEM_DOOR = HELPER.createBlock("stem_door", () -> new DoorBlock(WonderProperties.STEM.door(), WonderProperties.STEM_BLOCK_SET));
    public static final RegistryObject<Block> STEM_TRAPDOOR = HELPER.createBlock("stem_trapdoor", () -> new TrapDoorBlock(WonderProperties.STEM.trapdoor(), WonderProperties.STEM_BLOCK_SET));

    public static final Pair<RegistryObject<BlueprintStandingSignBlock>, RegistryObject<BlueprintWallSignBlock>> STEM_SIGNS = HELPER.createSignBlock("stem", WonderProperties.STEM_WOOD_TYPE, WonderProperties.STEM.sign());
    public static final Pair<RegistryObject<BlueprintCeilingHangingSignBlock>, RegistryObject<BlueprintWallHangingSignBlock>> STEM_HANGING_SIGNS = HELPER.createHangingSignBlock("stem", WonderProperties.STEM_WOOD_TYPE, WonderProperties.STEM.hangingSign());

    //public static final RegistryObject<Block> STEM_BOOKSHELF = HELPER.createFuelBlock("stem_bookshelf", () -> new Block(WonderProperties.STEM.bookshelf()), 300);
    //public static final RegistryObject<Block> STEM_LADDER = HELPER.createFuelBlock("stem_ladder", () -> new LadderBlock(WonderProperties.STEM.ladder()), 300);

    //public static final RegistryObject<BlueprintChestBlock> STEM_CHEST = HELPER.createChestBlock("stem", WonderProperties.STEM.chest());
    //public static final RegistryObject<BlueprintTrappedChestBlock> TRAPPED_STEM_CHEST = HELPER.createTrappedChestBlock("stem", WonderProperties.STEM.chest());

    public static final RegistryObject<Block> STEM_VERTICAL_SLAB = HELPER.createBlock("stem_vertical_slab", () -> new VerticalSlabBlock(Block.Properties.copy(STEM_SLAB.get())));

    public static final RegistryObject<Block> DANDE_LION_SPROUT = HELPER.createBlock("dande_lion_sprout", DandeLionSproutBlock::new, new Item.Properties());
    public static final RegistryObject<Block> POTTED_DANDE_LION_SPROUT = HELPER.createBlockNoItem("potted_dande_lion_sprout", PottedSproutBlock::new);

    public static final RegistryObject<Block> MELLOW_PETALS = HELPER.createBlockNoItem("mellow_petals", () -> new MellowPetalsBlock(Block.Properties.copy(Blocks.PINK_PETALS).mapColor(MapColor.COLOR_YELLOW)));

    public static void setupTabEditors() {
        CreativeModeTabContentsPopulator.mod(WorldOfWonder.MOD_ID)
                .tab(BUILDING_BLOCKS)
                .addItemsBefore(of(Blocks.BAMBOO_BLOCK), STEM_LOG, STEM_WOOD, STRIPPED_STEM_LOG, STRIPPED_STEM_WOOD, STEM_PLANKS)
                .addItemsBefore(of(Blocks.BAMBOO_BLOCK),
                        STEM_STAIRS, STEM_SLAB, STEM_FENCE, STEM_FENCE_GATE, STEM_DOOR, STEM_TRAPDOOR, STEM_PRESSURE_PLATE, STEM_BUTTON)
                .tab(FUNCTIONAL_BLOCKS)
                .addItemsBefore(of(Blocks.BAMBOO_SIGN),
                        STEM_SIGNS.getFirst(), STEM_HANGING_SIGNS.getFirst())
                .tab(NATURAL_BLOCKS)
                .addItemsBefore(of(Blocks.MUSHROOM_STEM), STEM_LOG)
                .addItemsAfter(of(Blocks.AZALEA_LEAVES), DANDELION_PETALS, DANDELION_FLUFF)
                .tab(WonderTabs.ITEM_GROUP.getKey())
                        .addItems(DANDE_LION_SPROUT, STEM_PLANKS, STEM_LOG, STRIPPED_STEM_LOG, STEM_WOOD, STRIPPED_STEM_WOOD,
                                STEM_STAIRS, STEM_SLAB, STEM_FENCE, STEM_FENCE_GATE, STEM_DOOR, STEM_TRAPDOOR,
                                STEM_PRESSURE_PLATE, STEM_BUTTON, STEM_SIGNS.getFirst(), STEM_HANGING_SIGNS.getFirst(),
                                DANDELION_PETALS, DANDELION_FLUFF, MELLOW_PETALS)
                //.tab(FUNCTIONAL_BLOCKS)
                //.addItems(STEM_LADDER, STEM_CHEST)
                //.tab(REDSTONE_BLOCKS)
                //.addItems(TRAPPED_STEM_CHEST)
        ;
    }
}

