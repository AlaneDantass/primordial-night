package net.poo.primordialnight.entity.ai.attack;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.poo.primordialnight.entity.ModEntities;
import net.poo.primordialnight.entity.custom.GravityOrbProjectileEntity;
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
        // ── 1. Calcular vetor de direção do olho do boss ao centro do alvo ────────
        // getEyeY() = altura dos olhos do boss (ponto de disparo)
        // target.getY() + target.getBbHeight() / 2.0 = centro vertical do alvo
        double dx = target.getX()  - boss.getX();
        double dy = (target.getY() + target.getBbHeight() / 2.0) - boss.getEyeY();
        double dz = target.getZ()  - boss.getZ();

        // ── 2. Instanciar o projétil apontando na direção calculada ───────────────
        // O construtor posiciona o orbe na altura dos olhos do boss
        // e usa (dx, dy, dz) como vetor de movimento inicial
        GravityOrbProjectileEntity orb = new GravityOrbProjectileEntity(
                ModEntities.GRAVITY_ORB_PROJECTILE.get(),
                boss, dx, dy, dz,
                boss.level()
        );

        // ── 3. Spawnar no mundo e tocar som de disparo ────────────────────────────
        boss.level().addFreshEntity(orb);

        // Som de disparo: Enderman teleport (temático ao vazio), pitch grave
        boss.level().playSound(
                null,
                boss.getX(), boss.getEyeY(), boss.getZ(),
                SoundEvents.ENDERMAN_TELEPORT,
                SoundSource.HOSTILE,
                1.0F,
                0.5F
        );
    }
}
