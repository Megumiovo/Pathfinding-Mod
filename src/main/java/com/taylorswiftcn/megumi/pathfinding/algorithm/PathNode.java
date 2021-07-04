package com.taylorswiftcn.megumi.pathfinding.algorithm;

import lombok.Data;
import net.minecraft.util.math.BlockPos;

@Data
public class PathNode implements Comparable<PathNode> {

    private BlockPos pos;
    private PathNode parent;

    public double expense;
    public double distance;

    public PathNode(BlockPos pos, PathNode parent, double expense, double distance) {
        this.pos = pos;
        this.parent = parent;
        this.expense = expense;
        this.distance = distance;
    }

    public double getPriority() {
        return expense + distance;
    }

    @Override
    public int compareTo(PathNode node) {
        if (node == null) return -1;
        if (getPriority() > node.getPriority()) return 1;
        return -1;
    }
}
