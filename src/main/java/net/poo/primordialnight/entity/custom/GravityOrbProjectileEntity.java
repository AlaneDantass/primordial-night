package net.poo.primordialnight.entity.custom;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Zero-Gravity Orb Projectile fired by Yma-Pytu during Phase 2 (BossPhase.ENRAGED).
 * Causes short Levitation and Darkness on impact to disorient the player in the arena.
 *
 * Responsible: Dev 2
 * OOP Concepts:
 * - Inheritance: extends AbstractHurtingProjectile
 * - Polymorphism: @Override onHitEntity(), onHit(), isPickable()
 *
 * Reference Repositories:
 * - mirror-abyss-main (VoidBolt.java -> extends AbstractHurtingProjectile, applies debuff onHitEntity)
 * - AlexsMobs-1.20 (EntityVoidWormShot.java -> homing & impact mechanics)
 */
public class GravityOrbProjectileEntity extends AbstractHurtingProjectile {

    private static final float ORB_DAMAGE = 6.0F;
    private static final int LEVITATION_DURATION_TICKS = 50;
    private static final int DARKNESS_DURATION_TICKS = 80;

    public GravityOrbProjectileEntity(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public GravityOrbProjectileEntity(EntityType<? extends AbstractHurtingProjectile> entityType,
                                      LivingEntity shooter, double offsetX, double offsetY, double offsetZ, Level level) {
        super(entityType, shooter, offsetX, offsetY, offsetZ, level);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        if (this.level().isClientSide()) {
            return;
        }

        // TODO (Dev 2):
        // 1. Check if hitResult.getEntity() is a LivingEntity target and is NOT the owner (YmaPytuBossEntity).
        // 2. Apply damage using ORB_DAMAGE.
        // 3. Apply MobEffects.LEVITATION (LEVITATION_DURATION_TICKS) and MobEffects.DARKNESS (DARKNESS_DURATION_TICKS).
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide()) {
            this.discard();
        }
    }

    @Override
    public boolean isPickable() {
        return false;
    }
}
