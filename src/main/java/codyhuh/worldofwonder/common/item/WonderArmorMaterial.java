package codyhuh.worldofwonder.common.item;

import java.util.function.Supplier;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public class WonderArmorMaterial implements ArmorMaterial {
    private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
    private final String name;
    private final int maxDamageFactor;
    private final int[] damageReductionAmountArray;
    private final int enchantability;
    private final SoundEvent soundEvent;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairMaterial;

    public WonderArmorMaterial(String nameIn, int maxDamageFactorIn, int[] damageReductionAmountsIn, int enchantabilityIn, SoundEvent equipSoundIn, float toughness, float knockbackResistance) {
        this(nameIn, maxDamageFactorIn, damageReductionAmountsIn, enchantabilityIn, equipSoundIn, toughness, knockbackResistance, () -> Ingredient.EMPTY);
    }

    public WonderArmorMaterial(String nameIn, int maxDamageFactorIn, int[] damageReductionAmountsIn, int enchantabilityIn, SoundEvent equipSoundIn, float toughness, float knockbackResistance, Supplier<Ingredient> repairMaterialSupplier) {
        this.name = nameIn;
        this.maxDamageFactor = maxDamageFactorIn;
        this.damageReductionAmountArray = damageReductionAmountsIn;
        this.enchantability = enchantabilityIn;
        this.soundEvent = equipSoundIn;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairMaterial = repairMaterialSupplier;
    }

    public int getDurabilityForType(Type slotIn) {
        return MAX_DAMAGE_ARRAY[slotIn.getSlot().getIndex()] * this.maxDamageFactor;
    }

    public int getDefenseForType(Type slotIn) {
        return this.damageReductionAmountArray[slotIn.getSlot().getIndex()];
    }

    public int getEnchantmentValue() {
        return this.enchantability;
    }

    public SoundEvent getEquipSound() {
        return this.soundEvent;
    }

    public Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }

    public String getName() {
        return this.name;
    }

    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return knockbackResistance;
    }
}
