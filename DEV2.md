# 🧠 Dev 2 — Sistema de IA de Combate do Yma-Pytu

> **Projeto:** Yma-Pytu: The First Night — Mod Minecraft 1.20.1 (Forge)  
> **Disciplina:** Programação Orientada a Objetos  
> **Equipe:** Alane, Danilo, Arthur, Kalebe

---

## 📋 Visão Geral da Responsabilidade

O **Dev 2** é responsável por toda a **inteligência artificial de combate** do boss Yma-Pytu. Isso engloba o sistema de ataques, os goals de comportamento de combate e a entidade do projétil de orbe gravitacional.

---

## 📂 Arquivos sob Responsabilidade do Dev 2

| Arquivo | Pacote | Descrição |
|---|---|---|
| [`BossAttackAction.java`](file:///home/kalebe/Documentos/Faculdade/Projetos/POO/Boss_Minecraft/primordial-night/src/main/java/net/poo/primordialnight/entity/ai/attack/BossAttackAction.java) | `entity.ai.attack` | Interface Strategy Pattern para ações ofensivas |
| [`AbstractBossAttack.java`](file:///home/kalebe/Documentos/Faculdade/Projetos/POO/Boss_Minecraft/primordial-night/src/main/java/net/poo/primordialnight/entity/ai/attack/AbstractBossAttack.java) | `entity.ai.attack` | Classe abstrata base com Template Method Pattern |
| [`VoidSlamAttack.java`](file:///home/kalebe/Documentos/Faculdade/Projetos/POO/Boss_Minecraft/primordial-night/src/main/java/net/poo/primordialnight/entity/ai/attack/VoidSlamAttack.java) | `entity.ai.attack` | Ataque Pisão Abissal (AoE, Fase 1+) |
| [`GravityOrbAttack.java`](file:///home/kalebe/Documentos/Faculdade/Projetos/POO/Boss_Minecraft/primordial-night/src/main/java/net/poo/primordialnight/entity/ai/attack/GravityOrbAttack.java) | `entity.ai.attack` | Ataque Esferas de Gravidade Zero (exclusivo Fase 2) |
| [`YmaPytuAttackGoal.java`](file:///home/kalebe/Documentos/Faculdade/Projetos/POO/Boss_Minecraft/primordial-night/src/main/java/net/poo/primordialnight/entity/ai/goal/YmaPytuAttackGoal.java) | `entity.ai.goal` | Goal principal de combate corpo-a-corpo e ataques especiais |
| [`TeleportBehindTargetGoal.java`](file:///home/kalebe/Documentos/Faculdade/Projetos/POO/Boss_Minecraft/primordial-night/src/main/java/net/poo/primordialnight/entity/ai/goal/TeleportBehindTargetGoal.java) | `entity.ai.goal` | Goal de teletransporte "Passo nas Sombras" |
| [`GravityOrbProjectileEntity.java`](file:///home/kalebe/Documentos/Faculdade/Projetos/POO/Boss_Minecraft/primordial-night/src/main/java/net/poo/primordialnight/entity/custom/GravityOrbProjectileEntity.java) | `entity.custom` | Projétil do ataque de Fase 2 (Levitação + Escuridão) |

---

## 🏗️ Arquitetura — Diagrama de Classes

```
         «interface»
       BossAttackAction
    ┌──────────────────┐
    │ + canUseAttack() │
    │ + performAttack()│
    └────────┬─────────┘
             │ implements
    ┌─────────▼──────────┐
    │  AbstractBossAttack │  ← Template Method Pattern
    │ ─────────────────── │
    │ - cooldownTicks     │
    │ - baseDamage        │
    │ - minimumRequiredPhase │
    │ ─────────────────── │
    │ + canUseAttack()    │
    │ + performAttack()   │  ← final (chama executeAttack)
    │ # executeAttack()   │  ← abstract
    └────────┬────────────┘
             │ extends
   ┌─────────┴─────────┐
   │                   │
┌──▼─────────┐  ┌──────▼──────────┐
│VoidSlamAttack│  │GravityOrbAttack│
│ ──────────── │  │ ────────────── │
│ cooldown: 60t│  │ cooldown: 50t  │
│ damage: 14.0 │  │ damage: 6.0    │
│ fase: PHASE_1│  │ fase: ENRAGED  │
│ radius: 5.0  │  │                │
└──────────────┘  └────────────────┘

YmaPytuAttackGoal ──uses──► List<BossAttackAction>  (Strategy Pattern)
TeleportBehindTargetGoal ──controls──► YmaPytuBossEntity
GravityOrbProjectileEntity ──fired by──► GravityOrbAttack
```

---

## 🎯 Detalhes de Cada Componente

### 1. `BossAttackAction` — Interface (Strategy Pattern)

Define o contrato que toda ação ofensiva do boss deve cumprir:

```java
boolean canUseAttack(YmaPytuBossEntity boss, LivingEntity target);
int performAttack(YmaPytuBossEntity boss, LivingEntity target);
```

**Conceitos POO aplicados:** Interface e Strategy Design Pattern.

---

### 2. `AbstractBossAttack` — Classe Abstrata (Template Method)

Centraliza os parâmetros comuns (cooldown, dano, fase mínima) e garante que `performAttack()` sempre chame `executeAttack()` internamente.

| Atributo | Tipo | Descrição |
|---|---|---|
| `cooldownTicks` | `int` | Tempo de recarga após usar o ataque |
| `baseDamage` | `float` | Dano base da habilidade |
| `minimumRequiredPhase` | `BossPhase` | Fase mínima para liberar o ataque |

**Conceitos POO aplicados:**
- **Abstração** — classe abstrata com método abstrato `executeAttack()`
- **Encapsulamento** — atributos `private final` inicializados via construtor `protected`
- **Template Method** — `performAttack()` valida e delega para `executeAttack()`

---

### 3. `VoidSlamAttack` — Pisão Abissal (Fase 1+)

O boss bate os dois braços de obsidiana no chão gerando uma onda de choque escura com AoE.

| Constante | Valor |
|---|---|
| `SLAM_COOLDOWN_TICKS` | 60 ticks (~3s) |
| `SLAM_BASE_DAMAGE` | 14.0 de dano |
| `SHOCKWAVE_RADIUS` | 5.0 blocos |
| `VERTICAL_KNOCKUP_VELOCITY` | 0.65 (impulso vertical) |

**TODOs a implementar:**
1. Buscar todas as `LivingEntity` em raio `SHOCKWAVE_RADIUS` com `level().getEntitiesOfClass(...)`
2. Aplicar `getBaseDamage()` a cada entidade (exceto o próprio boss)
3. Aplicar knockback vertical (`VERTICAL_KNOCKUP_VELOCITY` no eixo Y)
4. Spawnar partículas `SQUID_INK` / `DRAGON_BREATH` e tocar `WARDEN_SONIC_BOOM`

**Conceitos POO aplicados:** Herança de `AbstractBossAttack`, Polimorfismo via `@Override executeAttack()`.

---

### 4. `GravityOrbAttack` — Esferas de Gravidade Zero (Fase 2 exclusivo)

Lança um `GravityOrbProjectileEntity` em direção ao jogador. Só é desbloqueado quando o boss entra em `BossPhase.ENRAGED` (< 50% de HP).

| Constante | Valor |
|---|---|
| `ORB_ATTACK_COOLDOWN_TICKS` | 50 ticks (~2.5s) |
| `ORB_BASE_DAMAGE` | 6.0 de dano |
| Fase mínima | `BossPhase.ENRAGED` |

**TODOs a implementar:**
1. Calcular vetor de direção (`dx, dy, dz`) da posição dos olhos do boss ao centro do alvo
2. Instanciar `GravityOrbProjectileEntity` usando `ModEntities.GRAVITY_ORB_PROJECTILE.get()`
3. Spawnar o projétil com `boss.level().addFreshEntity(orb)` e tocar som de Enderman/Beacon

**Conceitos POO aplicados:** Herança e Polimorfismo.

---

### 5. `GravityOrbProjectileEntity` — Projétil do Orbe

Projétil disparado pelo boss na Fase 2. Ao acertar o jogador, causa **Levitação** e **Escuridão** para desorientá-lo na arena.

| Constante | Valor |
|---|---|
| `ORB_DAMAGE` | 6.0 |
| `LEVITATION_DURATION_TICKS` | 50 ticks (~2.5s) |
| `DARKNESS_DURATION_TICKS` | 80 ticks (~4s) |

**TODOs a implementar (em `onHitEntity`):**
1. Verificar se a entidade atingida é uma `LivingEntity` e **não** é o próprio boss (owner)
2. Aplicar `ORB_DAMAGE` ao alvo
3. Aplicar `MobEffects.LEVITATION` e `MobEffects.DARKNESS` com suas respectivas durações

**Conceitos POO aplicados:** Herança de `AbstractHurtingProjectile`, Polimorfismo via `@Override onHitEntity()`, `onHit()`, `isPickable()`.

---

### 6. `YmaPytuAttackGoal` — Goal Principal de Combate

Estende `MeleeAttackGoal` do Minecraft e gerencia tanto o ataque corpo-a-corpo quanto a seleção dinâmica dos ataques especiais via Strategy Pattern.

```java
// Lista de estratégias registradas:
this.attackStrategies = List.of(
    new VoidSlamAttack(),
    new GravityOrbAttack()
);
```

**TODO a implementar (em `tick()`):**
- Iterar sobre `attackStrategies`
- Selecionar uma `BossAttackAction` onde `canUseAttack()` retorne `true`
- Chamar `performAttack(bossEntity, target)` e atribuir o cooldown retornado a `specialAttackCooldownTicks`

**Conceitos POO aplicados:** Herança (`MeleeAttackGoal`), Polimorfismo e Strategy Pattern via `List<BossAttackAction>`.

---

### 7. `TeleportBehindTargetGoal` — Passo nas Sombras

Como Yma-Pytu é a escuridão viva do End, se o jogador fugir a mais de **12 blocos** de distância (144 blocos²), o boss se dissolve em partículas de sombra e reaparece **atrás** do alvo.

| Constante | Valor |
|---|---|
| `MIN_DISTANCE_TO_TELEPORT_SQR` | 144.0 (12 blocos) |
| `TELEPORT_COOLDOWN_TICKS` | 100 ticks (~5s) |

**TODOs a implementar (em `start()`):**
1. Calcular posição 2 blocos atrás do ângulo de visão do alvo: `target.getLookAngle().scale(-2.0)`
2. Verificar se o chão abaixo dessa posição é sólido (evitar teleporte para o vazio do End!)
3. Spawnar partículas `PORTAL` / `LARGE_SMOKE`, tocar `SoundEvents.ENDERMAN_TELEPORT`
4. Mover o boss com `boss.teleportTo(destX, destY, destZ)`

**Conceitos POO aplicados:** Herança (`Goal` do Minecraft), Polimorfismo via `@Override canUse()` e `start()`.

---

## 🎓 Conceitos de POO Cobertos pelo Dev 2

| Conceito | Onde aparece |
|---|---|
| **Interface** | `BossAttackAction` |
| **Classe Abstrata** | `AbstractBossAttack` |
| **Herança** | Todas as classes concretas estendem suas bases |
| **Polimorfismo** | `executeAttack()`, `canUseAttack()`, `onHitEntity()`, `canUse()`, `start()` |
| **Encapsulamento** | Atributos `private final` em `AbstractBossAttack` |
| **Template Method Pattern** | `performAttack()` → `executeAttack()` |
| **Strategy Pattern** | `List<BossAttackAction>` em `YmaPytuAttackGoal` |

---

## ✅ Checklist de Implementação

- [x] `BossAttackAction` interface criada
- [x] `AbstractBossAttack` com Template Method implementado
- [x] `VoidSlamAttack` estrutura criada (TODO: lógica de AoE)
- [x] `GravityOrbAttack` estrutura criada (TODO: spawn do projétil)
- [x] `GravityOrbProjectileEntity` estrutura criada (TODO: `onHitEntity`)
- [x] `YmaPytuAttackGoal` estrutura criada (TODO: seleção do ataque no `tick()`)
- [x] `TeleportBehindTargetGoal` estrutura criada (TODO: lógica de teletransporte)
- [ ] Implementar lógica AoE de `VoidSlamAttack.executeAttack()`
- [ ] Implementar spawn do projétil em `GravityOrbAttack.executeAttack()`
- [ ] Implementar debuffs em `GravityOrbProjectileEntity.onHitEntity()`
- [ ] Implementar seleção do Strategy em `YmaPytuAttackGoal.tick()`
- [ ] Implementar teletransporte em `TeleportBehindTargetGoal.start()`

---

## 🔗 Referências dos Repositórios Base

| Repositório | Arquivos consultados |
|---|---|
| `Bosses-of-Mass-Destruction-FORGE-master` | `IAction.java`, `WaveAction.java`, `ObsidilithMoveLogic.java`, `TeleportAction.java` |
| `mirror-abyss-main` | `VoidBolt.java`, `MirrorEater.java` |
| `AlexsMobs-1.20` | `EntityVoidWormShot.java` |
| `silver-samurai-boss-main` | `StompCombat.java` |
| `Forge-Tutorial-1.20.X-29-attackAnimation` | `RhinoAttackGoal.java` |
