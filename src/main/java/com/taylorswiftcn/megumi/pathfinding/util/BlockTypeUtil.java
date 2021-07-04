package com.taylorswiftcn.megumi.pathfinding.util;

import net.minecraft.block.Block;

import static net.minecraft.init.Blocks.*;

public class BlockTypeUtil {

    public static boolean isStep(Block type) {
        if (STONE_SLAB.equals(type) ||
                WOODEN_SLAB.equals(type) ||
                PURPUR_SLAB.equals(type) ||
                STONE_SLAB2.equals(type)) {
            return true;
        }

        return false;
    }

    public static boolean isDoorOrGate(Block type) {
        if (DARK_OAK_DOOR.equals(type) ||
                IRON_DOOR.equals(type) ||
                BIRCH_DOOR.equals(type) ||
                ACACIA_DOOR.equals(type) ||
                JUNGLE_DOOR.equals(type) ||
                SPRUCE_DOOR.equals(type) ||
                OAK_DOOR.equals(type) ||
                OAK_FENCE_GATE.equals(type) ||
                BIRCH_FENCE_GATE.equals(type) ||
                ACACIA_FENCE_GATE.equals(type) ||
                JUNGLE_FENCE_GATE.equals(type) ||
                SPRUCE_FENCE_GATE.equals(type) ||
                DARK_OAK_FENCE_GATE.equals(type)) {
            return true;
        }

        return false;
    }

    public static boolean isFence(Block type) {
        if (OAK_FENCE.equals(type) ||
                ACACIA_FENCE.equals(type) ||
                BIRCH_FENCE.equals(type) ||
                DARK_OAK_FENCE.equals(type) ||
                JUNGLE_FENCE.equals(type) ||
                NETHER_BRICK_FENCE.equals(type) ||
                SPRUCE_FENCE.equals(type) ||
                OAK_FENCE_GATE.equals(type) ||
                BIRCH_FENCE_GATE.equals(type) ||
                ACACIA_FENCE_GATE.equals(type) ||
                JUNGLE_FENCE_GATE.equals(type) ||
                SPRUCE_FENCE_GATE.equals(type) ||
                DARK_OAK_FENCE_GATE.equals(type)) {
            return true;
        }

        return false;
    }

    public static boolean isWall(Block type) {
        return type.equals(COBBLESTONE_WALL);
    }
}
