package net.poo.primordialnight.procedures;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;

/**
 * Builds the circular "Sanctuary of the First Night" arena (End Stone Bricks and Obsidian)
 * around the Totem activation point in The End, solving Danilo's "Open End" problem.
 * <p>
 * Team Decision (A2 & Yma-Pytu Creative Sheet Section 2.A):
 * When the player activates the Totem with the Dragon Egg in The End, this procedure constructs
 * the circular arena platform and border walls so neither the player nor Yma-Pytu falls into the void.
 * <p>
 * Responsible: Dev 4 & Dev 1
 * OOP Concepts: Single Responsibility Principle, static helper methods, named constants (no magic numbers).
 */
public class SanctuaryArenaBuilderProcedure {

    public static final int ARENA_RADIUS = 16;
    public static final int BORDER_WALL_HEIGHT = 3;

    public static void buildCircularSanctuaryArena(ServerLevel serverLevel, BlockPos centerPos) {
        // TODO (Dev 4):
        for (int x = -ARENA_RADIUS; x <= ARENA_RADIUS; x++) {
            for (int z = -ARENA_RADIUS; z <= ARENA_RADIUS; z++) {
                int distSq = x * x + z * z;
                if (distSq <= ARENA_RADIUS * ARENA_RADIUS) {
                    BlockPos floorPos = centerPos.offset(x, 0, z);
                    serverLevel.setBlock(floorPos, Blocks.END_STONE_BRICKS.defaultBlockState(), 3);
                    for (int y = 1; y <= 6; y++) {
                        serverLevel.setBlock(floorPos.above(y), Blocks.AIR.defaultBlockState(), 3);
                    }
                    if (distSq > (ARENA_RADIUS - 1) * (ARENA_RADIUS - 1)) {
                        for (int y = 1; y <= BORDER_WALL_HEIGHT; y++) {
                            serverLevel.setBlock(floorPos.above(y), Blocks.NETHER_BRICK_FENCE.defaultBlockState(), 3);
                        }
                    }
                }
            }
        }

    }
}

