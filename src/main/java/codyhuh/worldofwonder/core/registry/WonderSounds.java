package codyhuh.worldofwonder.core.registry;

import codyhuh.worldofwonder.WorldOfWonder;
import com.teamabnormals.blueprint.core.util.registry.SoundSubRegistryHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = WorldOfWonder.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WonderSounds {
    public static final SoundSubRegistryHelper HELPER = WorldOfWonder.REGISTRY_HELPER.getSoundSubHelper();

    public static final RegistryObject<SoundEvent> DANDE_LION_AMBIENT = HELPER.createSoundEvent("entity.dande_lion.ambient");
    public static final RegistryObject<SoundEvent> DANDE_LION_PURR = HELPER.createSoundEvent("entity.dande_lion.purr");
    public static final RegistryObject<SoundEvent> DANDE_LION_HURT = HELPER.createSoundEvent("entity.dande_lion.hurt");
    public static final RegistryObject<SoundEvent> DANDE_LION_DEATH = HELPER.createSoundEvent("entity.dande_lion.death");

    public static final RegistryObject<SoundEvent> MUSIC_BIOME_DANDELION_FIELDS = HELPER.createSoundEvent("music.overworld.dandelion_fields");
}
