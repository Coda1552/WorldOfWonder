package codyhuh.worldofwonder.common.entity;

import codyhuh.worldofwonder.core.registry.WonderBlocks;
import codyhuh.worldofwonder.core.registry.WonderEntityTypes;
import codyhuh.worldofwonder.core.registry.WonderSounds;
import codyhuh.worldofwonder.core.other.WonderItemTags;
import com.davigj.just_dandy.core.registry.JDParticleTypes;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class DandeLionEntity extends TamableAnimal {
    private static final EntityDataAccessor<Boolean> SHEARED = SynchedEntityData.defineId(DandeLionEntity.class, EntityDataSerializers.BOOLEAN);
    private boolean madeChild;
    private int shearedTicks;

    public DandeLionEntity(EntityType<? extends DandeLionEntity> type, Level levelIn) {
        super(type, levelIn);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(7, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(4, new NonTameRandomTargetGoal<>(this, Animal.class, false, entity -> entity.getBbWidth() <= 0.75F && entity.getBbHeight() <= 0.75F && !(entity instanceof DandeLionEntity)));
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.ATTACK_DAMAGE, 6)
                .add(Attributes.ATTACK_KNOCKBACK, 0.2);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SHEARED, false);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        if (random.nextBoolean()) shear();
        return super.finalizeSpawn(levelIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Angry", this.isAngry());
        compound.putBoolean("Sheared", this.isSheared());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setAngry(compound.getBoolean("Angry"));
        this.setSheared(compound.getBoolean("Sheared"));
    }

    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (this.level().isClientSide) {
            boolean flag = this.isOwnedBy(player) || this.isTame() || stack.is(ItemTags.SMALL_FLOWERS)
                    || stack.is(Tags.Items.SHEARS) && !this.isTame() && !this.isAngry();
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else {
            if (!isSheared() && stack.is(Tags.Items.SHEARS)) {
                shear();
                this.playSound(SoundEvents.SHEEP_SHEAR, getSoundVolume(), 1);
                spawnAtLocation(new ItemStack(WonderBlocks.DANDELION_FLUFF.get(), random.nextInt(2) + 1));
                return InteractionResult.SUCCESS;
            }
            if (stack.getItem() instanceof SpawnEggItem egg && egg.spawnsEntity(stack.getTag(), this.getType())) {
                BlockState sprout = WonderBlocks.DANDE_LION_SPROUT.get().defaultBlockState();
                if (sprout.canSurvive(level(), blockPosition())
                && level().getBlockState(blockPosition()).canBeReplaced()) {
                    level().setBlock(blockPosition(), sprout, 3);
                    ParticleOptions particle = ParticleTypes.WAX_OFF;
                    if (ModList.get().isLoaded("just_dandy")) {
                        particle = JDParticleTypes.DANDELION_FLUFF.get();
                    }
                    for (int i = 0; i < 4; i++) {
                        ((ServerLevel)level()).sendParticles(particle, blockPosition().getX() + random.nextGaussian() * 0.2,
                                getEyeY() + random.nextGaussian() * 0.5, blockPosition().getZ() + random.nextGaussian() * 0.2,
                                1,0, 0, 0, 1);
                    }
                } else {
                    DandeLionSeedEntity seed = WonderEntityTypes.DANDE_LION_SEED.get().create(level());
                    if (seed != null) {
                        double rotation = random.nextInt(360);
                        seed.setPos(getX(), getY(), getZ());
                        Vec3 target = seed.position().add(new Vec3(Mth.cos((float)Math.toRadians(rotation)), 0, Mth.sin((float)Math.toRadians(rotation))).scale(16));
                        seed.setTarget(target.x, target.z);
                        resetLove();
                        level().addFreshEntity(seed);
                    }
                }
                return InteractionResult.SUCCESS;
            }
            if (this.isTame()) {
                if (this.isFood(stack) && this.getHealth() < this.getMaxHealth()) {
                    // heal it if it's food
                    this.heal(4);
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                    this.gameEvent(GameEvent.EAT, this);
                    return InteractionResult.SUCCESS;
                } else {
                    if (stack.is(WonderItemTags.FERTILIZER) && (getHealth() < getMaxHealth() || shearedTicks != 0)) {
                        if (!isBaby()) {
                            if (!player.getAbilities().instabuild) {
                                stack.shrink(1);
                            }
                            this.heal(4);
                            if (shearedTicks > 0) {
                                shearedTicks -= 3000;
                                if (shearedTicks < 0) {
                                    shearedTicks = 0;
                                    setSheared(false);
                                }
                            }
                        } else {
                            setAge((int) (this.age * 0.9));
                        }
                        if (level() instanceof ServerLevel server) {
                            this.playSound(SoundEvents.BONE_MEAL_USE, getSoundVolume(), 1);
                            for (int i = 0; i < 5; ++i) {
                                RandomSource random = level().getRandom();
                                server.sendParticles(ParticleTypes.HAPPY_VILLAGER, this.getEyePosition().x + random.nextGaussian() * 0.25,
                                        this.getEyeY() + (random.nextGaussian() * 0.25) - 0.15, this.getEyePosition().z + random.nextGaussian() * 0.25,
                                        1, 0.0D, 0.0D, 0.0D, 1.0D);
                            }
                        }
                        return InteractionResult.SUCCESS;
                    }
                    if (this.isFood(stack)) {
                        if (this.getAge() == 0 && this.canFallInLove()) {
                            if (flowersAround()) {
                                this.usePlayerItem(player, hand, stack);
                                this.setInLove(player);
                                player.swing(hand, true);
                            } else {
                                level().broadcastEntityEvent(this, (byte) 5);
                            }
                            return InteractionResult.SUCCESS;
                        }
                        if (this.isBaby()) {
                            this.usePlayerItem(player, hand, stack);
                            this.ageUp((int) (this.getAge() / -20.0 * 0.1), true);
                            return InteractionResult.SUCCESS;
                        }
                    }
                    InteractionResult interactionresult = super.mobInteract(player, hand);
                    if ((!interactionresult.consumesAction() || this.isBaby()) && this.isOwnedBy(player)) {
                        this.setOrderedToSit(!this.isOrderedToSit());
                        this.jumping = false;
                        this.navigation.stop();
                        this.setTarget((LivingEntity) null);
                        return InteractionResult.SUCCESS;
                    } else {
                        return interactionresult;
                    }
                }
            } else if (stack.is(ItemTags.SMALL_FLOWERS) && !this.isAngry()) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }

                if (this.random.nextInt(3) == 0 && !ForgeEventFactory.onAnimalTame(this, player)) {
                    this.tame(player);
                    this.navigation.stop();
                    this.setTarget((LivingEntity) null);
                    this.setOrderedToSit(true);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                }

                return InteractionResult.SUCCESS;
            } else {
                return super.mobInteract(player, hand);
            }
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 5) {
            for (int i = 0; i <= 3; ++i) {
                double d0 = this.random.nextGaussian() * 0.02D;
                double d1 = this.random.nextGaussian() * 0.02D;
                double d2 = this.random.nextGaussian() * 0.02D;
                this.level().addParticle(ParticleTypes.SMOKE, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ItemTags.TALL_FLOWERS);
    }

    @Override
    public boolean canMate(@NotNull Animal otherAnimal) {
        if (otherAnimal != this && this.isTame() && otherAnimal instanceof DandeLionEntity lion) {
            return lion.isTame() && !lion.isInSittingPose() && this.isInLove() && lion.isInLove();
        }
        return false;
    }

    public boolean isAngry() {
        return (this.entityData.get(DATA_FLAGS_ID) & 2) != 0;
    }

    public void setAngry(boolean angry) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        this.entityData.set(DATA_FLAGS_ID, angry ? (byte) (b0 | 2) : (byte) (b0 & -3));
    }

    @Override
    public int getAmbientSoundInterval() {
        return super.getAmbientSoundInterval() * 3;
    }

    @Override
    public void setTarget(@Nullable LivingEntity entitylivingbaseIn) {
        super.setTarget(entitylivingbaseIn);
        if (entitylivingbaseIn == null) {
            this.setAngry(false);
        } else if (!this.isTame()) {
            this.setAngry(true);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        this.setOrderedToSit(false);
        return super.hurt(source, amount);
    }

	@Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (this.getTarget() == null && this.isAngry()) {
                this.setAngry(false);
            }
            if (this.isBaby()) return;
            if (shearedTicks > 0 && --shearedTicks == 0) {
                setSheared(false);
            }
        }
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHitIn) {
        super.dropCustomDeathLoot(source, looting, recentlyHitIn);
        spawnAtLocation(new ItemStack(isSheared() ? WonderBlocks.DANDELION_PETALS.get() : WonderBlocks.DANDELION_FLUFF.get(), random.nextInt(2) + 1));
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return isTame() ? SoundEvents.CAT_PURR : SoundEvents.CAT_STRAY_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CAT_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.CAT_HURT;
    }

    @Override
    public float getVoicePitch() {
        return 0.6F;
    }

    public boolean isSheared() {
        return this.entityData.get(SHEARED);
    }

    private void setSheared(boolean sheared) {
        this.entityData.set(SHEARED, sheared);
    }

	public void shear() {
        if (!level().isClientSide) {
            shearedTicks = 12000;
            setSheared(true);
        }
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return !this.isAngry() && super.canBeLeashed(player);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageable) {
        if (ageable instanceof DandeLionEntity) {
            if (((DandeLionEntity) ageable).madeChild) ((DandeLionEntity) ageable).madeChild = false;
            else {
                madeChild = true;
                for (int i = 0; i < random.nextInt(2) + 3; i++) {
                    DandeLionSeedEntity seed = WonderEntityTypes.DANDE_LION_SEED.get().create(level());
                    if (seed != null) {
                        double rotation = Math.toRadians(level().random.nextInt(360));
                        seed.setPos(getX(), getY(), getZ());
                        seed.setTarget(getX() + Math.sin(rotation) * 16.0, getZ() + Math.cos(-rotation) * 16.0);
                        level().addFreshEntity(seed);
                    }
                }

                ServerPlayer serverplayerentity = getLoveCause();
                if (serverplayerentity == null) {
                    serverplayerentity = ((DandeLionEntity) ageable).getLoveCause();
                }

                if (serverplayerentity != null) {
                    serverplayerentity.awardStat(Stats.ANIMALS_BRED);
                    CriteriaTriggers.BRED_ANIMALS.trigger(serverplayerentity, this, (Animal) ageable, null);
                }

                setAge(6000);
                ageable.setAge(6000);
                resetLove();
                ((Animal) ageable).resetLove();

                this.level().broadcastEntityEvent(this, (byte) 18);
                this.level().broadcastEntityEvent(ageable, (byte) 18);
                if (this.level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                    this.level().addFreshEntity(new ExperienceOrb(this.level(), getX(), getY(), getZ(), random.nextInt(7) + 1));
                }
            }
        }
        return null;
    }

    private boolean flowersAround() {
        BlockPos p = blockPosition();
        BlockPos.MutableBlockPos mutable = p.mutable();
        int flowers = 0;
        for (int i = -16; i <= 16; i++) {
            for (int j = -16; j <= 16; j++) {
                mutable.setWithOffset(p, i, 0, j);
                BlockState current = level().getBlockState(mutable);
                BlockState down = level().getBlockState(mutable.move(Direction.DOWN));
                if (!down.canOcclude()) current = down;
                if (current.is(BlockTags.FLOWERS) && ++flowers >= 6) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
        if (target instanceof Creeper || target instanceof Ghast) {
            return false;
        } else {
            if (target instanceof DandeLionEntity lion) {
                return !lion.isTame() || lion.getOwner() != owner;
            } else if (target instanceof Player && owner instanceof Player && !((Player) owner).canHarmPlayer((Player) target)) {
                return false;
            } else if (target instanceof AbstractHorse && ((AbstractHorse) target).isTamed()) {
                return false;
            } else {
                return !(target instanceof TamableAnimal) || !((TamableAnimal) target).isTame();
            }
        }
    }
}
