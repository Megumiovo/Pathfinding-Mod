package com.taylorswiftcn.megumi.pathfinding.algorithm;

import com.taylorswiftcn.megumi.pathfinding.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class PathFinder extends Thread {

    private EntityPlayerSP player;
    private BlockPos pos;
    private List<BlockPos> path;
    private Boolean off;

    public PathFinder(BlockPos pos, boolean off) {
        this.player = Minecraft.getMinecraft().player;
        this.pos = pos;
        this.path = new LinkedList<>();
        this.off = off;
    }

    @Override
    public void run() {
        long a = System.currentTimeMillis();
        AstarFinder finder = new AstarFinder(player.getPosition(), pos, off);
        BlockPos[] megumi = finder.getPath();

        if (megumi == null) {
            player.sendChatMessage("寻路失败");
            return;
        }

        long b = System.currentTimeMillis();

        Main.getLogger().info(String.format("用时: %s ms | 路径: %s", (b - a), path));

        path.addAll(Arrays.asList(megumi));
    }
}
