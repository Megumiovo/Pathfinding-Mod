package com.taylorswiftcn.megumi.pathfinding.algorithm;

import com.taylorswiftcn.megumi.pathfinding.Main;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiPathFinder extends Thread {

    private EntityPlayerSP player;
    private BlockPos pos;
    private ExecutorService service;
    private CountDownLatch latch;
    private List<ZonePathFinder> finders;

    public MultiPathFinder(BlockPos pos) {
        this.player = Minecraft.getMinecraft().player;
        this.pos = pos;
        this.finders = new LinkedList<>();
    }

    @Override
    public void run() {
        long a = System.currentTimeMillis();
        AstarFinder finder = new AstarFinder(player.getPosition(), pos, false);
        BlockPos[] megumi = finder.getPath();

        if (megumi == null) return;

        int part = megumi.length / 75;
        int extra = megumi.length % 75 - 1;

        service = Executors.newCachedThreadPool();
        latch = new CountDownLatch(part);

        for (int i = 0; i < part; i++) {
            BlockPos origin = megumi[i * 75];
            BlockPos target = megumi[(i + 1) == part ? (i + 1) * 75 + extra : (i + 1) * 75 - 1];

            ZonePathFinder zoneFinder = new ZonePathFinder(origin, target, latch);
            finders.add(zoneFinder);
            service.execute(zoneFinder);
        }
        long b;
        try {
            latch.await();
            b = System.currentTimeMillis();
            List<BlockPos> path = new LinkedList<>();
            for (ZonePathFinder zone : finders) {
                BlockPos[] pos = zone.getPath();
                path.addAll(Arrays.asList(pos));
            }

            Main.getLogger().info(String.format("用时: %s ms | 路径: %s", (b - a), path));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ZonePathFinder implements Runnable {

        private BlockPos origin;
        private BlockPos target;
        private CountDownLatch latch;
        @Getter private BlockPos[] path;

        private ZonePathFinder(BlockPos origin, BlockPos target, CountDownLatch latch) {
            this.origin = origin;
            this.target = target;
            this.latch = latch;
        }

        @Override
        public void run() {
            AstarFinder finder = new AstarFinder(origin, target, true);
            path = finder.getPath();
            System.out.println("Path: " + (path == null));
            latch.countDown();
        }
    }
}
