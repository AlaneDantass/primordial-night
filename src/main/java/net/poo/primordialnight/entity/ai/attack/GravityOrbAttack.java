package net.poo.primordialnight.entity.ai.attack;

import net.minecraft.world.entity.LivingEntity;
import net.poo.primordialnight.entity.custom.YmaPytuBossEntity;
import net.poo.primordialnight.entity.enums.BossPhase;

/**
 * Phase 2 Attack: Zero-Gravity Orbs ("Esferas de Gravidade Zero").
 * Unlocked only when Yma-Pytu enters BossPhase.ENRAGED (< 50% HP).
 * Fires GravityOrbProjectileEntity towards the target, inflicting short Levitation and Darkness.
 *
 * Responsible: Dev 2
 * OOP Concepts:
 * - Inheritance: extends AbstractBossAttack
 * - Polymorphism: @Override executeAttack()
 *
 * Reference Repositories:
 * - mirror-abyss-main (MirrorEater.java & VoidBolt.java)
 * - AlexsMobs-1.20 (EntityVoidWormShot.java)
 */
public class GravityOrbAttack extends AbstractBossAttack {

    private static final int ORB_ATTACK_COOLDOWN_TICKS = 50;
    private static final float ORB_BASE_DAMAGE = 6.0F;

    public GravityOrbAttack() {
        super(ORB_ATTACK_COOLDOWN_TICKS, ORB_BASE_DAMAGE, BossPhase.ENRAGED);
    }

    @Override
    protected void executeAttack(YmaPytuBossEntity boss, LivingEntity target) {
        // TODO (Dev 2):
        // 1. Calculate direction vector (dx, dy, dz) from boss eye position to target center.
        // 2. Instantiate GravityOrbProjectileEntity using ModEntities.GRAVITY_ORB_PROJECTILE.get().
        // 3. Spawn the projectile in boss.level().addFreshEntity(orb) and play an Enderman/Beacon sound.
    }
}
