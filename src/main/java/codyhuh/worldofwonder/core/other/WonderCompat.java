package codyhuh.worldofwonder.core.other;

import codyhuh.worldofwonder.core.registry.WonderBlocks;
import codyhuh.worldofwonder.core.registry.WonderItems;
import com.teamabnormals.blueprint.core.util.DataUtil;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.DispenserBlock;

public class WonderCompat {

    public static void registerCompat() {
        DispenserBlock.registerBehavior(WonderItems.BLOOM_MEAL.get(), WonderEvents.BLOOM_MEAL_DISPENSE);
        WonderCompat.registerCompostable(WonderBlocks.DANDELION_PETALS.get().asItem(), 0.3F);
        WonderCompat.registerCompostable(WonderBlocks.DANDELION_FLUFF.get().asItem(), 0.3F);
        WonderCompat.registerFlammables();
    }

    public static void registerCompostable(ItemLike itemIn, float chance) {
        ComposterBlock.COMPOSTABLES.put(itemIn, chance);
    }

    public static void registerFlammables() {
        DataUtil.registerFlammable(WonderBlocks.STEM_PLANKS.get(), 5, 20);
        DataUtil.registerFlammable(WonderBlocks.STEM_STAIRS.get(), 5, 20);
        DataUtil.registerFlammable(WonderBlocks.STEM_SLAB.get(), 5, 20);
        DataUtil.registerFlammable(WonderBlocks.STEM_FENCE.get(), 5, 20);
        DataUtil.registerFlammable(WonderBlocks.STEM_FENCE_GATE.get(), 5, 20);
        DataUtil.registerFlammable(WonderBlocks.STEM_LOG.get(), 5, 20);
        DataUtil.registerFlammable(WonderBlocks.STRIPPED_STEM_LOG.get(), 5, 20);
        DataUtil.registerFlammable(WonderBlocks.STEM_WOOD.get(), 5, 20);
        DataUtil.registerFlammable(WonderBlocks.STRIPPED_STEM_WOOD.get(), 5, 20);
        DataUtil.registerFlammable(WonderBlocks.DANDELION_PETALS.get(), 5, 60);
        DataUtil.registerFlammable(WonderBlocks.DANDELION_FLUFF.get(), 5, 60);
        //DataUtil.registerFlammable(WonderBlocks.STEM_BOOKSHELF.get(), 30, 60);
    }
}
