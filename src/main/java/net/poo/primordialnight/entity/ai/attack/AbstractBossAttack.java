package net.poo.primordialnight.entity.ai.attack;

import net.minecraft.world.entity.LivingEntity;
import net.poo.primordialnight.entity.custom.YmaPytuBossEntity;
import net.poo.primordialnight.entity.enums.BossPhase;

/**
 * Abstract base class for all Yma-Pytu boss attacks (Template Method + Strategy Pattern).
 * Isolates common attack parameters (cooldownTicks, baseDamage, requiredPhase) from specific
 * attack implementations.
 *
 * Responsible: Dev 2
 * OOP Concepts:
 * - Abstraction: abstract class with abstract method executeAttack()
 * - Encapsulation: private final attributes initialized via protected constructor
 * - Template Method Pattern: performAttack() validates and delegates to executeAttack()
 *
 * Reference:
 * - Yma-Pytu, A Primeira Noite.pdf (Section 2.D)
 * - Plano_de_Trabalho_Boss_POO.pdf (Section 3 - Abstracao & Polimorfismo)
 * - GUIA_BOAS_PRATICAS_ROMHENRI.md (Section 10 #1 - Template Method in Abstract class)
 */
public abstract class AbstractBossAttack implements BossAttackAction {

    private final int cooldownTicks;
    private final float baseDamage;
    private final BossPhase minimumRequiredPhase;

    protected AbstractBossAttack(int cooldownTicks, float baseDamage, BossPhase minimumRequiredPhase) {
        this.cooldownTicks = cooldownTicks;
        this.baseDamage = baseDamage;
        this.minimumRequiredPhase = minimumRequiredPhase;
    }

    @Override
    public boolean canUseAttack(YmaPytuBossEntity boss, LivingEntity target) {
        if (target == null || !target.isAlive()) {
            return false;
        }
        return boss.getBossPhase().getPhaseId() >= this.minimumRequiredPhase.getPhaseId();
    }

    @Override
    public final int performAttack(YmaPytuBossEntity boss, LivingEntity target) {
        this.executeAttack(boss, target);
        return this.cooldownTicks;
    }

    /**
     * Polymorphic method implemented by each concrete attack subclass (VoidSlamAttack, GravityOrbAttack).
     */
    protected abstract void executeAttack(YmaPytuBossEntity boss, LivingEntity target);

    public int getCooldownTicks() {
        return this.cooldownTicks;
    }

    public float getBaseDamage() {
        return this.baseDamage;
    }

    public BossPhase getMinimumRequiredPhase() {
        return this.minimumRequiredPhase;
    }
}
