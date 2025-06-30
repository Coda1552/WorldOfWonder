package codyhuh.worldofwonder.core.registry;

import codyhuh.worldofwonder.WorldOfWonder;
import codyhuh.worldofwonder.common.item.ItemDandelionHat;
import codyhuh.worldofwonder.common.item.MellowPetalsBlockItem;
import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import com.teamabnormals.blueprint.core.util.registry.ItemSubRegistryHelper;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import static net.minecraft.world.item.CreativeModeTabs.*;
import static net.minecraft.world.item.Items.*;
import static net.minecraft.world.item.crafting.Ingredient.of;

@Mod.EventBusSubscriber(modid = WorldOfWonder.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WonderItems {
    public static final ItemSubRegistryHelper HELPER = WorldOfWonder.REGISTRY_HELPER.getItemSubHelper();

    public static final Pair<RegistryObject<Item>, RegistryObject<Item>> STEM_BOAT = HELPER.createBoatAndChestBoatItem("stem", WonderBlocks.STEM_PLANKS);
    public static final RegistryObject<Item> DANDELION_HAT = HELPER.createItem("dandelion_hat", ItemDandelionHat::new);
    public static final RegistryObject<Item> BLOOM_MEAL = HELPER.createItem("bloom_meal", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MELLOW_PETALS = HELPER.createItem("mellow_petals", () -> new MellowPetalsBlockItem(WonderBlocks.MELLOW_PETALS.get(), new Item.Properties()));

    public static final RegistryObject<ForgeSpawnEggItem> DANDELION_SPAWN_EGG = HELPER.createSpawnEggItem("dande_lion", WonderEntityTypes.DANDE_LION::get, 0x9ad746, 0xf8ff00);

    public static void setupTabEditors() {
        CreativeModeTabContentsPopulator.mod(WorldOfWonder.MOD_ID)
                .tab(INGREDIENTS).addItemsAfter(of(BONE_MEAL), BLOOM_MEAL)
                .tab(SPAWN_EGGS).addItems(DANDELION_SPAWN_EGG)
                .tab(NATURAL_BLOCKS).addItemsAfter(of(PINK_PETALS), MELLOW_PETALS)
                .tab(COMBAT).addItemsAfter(of(TURTLE_HELMET), DANDELION_HAT)
                .tab(TOOLS_AND_UTILITIES).addItemsBefore(of(BAMBOO_RAFT), STEM_BOAT.getFirst(), STEM_BOAT.getSecond())
                .tab(WonderTabs.ITEM_GROUP.getKey())
                .addItems(BLOOM_MEAL, STEM_BOAT.getFirst(), STEM_BOAT.getSecond(), DANDELION_HAT, DANDELION_SPAWN_EGG);
        ;
    }
}
