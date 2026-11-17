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
    /*
    Phase 1 (100% to 50% HP): "The Weight of Silence" (O Peso do Silencio).
     */
    PHASE_ONE(0, 1.0F, false),

    /**
    Estados PHASE_ONE, ENRAGED e VULNERABLE declarados e associados aos seus identificadores, multiplicadores e gatilhos de céu.
    Encapsulamento correto com campos private final.
    Getters públicos para expor os dados sem permitir mutação externa indevida.
    Método estático de conversão fromId(int id).
     */
    ENRAGED(1, 1.35F, true),

    VULNERABLE(2, 0.75F, true);

    private final int phaseId;
    private final float speedMultiplier;
    private final boolean shouldDarkenSky;

    //Construtor do ENUM
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
    // !Poderíamos usar uma exceção para argumento inválido aqui, para caso seja passado um id errado para  a Phase.Na classe YmaPytuBossEntity, nós chamamos 
    // !a fase do boss e o identificador da fase é o this.entityData.get(DATA_PHASE). onde o entityData, placa de rede do mine que envia o id da mudança de fase do 
    // !sevidor para o Cliente (tela do jogador) e guarda o idPhase em entityData. Na renderização o dev 3 projeta
    // !o que vai aparecer na tela.
    // !Caso haja alguma falha de comunicação entre essas classes que dependem da placa de rede do mine, seja por uma dessincronização entre versões do mod, ou corrupções
    // !de pacotes, precisamos lidar com as falhas de forma a não interromper o jogo.

    // !COMO???
    /*podemos manter o boss sempre na fase 1, que já é padrão e não vai exigir essa mudança entre cliente e servidor, então a vida do boos continua caindo 
    até zerar, e o fluxo de batalha fica bem mais simples. ao final da batalha pode aparecer uma notificação ao jogador sobre a falha */
}
