package net.poo.primordialnight.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.poo.primordialnight.entity.custom.YmaPytuBossEntity;

import java.util.EnumSet;

/**
 * Shadow Step AI Goal ("Passo nas Sombras").
 * Because Yma-Pytu is the living darkness of The End, if the target player runs too far away,
 * the Boss dissolves into shadow particles and reappears directly behind the target.
 *
 * Responsible: Dev 2
 * OOP Concepts:
 * - Inheritance: extends Goal (Minecraft AI base class)
 * - Polymorphism: @Override canUse(), start()
 *
 * Reference Repository:
 * - Bosses-of-Mass-Destruction-FORGE-master (entity/custom/lich/TeleportAction.java)
 */
public class TeleportBehindTargetGoal extends Goal {

    private static final double MIN_DISTANCE_TO_TELEPORT_SQR = 144.0D; // 12 blocks squared
    private static final int TELEPORT_COOLDOWN_TICKS = 100;

    private final YmaPytuBossEntity boss;
    private int cooldownTimer = 0;

    public TeleportBehindTargetGoal(YmaPytuBossEntity boss) {
        this.boss = boss;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.cooldownTimer > 0) {
            --this.cooldownTimer;
            return false;
        }
        LivingEntity target = this.boss.getTarget();
        return target != null && target.isAlive() && this.boss.distanceToSqr(target) >= MIN_DISTANCE_TO_TELEPORT_SQR;
    }

    @Override
    public void start() {
        this.cooldownTimer = TELEPORT_COOLDOWN_TICKS;
        // TODO (Dev 2):
        // 1. Compute the position 2 blocks behind the target's look angle (target.getLookAngle().scale(-2.0)).
        // 2. Verify that the ground below that position is solid (so the Boss never teleports into the End void!).
        // 3. Spawn ParticleTypes.PORTAL / ParticleTypes.LARGE_SMOKE, play SoundEvents.ENDERMAN_TELEPORT,
        //    and move the boss with boss.teleportTo(destX, destY, destZ).
    }
}
