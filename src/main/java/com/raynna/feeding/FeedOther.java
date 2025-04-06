package com.raynna.feeding;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(FeedOther.MOD_ID)
public class FeedOther
{
    public static final String MOD_ID = "feedother";
    public static final String MOD_NAME = "RaynnaFeedOther";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static FeedOther INSTANCE;
    public static IProxy PROXY;

    public FeedOther(IEventBus modEventBus, ModContainer modContainer)
    {
        INSTANCE = this;
        PROXY = FMLEnvironment.dist == Dist.CLIENT
                ? new SideProxy.Client(modEventBus, modContainer)
                : new SideProxy.Server(modEventBus, modContainer);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.Server.SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.Client.SPEC);
        NeoForge.EVENT_BUS.register(this);

    }

    public static ResourceLocation getId(String path) {
        if (path.contains(":")) {
            if (path.startsWith(FeedOther.MOD_ID)) {
                return ResourceLocation.tryParse(path);
            } else {
                throw new IllegalArgumentException("path contains namespace other than " + FeedOther.MOD_ID);
            }
        }
        return ResourceLocation.fromNamespaceAndPath(FeedOther.MOD_ID, path);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("[RaynnaFeedOther] Mod loaded on dedicated server]");
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            LOGGER.info("[RaynnaFeedOther] Mod loaded on client]");
        }
    }
}
