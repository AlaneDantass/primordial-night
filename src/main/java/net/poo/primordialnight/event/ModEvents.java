package net.poo.primordialnight.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.poo.primordialnight.PrimordialNightMod;
import net.poo.primordialnight.entity.ModEntities;
import net.poo.primordialnight.entity.custom.YmaPytuBossEntity;
import net.poo.primordialnight.procedures.SanctuaryArenaBuilderProcedure;
import net.poo.primordialnight.procedures.TotemStructureValidator;


@Mod.EventBusSubscriber(modid = PrimordialNightMod.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onRightClickBlockWithDragonEgg(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if (level.isClientSide()) {
            return;
        }

        // Only allow summoning inside The End dimension
        if (!level.dimension().location().equals(Level.END.location())) {
            return;
        }

        ItemStack heldStack = event.getItemStack();
        if (!heldStack.is(Items.DRAGON_EGG)) {
            return;
        }

        BlockPos clickedPos = event.getPos();
        if (!TotemStructureValidator.isValidTotemStructure(level, clickedPos)) {
            return;
        }

        if (level instanceof ServerLevel serverLevel) {
            Player player = event.getEntity();

            // 1. Cancel default Dragon Egg placement so the player keeps the unique Dragon Egg in hand!
            event.setCanceled(true);

            // 2. Dismantle the 4 Totem blocks with purple particles and thunder
            TotemStructureValidator.dismantleTotemStructure(serverLevel, clickedPos);

            // 3. Build the circular Sanctuary of the First Night arena around the ritual site
            SanctuaryArenaBuilderProcedure.buildCircularSanctuaryArena(serverLevel, clickedPos.below());

            YmaPytuBossEntity bossEntity = ModEntities.YMA_PYTU_BOSS.get().create(serverLevel);
            if (bossEntity != null) {
                BlockPos spawnPos = clickedPos.below();
                bossEntity.moveTo(spawnPos.getX() + 0.5, spawnPos.getY() + 1.0, spawnPos.getZ() + 0.5, 0.0F, 0.0F);
                serverLevel.sendParticles(ParticleTypes.PORTAL, spawnPos.getX() + 0.5, spawnPos.getY() + 1.5, spawnPos.getZ() + 0.5, 40, 1.0, 1.0, 1.0, 0.2);
                serverLevel.playSound(null, spawnPos, SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.HOSTILE, 2.0F, 0.8F);
                serverLevel.addFreshEntity(bossEntity);
            }
        }
    }
}
