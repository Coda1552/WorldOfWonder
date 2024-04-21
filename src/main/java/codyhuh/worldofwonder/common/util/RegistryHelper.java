package codyhuh.worldofwonder.common.util;

import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.Consumer;

public class RegistryHelper {
	private final Map<IForgeRegistry<?>, ISubRegistryHelper<?>> subHelpers = Maps.newHashMap();
	protected final String modId;

	public RegistryHelper(String modId) {
		this.modId = modId;
		this.putDefaultSubHelpers();
	}

	public static RegistryHelper create(String modId, Consumer<RegistryHelper> consumer) {
		RegistryHelper helper = new RegistryHelper(modId);
		consumer.accept(helper);
		return helper;
	}

	public String getModId() {
		return this.modId;
	}

	public ResourceLocation prefix(String name) {
		return new ResourceLocation(this.modId, name);
	}

	public <V> void putSubHelper(IForgeRegistry<V> registry, ISubRegistryHelper<V> subHelper) {
		this.subHelpers.put(registry, subHelper);
	}

	protected void putDefaultSubHelpers() {
		this.putSubHelper(ForgeRegistries.ITEMS, new ItemSubRegistryHelper(this));
		this.putSubHelper(ForgeRegistries.BLOCKS, new BlockSubRegistryHelper(this));
		this.putSubHelper(ForgeRegistries.BLOCK_ENTITY_TYPES, new BlockEntitySubRegistryHelper(this));
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public <T, S extends ISubRegistryHelper<T>> S getSubHelper(IForgeRegistry<T> registry) {
		S subHelper = (S) this.subHelpers.get(registry);
		if (subHelper == null) {
			throw new NullPointerException("No Sub Helper is registered for the forge registry: " + registry);
		}
		return subHelper;
	}

	@Nonnull
	public <T extends AbstractSubRegistryHelper<Item>> T getItemSubHelper() {
		return this.getSubHelper(ForgeRegistries.ITEMS);
	}

	@Nonnull
	public <T extends AbstractSubRegistryHelper<Block>> T getBlockSubHelper() {
		return this.getSubHelper(ForgeRegistries.BLOCKS);
	}

	@Nonnull
	public <T extends AbstractSubRegistryHelper<SoundEvent>> T getSoundSubHelper() {
		return this.getSubHelper(ForgeRegistries.SOUND_EVENTS);
	}

	@Nonnull
	public <T extends AbstractSubRegistryHelper<BlockEntityType<?>>> T getBlockEntitySubHelper() {
		return this.getSubHelper(ForgeRegistries.BLOCK_ENTITY_TYPES);
	}

	@Nonnull
	public <T extends AbstractSubRegistryHelper<EntityType<?>>> T getEntitySubHelper() {
		return this.getSubHelper(ForgeRegistries.ENTITY_TYPES);
	}

	public void register(IEventBus eventBus) {
		this.subHelpers.values().forEach(helper -> {
			helper.register(eventBus);
		});
	}
}