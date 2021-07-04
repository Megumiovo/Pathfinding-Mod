package com.taylorswiftcn.megumi.pathfinding.algorithm;

import com.taylorswiftcn.megumi.pathfinding.util.BlockTypeUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class AstarFinder {

    private EntityPlayerSP player;
    private World world;

    private BlockPos origin;
    private BlockPos target;

    private PathNode start;
    private PathNode end;

    private Queue<PathNode> openNodes;
    private List<PathNode> closeNodes;

    private BlockPos[] path;

    private Boolean off;

    public AstarFinder(BlockPos origin, BlockPos target, boolean off) {
        this.player = Minecraft.getMinecraft().player;
        this.world = player.world;
        this.origin = origin;
        this.target = target;
        this.start = new PathNode(origin, null, 0, getEuclideanDistance(origin, target));
        this.end = new PathNode(origin, null, 0, 0);
        this.openNodes = new PriorityQueue<>();
        this.closeNodes = new ArrayList<>();
        this.path = null;
        this.off = off;
    }

    public BlockPos[] getPath() {
        openNodes.add(start);

        launch();

        return path;
    }

    private void launch() {
        while (!openNodes.isEmpty()) {

            if (openNodes.size() > 600) return;

            PathNode current = openNodes.poll();

            if (current.getDistance() < 1) {
                int length = 1;
                end = current;
                PathNode last = current;
                while (last.getParent() != null) {
                    length++;
                    last = last.getParent();
                }

                last = end;

                path = new BlockPos[length];
                for (int i = length - 1; i > 0; i--) {
                    BlockPos pos = last.getPos();
                    if (BlockTypeUtil.isStep(world.getBlockState(pos.add(0, -1, 0)).getBlock())) {
                        pos.add(0, -0.5, 0);
                    }
                    path[i] = pos;

                    last = last.getParent();
                }
                path[0] = origin;

                return;
            }

            closeNodes.add(current);
            explore(current);
        }
    }

    private void explore(PathNode node) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if ((x == 0 && (y == 0 || y == -1) && z == 0)) continue;
                    BlockPos pos = node.getPos().add(x, y, z);

                    if (y > 0) {
                        Block block = world.getBlockState(pos).getBlock();
                        if (BlockTypeUtil.isFence(block) || BlockTypeUtil.isWall(block)) continue;
                    }

                    if (!canStandAt(pos)) continue;

                    if (x * z == 0) {
                        addNeighborNode(pos, node, y != 0 ? 1.4142 : 1);
                        continue;
                    }

                    BlockPos neighborA = pos.add(-x, 0, 0);
                    BlockPos neighborB = pos.add(0, 0, -z);
                    if (!(canStandAt(neighborA) || canStandAt(neighborB))) continue;

                    addNeighborNode(pos, node, y == 0 ? 1.4142 : 1.732);
                }
            }
        }
    }

    private void addNeighborNode(BlockPos pos, PathNode parent, double expense) {
        if (!canAddNodeToOpen(pos)) return;
        PathNode child = getNodeInOpen(pos);
        expense = parent.getExpense() + expense;

        if (child == null) {
            child = new PathNode(pos, parent, expense, off ? getEuclideanDistance(pos, target) : getManhattanDistance(pos, target));
            openNodes.add(child);
            return;
        }

        if (child.getExpense() > expense) {
            child.setExpense(expense);
            child.setParent(parent);
        }
    }

    private PathNode getNodeInOpen(BlockPos pos) {
        if (openNodes.isEmpty()) return null;
        for (PathNode node : openNodes) {
            if (node.getPos().equals(pos)) return node;
        }

        return null;
    }

    public boolean canAddNodeToOpen(BlockPos pos) {
        if (closeNodes.isEmpty()) return true;
        for (PathNode node : closeNodes) {
            if (node.getPos().equals(pos)) return false;
        }

        return true;
    }

    private boolean canStandAt(BlockPos pos) {
        if (isObstructed(pos)) return false;

        BlockPos clone = pos.add(0, -1, 0);
        Block block = world.getBlockState(clone).getBlock();
        if (BlockTypeUtil.isFence(block) || BlockTypeUtil.isWall(block)) {
            return !(isObstructed(pos.add(0, 1, 0)) || isObstructed(pos.add(0, 2, 0)));
        }

        return !(isObstructed(pos.add(0, 1, 0)) || !isObstructed(pos.add(0, -1, 0)));
    }

    private boolean isObstructed(BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        Material material = state.getMaterial();

        return material.isSolid();
    }

    private double getManhattanDistance(BlockPos posA, BlockPos posB) {
        return Math.abs(posA.getX() - posB.getX()) + Math.abs(posA.getY() - posB.getY()) + Math.abs(posA.getZ() - posB.getZ());
        /*return Math.pow(locA.getX() - locB.getX(), 2) + Math.pow(locA.getY() - locB.getY(), 2) +Math.pow(locA.getZ() - locB.getZ(), 2);*/
    }

    private double getEuclideanDistance(BlockPos posA, BlockPos posB) {
        double dx = Math.pow(posA.getX() - posB.getX(), 2);
        double dy = Math.pow(posA.getY() - posB.getY(), 2);
        double dz = Math.pow(posA.getZ() - posB.getZ(), 2);

        return Math.sqrt(dx + dy + dz);
    }
}
