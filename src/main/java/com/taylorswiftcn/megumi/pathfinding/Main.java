package com.taylorswiftcn.megumi.pathfinding;

import lombok.Getter;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION)
public class Main {
    public static final String MODID = "pathfinding";
    public static final String NAME = "Pathfinding Mod";
    public static final String VERSION = "1.0";

    @Getter private static Logger logger;

    @Getter private static Main instance;

    public BlockPos pos;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        instance = this;
        pos = null;
        logger.info("Pathfinding 加载成功!");
    }
}