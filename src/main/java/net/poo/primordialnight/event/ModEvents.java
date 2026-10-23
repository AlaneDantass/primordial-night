package net.poo.primordialnight.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.poo.primordialnight.PrimordialNightMod;
import net.poo.primordialnight.procedures.SanctuaryArenaBuilderProcedure;
import net.poo.primordialnight.procedures.TotemStructureValidator;

/**
 * Listens to Forge game events on the main Forge Event Bus.
 * Implements the Team's Totem + Ender Dragon Egg Summoning Ritual.
 *
 * Responsible: Dev 4
 * OOP Concepts: Event-Driven Architecture (@SubscribeEvent), delegation to TotemStructureValidator
 * and SanctuaryArenaBuilderProcedure.
 *
 * Reference:
 * - Yma-Pytu, A Primeira Noite.pdf (Section 2.B)
 * - GUIA_BOAS_PRATICAS_ROMHENRI.md (Section 6.4 & Section 7 #6 Dimension check)
 */
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

            // TODO (Dev 4):
            // 4. Instantiate YmaPytuBossEntity via ModEntities.YMA_PYTU_BOSS.get().create(serverLevel),
            //    move it to clickedPos.below(), spawn purple portal particles and thunder sound,
            //    and call serverLevel.addFreshEntity(bossEntity).
        }
    }
}
