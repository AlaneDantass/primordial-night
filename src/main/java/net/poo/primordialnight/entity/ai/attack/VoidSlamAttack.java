package net.poo.primordialnight.entity.ai.attack;

import net.minecraft.world.entity.LivingEntity;
import net.poo.primordialnight.entity.custom.YmaPytuBossEntity;
import net.poo.primordialnight.entity.enums.BossPhase;

/**
 * Phase 1+ Attack: Abyssal Slam ("Pisao Abissal").
 * Yma-Pytu slams the ground with both obsidian arms, generating a dark shockwave
 * that deals Area-of-Effect (AoE) damage and knocks nearby players upward.
 *
 * Responsible: Dev 2
 * OOP Concepts:
 * - Inheritance: extends AbstractBossAttack
 * - Polymorphism: @Override executeAttack()
 *
 * Reference Repositories:
 * - Bosses-of-Mass-Destruction-FORGE-master (WaveAction.java & BurstAction.java)
 * - silver-samurai-boss-main (StompCombat.java -> AoE radius and vertical knock-up vector)
 */
public class VoidSlamAttack extends AbstractBossAttack {

    private static final int SLAM_COOLDOWN_TICKS = 60;
    private static final float SLAM_BASE_DAMAGE = 14.0F;
    private static final double SHOCKWAVE_RADIUS = 5.0D;
    private static final double VERTICAL_KNOCKUP_VELOCITY = 0.65D;

    public VoidSlamAttack() {
        super(SLAM_COOLDOWN_TICKS, SLAM_BASE_DAMAGE, BossPhase.PHASE_ONE);
    }

    @Override
    protected void executeAttack(YmaPytuBossEntity boss, LivingEntity target) {
        // TODO (Dev 2):
        // 1. Query all LivingEntity targets within SHOCKWAVE_RADIUS using boss.level().getEntitiesOfClass(...).
        // 2. Deal this.getBaseDamage() to each valid entity (excluding the boss itself).
        // 3. Apply upward knockback velocity (VERTICAL_KNOCKUP_VELOCITY on Y axis).
        // 4. Spawn a ring of ParticleTypes.SQUID_INK / ParticleTypes.DRAGON_BREATH via ServerLevel.sendParticles()
        //    and play SoundEvents.GENERIC_EXPLODE / SoundEvents.WARDEN_SONIC_BOOM.
    }
}
