package net.poo.primordialnight.entity.enums;

/**
 * Typed Enum representing the combat state machine of Yma-Pytu (The First Night).
 * Eliminates magic numbers when checking boss health thresholds and phase transitions.
 *
 * Responsible: Dev 1
 * OOP Concepts: Typed Enums with encapsulated attributes and methods.
 * Reference:
 * - Yma-Pytu, A Primeira Noite.pdf (Section 2.D)
 * - Plano_de_Trabalho_Boss_POO.pdf (Section 3 - Enums Tipados)
 * - GUIA_BOAS_PRATICAS_ROMHENRI.md (Section 5.7 - Enums inside enums/ package)
 */
public enum BossPhase {
    /**
     * Phase 1 (100% to 50% HP): "The Weight of Silence" (O Peso do Silencio).
     * Uses VoidSlamAttack and TeleportBehindTargetGoal.
     */
    PHASE_ONE(0, 1.0F, false),

    /**
     * Phase 2 (< 50% HP): "The Primordial Collapse" (O Colapso Primordial).
     * Chest core opens in violet flames, eyes change color, sky darkens (setDarkenScreen(true)),
     * +35% movement speed multiplier (1.35F), and unlocks GravityOrbAttack.
     */
    ENRAGED(1, 1.35F, true),

    /**
     * Optional brief recovery state after an ultimate attack.
     */
    VULNERABLE(2, 0.75F, true);

    private final int phaseId;
    private final float speedMultiplier;
    private final boolean shouldDarkenSky;

    BossPhase(int phaseId, float speedMultiplier, boolean shouldDarkenSky) {
        this.phaseId = phaseId;
        this.speedMultiplier = speedMultiplier;
        this.shouldDarkenSky = shouldDarkenSky;
    }

    public int getPhaseId() {
        return this.phaseId;
    }

    public float getSpeedMultiplier() {
        return this.speedMultiplier;
    }

    public boolean shouldDarkenSky() {
        return this.shouldDarkenSky;
    }

    public static BossPhase fromId(int id) {
        for (BossPhase phase : values()) {
            if (phase.phaseId == id) {
                return phase;
            }
        }
        return PHASE_ONE;
    }
}
