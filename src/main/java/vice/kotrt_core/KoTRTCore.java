package vice.kotrt_core;

import codechicken.lib.render.block.BlockRenderingRegistry;
import codechicken.lib.render.block.ICCBlockRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.List;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("kotrt_core")
public class KoTRTCore
{
    public static final String MODID = "kotrt_core";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public KoTRTCore()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, KoTRTConfig.ConfigSpec);
        KoTRTConfig.loadConfig(FMLPaths.CONFIGDIR.get().resolve("kotrt-core.toml"));
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }


    @OnlyIn(Dist.CLIENT)
    private void onClientSetup(@Nonnull FMLClientSetupEvent e)
    {

    }


}