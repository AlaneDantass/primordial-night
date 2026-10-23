package net.poo.primordialnight.procedures;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Set;

/**
 * Validates and dismantles the T-shaped Ancestral Totem used to awaken Yma-Pytu.
 *
 * Team Decision (A3 & Yma-Pytu Creative Sheet Section 2.B):
 * - Vertical Base (2 blocks high: topBlockPos and topBlockPos.below()):
 *   Accepts either Blocks.PURPUR_PILLAR or Blocks.CRYING_OBSIDIAN.
 * - Horizontal Arms (2 blocks on the left/right of topBlockPos along either X-axis or Z-axis):
 *   Requires Blocks.END_STONE_BRICKS.
 *
 * Responsible: Dev 4
 * OOP Concepts:
 * - Encapsulation & Single Responsibility Principle (separates structure validation from ModEvents)
 * - Set<Block> lookup instead of long chains of '||' (GUIA_BOAS_PRATICAS_ROMHENRI.md Section 10 #3)
 */
public class TotemStructureValidator {

    private static final Set<Block> VALID_TOTEM_BASE_BLOCKS = Set.of(
            Blocks.PURPUR_PILLAR,
            Blocks.CRYING_OBSIDIAN
    );
    private static final Block VALID_TOTEM_ARM_BLOCK = Blocks.END_STONE_BRICKS;

    /**
     * Checks whether the clicked block is the top-center of a valid T-shaped Totem.
     */
    public static boolean isValidTotemStructure(Level level, BlockPos topCenterPos) {
        if (!isBaseBlock(level, topCenterPos) || !isBaseBlock(level, topCenterPos.below())) {
            return false;
        }

        boolean hasArmsOnXAxis = isArmBlock(level, topCenterPos.east()) && isArmBlock(level, topCenterPos.west());
        boolean hasArmsOnZAxis = isArmBlock(level, topCenterPos.north()) && isArmBlock(level, topCenterPos.south());

        return hasArmsOnXAxis || hasArmsOnZAxis;
    }

    /**
     * Removes the 4 blocks of the T-shaped Totem when the Dragon Egg awakens the Boss.
     */
    public static void dismantleTotemStructure(Level level, BlockPos topCenterPos) {
        // TODO (Dev 4):
        // 1. Check whether the arms are aligned on the X-axis (east/west) or Z-axis (north/south).
        // 2. Replace the 2 base blocks and 2 arm blocks with Blocks.AIR using level.destroyBlock(..., false).
    }

    private static boolean isBaseBlock(Level level, BlockPos pos) {
        return VALID_TOTEM_BASE_BLOCKS.contains(level.getBlockState(pos).getBlock());
    }

    private static boolean isArmBlock(Level level, BlockPos pos) {
        return level.getBlockState(pos).is(VALID_TOTEM_ARM_BLOCK);
    }
}
