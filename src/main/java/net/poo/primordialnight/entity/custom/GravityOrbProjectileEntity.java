package net.poo.primordialnight.entity.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
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

    /**
     * Chamado quando o orbe acerta uma entidade.
     * Aplica dano, Levitação e Escuridão ao alvo (se não for o próprio boss).
     */
    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);

        // Lógica de impacto só roda no servidor
        if (this.level().isClientSide()) {
            return;
        }

        Entity hitEntity = hitResult.getEntity();

        // ── 1. Validar: alvo deve ser uma LivingEntity e não o próprio dono (boss) ──
        if (!(hitEntity instanceof LivingEntity target)) {
            return;
        }
        if (hitEntity == this.getOwner()) {
            return;
        }

        // ── 2. Aplicar dano de impacto ───────────────────────────────────────────
        target.hurt(this.damageSources().thrown(this, this.getOwner()), ORB_DAMAGE);

        // ── 3. Aplicar efeitos de status: Levitação e Escuridão ──────────────────
        // amplifier=0 significa "nível 1" (a API usa base-0 internamente)
        target.addEffect(new MobEffectInstance(MobEffects.LEVITATION, LEVITATION_DURATION_TICKS, 0, false, true));
        target.addEffect(new MobEffectInstance(MobEffects.DARKNESS,   DARKNESS_DURATION_TICKS,  0, false, true));
    }

    /**
     * Chamado em qualquer tipo de colisão (entidade ou bloco).
     * Emite partículas de impacto e descarta o projétil.
     */
    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);

        // ── Efeitos visuais e sonoros no impacto (apenas servidor) ───────────────
        if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel) {
            // Burst de DRAGON_BREATH + SQUID_INK no ponto de impacto
            serverLevel.sendParticles(ParticleTypes.DRAGON_BREATH,
                    this.getX(), this.getY(), this.getZ(),
                    20, 0.3, 0.3, 0.3, 0.05);

            serverLevel.sendParticles(ParticleTypes.SQUID_INK,
                    this.getX(), this.getY(), this.getZ(),
                    8, 0.2, 0.2, 0.2, 0.02);

            // Som de impacto: batida do Enderman (temático ao vazio)
            serverLevel.playSound(null,
                    this.getX(), this.getY(), this.getZ(),
                    SoundEvents.ENDERMAN_HURT,
                    SoundSource.HOSTILE,
                    1.0F, 0.8F);

            // Descartar o projétil após o impacto
            this.discard();
        }
    }

    /**
     * Partículas emitidas a cada tick enquanto o orbe está em voo.
     * Sobrescreve o comportamento padrão do AbstractHurtingProjectile
     * (que emitiria chamas) para usar partículas temáticas do vazio.
     */
    @Override
    public void tick() {
        super.tick();

        // Trail de partículas visível para todos (cliente e servidor)
        if (this.level().isClientSide()) {
            this.level().addParticle(ParticleTypes.DRAGON_BREATH,
                    this.getX(), this.getY(), this.getZ(),
                    0.0, 0.0, 0.0);
        }
    }

    /**
     * O orbe não pode ser "pego" pelo jogador como um item.
     * Necessário para AbstractHurtingProjectile não registrar colisões indesejadas.
     */
    @Override
    public boolean isPickable() {
        return false;
    }
}
