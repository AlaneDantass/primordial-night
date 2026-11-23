package net.poo.primordialnight.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
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
    private static final int    TELEPORT_COOLDOWN_TICKS       = 100;
    private static final double BEHIND_OFFSET_BLOCKS          = 2.0D;

    private final YmaPytuBossEntity boss;
    private int cooldownTimer = 0;

    public TeleportBehindTargetGoal(YmaPytuBossEntity boss) {
        this.boss = boss;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    /**
     * Verifica a cada tick se o teleporte deve ser ativado.
     * Condições: cooldown zerado, alvo vivo e distância ≥ 12 blocos.
     */
    @Override
    public boolean canUse() {
        if (this.cooldownTimer > 0) {
            --this.cooldownTimer;
            return false;
        }
        LivingEntity target = this.boss.getTarget();
        return target != null
                && target.isAlive()
                && this.boss.distanceToSqr(target) >= MIN_DISTANCE_TO_TELEPORT_SQR;
    }

    /**
     * Executa o teleporte:
     * 1. Calcula posição 2 blocos atrás do alvo.
     * 2. Valida que o chão abaixo é sólido (evita cair no vazio do End).
     * 3. Emite partículas + som de saída, teleporta, emite partículas + som de chegada.
     */
    @Override
    public void start() {
        this.cooldownTimer = TELEPORT_COOLDOWN_TICKS;

        LivingEntity target = this.boss.getTarget();
        if (target == null) return;

        // ── 1. Posição atrás do alvo ─────────────────────────────────────────────
        // getLookAngle() retorna o vetor unitário para onde o alvo está olhando.
        // scale(-BEHIND_OFFSET_BLOCKS) inverte e escala: fica 2 blocos nas costas.
        Vec3 behind = target.getLookAngle().scale(-BEHIND_OFFSET_BLOCKS);
        double destX = target.getX() + behind.x;
        double destY = target.getY();
        double destZ = target.getZ() + behind.z;

        // ── 2. Verificação de chão sólido (proteção contra o vazio do End) ────────
        // Desce até 3 blocos procurando um bloco sólido; se não achar, aborta.
        BlockPos landingPos = BlockPos.containing(destX, destY, destZ);
        boolean groundFound = false;
        for (int i = 0; i <= 3; i++) {
            BlockPos checkPos = landingPos.below(i);
            if (this.boss.level().getBlockState(checkPos).isSolid()) {
                // Ajusta destY para pousar em cima do bloco encontrado
                destY = checkPos.getY() + 1;
                groundFound = true;
                break;
            }
        }
        if (!groundFound) return; // cancela o teleporte se só há vazio abaixo

        // ── 3. Efeitos de saída (posição original do boss) ───────────────────────
        spawnTeleportEffects(this.boss.getX(), this.boss.getY(), this.boss.getZ());

        // ── 4. Teleportar o boss ──────────────────────────────────────────────────
        this.boss.teleportTo(destX, destY, destZ);

        // ── 5. Efeitos de chegada (nova posição) ─────────────────────────────────
        spawnTeleportEffects(destX, destY, destZ);

        // Faz o boss olhar para o alvo imediatamente após o teleporte
        this.boss.getLookControl().setLookAt(target, 30.0F, 30.0F);
    }

    /**
     * Emite partículas e som de teleporte em um dado ponto.
     * Reutilizado nos pontos de saída e chegada (princípio DRY).
     */
    private void spawnTeleportEffects(double x, double y, double z) {
        if (!(this.boss.level() instanceof ServerLevel serverLevel)) return;

        // Partículas de portal (swirl roxo característico)
        serverLevel.sendParticles(ParticleTypes.PORTAL,
                x, y + 1.0, z,
                40, 0.5, 0.8, 0.5, 0.3);

        // Fumaça pesada para reforçar a dissolução/materialização
        serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE,
                x, y + 1.0, z,
                12, 0.4, 0.6, 0.4, 0.02);

        // Som de teleporte do Enderman — temático ao vazio
        serverLevel.playSound(null, x, y, z,
                SoundEvents.ENDERMAN_TELEPORT,
                SoundSource.HOSTILE,
                1.2F, 0.7F);
    }
}
