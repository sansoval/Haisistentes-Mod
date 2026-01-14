package net.mcreator.haisistente.entity;

import javax.annotation.Nullable;
import net.minecraftforge.network.PlayMessages;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.Difficulty;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffects;

import net.mcreator.haisistente.HaisistenteEntities;
import net.mcreator.haisistente.HaisistenteItems;
import net.mcreator.haisistente.HaisistenteMod;

public class HaisistenteZombie extends HaisistenteAbstract {

	public HaisistenteZombie(PlayMessages.SpawnEntity packet, Level world) {
		this(HaisistenteEntities.HAISISTENTE_ZOMBIE.get(), world);
	}

	public HaisistenteZombie(EntityType<? extends HaisistenteAbstract> type, Level world) {
		super(type, world);
	}

	@Override
	public String getTexture() {
		return "haisezombie_texture";
	}
	
	@Override
	public String getModel() {
		return "geo/ropa_zombie.geo.json";
	}
	
	@Override
	public String getGeoAnimation() {
		return "animations/ropa_zombie.animation.json";
	}

	@Override
	public boolean canEat() {
		return super.canEat() && hasEffect(MobEffects.WEAKNESS);
	}
	
	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.getAvailableGoals().removeIf(
    		g -> g.getGoal() instanceof PanicGoal
		);
		this.goalSelector.getAvailableGoals().removeIf(
    		g -> g.getGoal() instanceof HaisistenteAbstract.HaiseSleepOnOwnerGoal
		);
		this.goalSelector.getAvailableGoals().removeIf(
    		g -> g.getGoal() instanceof HaisistenteAbstract.HaiseDanceGoal
		);
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(HaisistenteZombie.class));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, HaisistenteAbstract.class, true));
	}
	
	@Override
	public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageable) {
		HaisistenteZombie retval = HaisistenteEntities.HAISISTENTE_ZOMBIE.get().create(serverWorld);
		retval.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(retval.blockPosition()), MobSpawnType.BREEDING, null, null);
		return retval;
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEAD;
	}

	protected boolean isSunSensitive() {
      return true;
   	}
   	
   	public void tryTame() {
   	}

   	public void aiStep() {
    	if (this.isAlive()) {
      		boolean flag = this.isSunSensitive() && this.isSunBurnTick();
            if (flag) this.setSecondsOnFire(8);
         }

      	super.aiStep();
   }

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3D);
		builder = builder.add(Attributes.MAX_HEALTH, 15.0D);
		builder = builder.add(Attributes.ARMOR, 0.0D);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3.0D);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16.0D);
		return builder;
	}

	public static void init() {
		SpawnPlacements.register(HaisistenteEntities.HAISISTENTE_ZOMBIE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				(entityType, world, reason, pos, random) -> (world.getDifficulty() != Difficulty.PEACEFUL && Mob.checkMobSpawnRules(entityType, world, reason, pos, random)));
	}
}

