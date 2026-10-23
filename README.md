# Ymã-Pytũ: The First Night v1.0.0
> Before the sun (*Kuaray*) or the earth existed, everything was **Ymã-Pytũ**: the First Night, the silent primordial darkness now petrified in the floating islands of The End.

<hr>

- Light mod with easy installation (Minecraft 1.20.1 — Forge 47.3.5);
- Authentic Tupi-Guarani cosmogony lore integrated into The End;
- Built with clean Object-Oriented Programming (Inheritance, Polymorphism, Abstraction, Encapsulation, Typed Enums, and Strategy Pattern).

---

### Docs n Stats

| Feature / Stat | Value / Description |
| --- | --- |
| **Boss Name** | `Ymã-Pytũ, The First Night` (`YmaPytuBossEntity`) |
| **Dimension** | The End (`Level.END`) |
| **Max Health / Armor** | `300 HP` (150 Hearts) / `10 Armor` / `100% Knockback Resistance` |
| **Hitbox / Model** | `1.8 × 3.8 blocks` (Titanic Void Golem in Blockbench) |
| **Wayfinding Item** | `Eye of Pytũ` (`EndCompassItem`) — points to the Sanctuary coordinates in The End |
| **Summoning Ritual** | T-shaped Totem (`Purpur Pillar` or `Crying Obsidian` 2-block base + `End Stone Bricks` arms) activated on top with the **Ender Dragon Egg** (the Egg is returned to the player and the circular Sanctuary Arena is built automatically) |
| **Phase 1 (100%–50% HP)** | *The Weight of Silence* (`BossPhase.PHASE_ONE`) — `VoidSlamAttack` (AoE shockwave + knock-up) & `TeleportBehindTargetGoal` (Shadow Step) |
| **Phase 2 (< 50% HP)** | *The Primordial Collapse* (`BossPhase.ENRAGED`) — Sky darkens (`setDarkenScreen(true)`), `+35% Speed`, and `GravityOrbAttack` (fires Zero-Gravity Orbs causing Levitation + Darkness) |
| **Legendary Drop** | `Blade of Pytũ` (`VoidbladeSwordItem`) — Void-safe floating/glowing drop; applies Darkness/Slowness on hit (`@Override hurtEnemy`) and grants the Boss's Shadow Step dash on right-click (`@Override use`) |

---

### Project Structure

```text
primordialnight/
├── _docs/                                           # Screenshots, banners, and combat GIFs
├── build.gradle                                     # Forge 1.20.1 & Java 17 build configuration
├── gradle.properties                                # Centralized mod metadata (mod_id, version, authors)
└── src/main/
    ├── java/net/poo/primordialnight/
    │   ├── PrimordialNightMod.java                  # Main @Mod orchestrator class (registrations only)
    │   ├── block/
    │   │   └── ModBlocks.java                       # DeferredRegister for blocks (Generics)
    │   ├── item/
    │   │   ├── ModItems.java                        # DeferredRegister for items
    │   │   ├── ModToolTiers.java                    # Custom PRIMORDIAL_VOID weapon tier
    │   │   ├── ModCreativeModeTabs.java             # Creative mode tab registration
    │   │   └── custom/
    │   │       ├── EndCompassItem.java              # Void Compass pointing to the Sanctuary
    │   │       └── VoidbladeSwordItem.java          # Legendary Sword drop (extends SwordItem)
    │   ├── entity/
    │   │   ├── ModEntities.java                     # EntityType registrations
    │   │   ├── enums/
    │   │   │   └── BossPhase.java                   # Typed Enum (PHASE_ONE, ENRAGED, VULNERABLE)
    │   │   ├── custom/
    │   │   │   ├── YmaPytuBossEntity.java           # Main Boss Entity (extends Monster, ServerBossEvent)
    │   │   │   └── GravityOrbProjectileEntity.java  # Phase 2 Zero-Gravity Orb (extends AbstractHurtingProjectile)
    │   │   ├── ai/
    │   │   │   ├── attack/
    │   │   │   │   ├── BossAttackAction.java        # Strategy Pattern interface
    │   │   │   │   ├── AbstractBossAttack.java      # Abstract base class (Template Method)
    │   │   │   │   ├── VoidSlamAttack.java          # Phase 1 AoE shockwave attack
    │   │   │   │   └── GravityOrbAttack.java        # Phase 2 projectile attack
    │   │   │   └── goal/
    │   │   │       ├── YmaPytuAttackGoal.java       # AI Goal selecting attacks via Strategy Pattern
    │   │   │       └── TeleportBehindTargetGoal.java# Shadow Step AI Goal
    │   │   └── client/
    │   │       ├── ModModelLayers.java              # ModelLayerLocation ID
    │   │       ├── YmaPytuModel.java                # Blockbench HierarchicalModel
    │   │       ├── YmaPytuRenderer.java             # MobRenderer with phase texture switching
    │   │       └── ModAnimationDefinitions.java     # Blockbench keyframe animations
    │   ├── procedures/
    │   │   ├── TotemStructureValidator.java         # Validates T-shaped Totem blocks
    │   │   ├── SanctuaryArenaBuilderProcedure.java  # Builds circular Sanctuary arena in The End
    │   │   └── VoidbladeShadowStepProcedure.java    # Executes Voidblade right-click dash & AoE
    │   ├── event/
    │   │   ├── ModEvents.java                       # RightClickBlock ritual with Dragon Egg
    │   │   ├── ModEventBusEvents.java               # EntityAttributeCreationEvent
    │   │   └── ModEventBusClientEvents.java         # RegisterLayerDefinitions
    │   ├── datagen/
    │   │   └── DataGenerators.java                  # Code-driven JSON generation
    │   ├── sound/
    │   │   └── ModSounds.java                       # Custom SoundEvents
    │   └── util/
    │       └── ModTags.java                         # Static nested classes for Block/Item tags
    └── resources/
        ├── META-INF/mods.toml
        ├── assets/primordialnight/                  # Client lang files, models, sounds, textures
        └── data/primordialnight/                    # Server loot tables and recipes
```

---

### Roadmap / Possible Updates

- [x] Project architecture & OOP package structure (`net.poo.primordialnight`);
- [ ] Phase 1 & Phase 2 Boss combat logic (`YmaPytuBossEntity`, `BossPhase`);
- [ ] Strategy Pattern attacks (`VoidSlamAttack`, `GravityOrbAttack`) & `TeleportBehindTargetGoal`;
- [ ] Blockbench 3D Model (`YmaPytuModel`) & Animations (`ModAnimationDefinitions`);
- [ ] Totem + Dragon Egg summoning ritual & automatic Sanctuary Arena construction;
- [ ] Legendary `VoidbladeSwordItem` drop & `EndCompassItem`.

---

### Authors & Credits

- **Team:** Alane, Danilo, Arthur, Kalebe
- **Architectural Reference & Best Practices:** Architectural structure and naming conventions inspired by the repositories of **Rômulo Henri ([@romhenri](https://github.com/romhenri))** (`rise-modpack` and `drg-stock`).
- **Technical Modding References:** Kaupenjoe (`Forge-Tutorial-1.20.X`), `Bosses-of-Mass-Destruction-FORGE`, `mirror-abyss`, `silver-samurai-boss`, `Astram_Anvishya_Asuram`, `AlexsMobs`, and `boss-locator`.
