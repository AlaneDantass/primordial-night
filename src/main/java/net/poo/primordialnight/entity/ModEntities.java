package net.poo.primordialnight.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.poo.primordialnight.PrimordialNightMod;
import net.poo.primordialnight.entity.custom.GravityOrbProjectileEntity;
import net.poo.primordialnight.entity.custom.YmaPytuBossEntity;

/**
 * Centralized registry for custom Entities (Boss and Projectiles).
 *
 * Responsible: Dev 1 & Dev 4
 * Reference: Forge-Tutorial-1.20.X-29-attackAnimation (ModEntities.java)
 */
public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, PrimordialNightMod.MOD_ID);

    // Titanic Void Golem (3.8 blocks tall as specified in Yma-Pytu creative sheet)
    public static final RegistryObject<EntityType<YmaPytuBossEntity>> YMA_PYTU_BOSS =
            ENTITY_TYPES.register("yma_pytu_boss",
                    () -> EntityType.Builder.of(YmaPytuBossEntity::new, MobCategory.MONSTER)
                            .sized(1.8F, 3.8F)
                            .fireImmune()
                            .build("yma_pytu_boss"));

    // Phase 2 Zero-Gravity Orb Projectile
    public static final RegistryObject<EntityType<GravityOrbProjectileEntity>> GRAVITY_ORB_PROJECTILE =
            ENTITY_TYPES.register("gravity_orb_projectile",
                    () -> EntityType.Builder.<GravityOrbProjectileEntity>of(GravityOrbProjectileEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build("gravity_orb_projectile"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
