package net.poo.primordialnight.entity.ai.attack;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.poo.primordialnight.entity.custom.YmaPytuBossEntity;
import net.poo.primordialnight.entity.enums.BossPhase;

import java.util.List;

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
        // ── 1. Buscar todas as LivingEntity dentro do raio do shockwave ──────────
        List<LivingEntity> nearbyEntities = boss.level().getEntitiesOfClass(
                LivingEntity.class,
                boss.getBoundingBox().inflate(SHOCKWAVE_RADIUS)
        );

        // ── 2 & 3. Filtrar, causar dano e aplicar knock-up ───────────────────────
        for (LivingEntity entity : nearbyEntities) {
            // Ignora o próprio boss
            if (entity == boss) continue;

            // Causa dano via damageSources().mobAttack()
            entity.hurt(boss.damageSources().mobAttack(boss), this.getBaseDamage());

            // Aplica impulso vertical (knock-up) sem alterar X e Z
            entity.setDeltaMovement(
                    entity.getDeltaMovement().add(0, VERTICAL_KNOCKUP_VELOCITY, 0)
            );
            // Notifica o cliente que o vetor de movimento mudou
            entity.hurtMarked = true;
        }

        // ── 4. Efeitos visuais e sonoros (apenas no servidor) ────────────────────
        if (boss.level() instanceof ServerLevel serverLevel) {
            double bossX = boss.getX();
            double bossY = boss.getY();       // pé do boss (nível do chão)
            double bossZ = boss.getZ();

            // Partículas de SQUID_INK em anel ao redor do ponto de impacto
            // Itera 360° em passos de 15° para formar o anel de shockwave
            for (int angleDeg = 0; angleDeg < 360; angleDeg += 15) {
                double angleRad = Math.toRadians(angleDeg);
                double offsetX = Math.cos(angleRad) * SHOCKWAVE_RADIUS * 0.5;
                double offsetZ = Math.sin(angleRad) * SHOCKWAVE_RADIUS * 0.5;

                // Anel interno de SQUID_INK (escuro, representa o vazio)
                serverLevel.sendParticles(
                        ParticleTypes.SQUID_INK,
                        bossX + offsetX, bossY + 0.1, bossZ + offsetZ,
                        3,              // quantidade por ponto
                        0.1, 0.0, 0.1, // spread X, Y, Z
                        0.05            // velocidade extra
                );

                // Anel externo de DRAGON_BREATH (roxo, representa a energia do vazio)
                double outerOffsetX = Math.cos(angleRad) * SHOCKWAVE_RADIUS;
                double outerOffsetZ = Math.sin(angleRad) * SHOCKWAVE_RADIUS;
                serverLevel.sendParticles(
                        ParticleTypes.DRAGON_BREATH,
                        bossX + outerOffsetX, bossY + 0.2, bossZ + outerOffsetZ,
                        2,
                        0.15, 0.1, 0.15,
                        0.02
                );
            }

            // Som de impacto: explosão abafada (GENERIC_EXPLODE) no ponto do slam
            serverLevel.playSound(
                    null,                       // null = toca para todos os jogadores próximos
                    bossX, bossY, bossZ,
                    SoundEvents.GENERIC_EXPLODE,
                    SoundSource.HOSTILE,
                    1.5F,                       // volume
                    0.6F                        // pitch (grave, impacto pesado)
            );

            // Som secundário: WARDEN_SONIC_BOOM dá a sensação de onda de choque vinda do vazio
            serverLevel.playSound(
                    null,
                    bossX, bossY, bossZ,
                    SoundEvents.WARDEN_SONIC_BOOM,
                    SoundSource.HOSTILE,
                    0.8F,
                    1.2F
            );
        }
    }
}
