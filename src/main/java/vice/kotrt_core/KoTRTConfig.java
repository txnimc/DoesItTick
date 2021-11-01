package vice.kotrt_core;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import lombok.val;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import vice.kotrt_core.config.ConfigBuilder;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class KoTRTConfig
{
    public static ForgeConfigSpec ConfigSpec;

    public static ForgeConfigSpec.ConfigValue<Boolean> fpsCounter;

    public static ForgeConfigSpec.ConfigValue<Integer> maxTileEntityRenderDistanceSquare;
    public static ForgeConfigSpec.ConfigValue<Integer> maxTileEntityRenderDistanceY;

    public static ForgeConfigSpec.ConfigValue<Integer> cloudHeight;

    public static ForgeConfigSpec.ConfigValue<Integer> maxEntityRenderDistanceSquare;
    public static ForgeConfigSpec.ConfigValue<Integer> maxEntityRenderDistanceY;

    public static ForgeConfigSpec.ConfigValue<Integer> maxLivingEntityTickDistanceSquare;
    public static ForgeConfigSpec.ConfigValue<Integer> maxLivingEntityTickDistanceY;

    public static ForgeConfigSpec.ConfigValue<Integer> maxLivingEntitySpawnDistanceY;
    public static ForgeConfigSpec.ConfigValue<Integer> maxLivingEntitySpawnDistanceSquare;

    public static ForgeConfigSpec.ConfigValue<Integer> maxFluidTickDistanceY;
    public static ForgeConfigSpec.ConfigValue<Integer> maxFluidTickDistanceSquare;

    public static ForgeConfigSpec.ConfigValue<Integer> sodiumUpdateWaitTime;

    static
    {
        val builder = new ConfigBuilder("Knights Of The Round Table Core Settings");

        builder.Block("Miscellaneous Toggles", b -> {
            fpsCounter = b.define("Show FPS Counter", true);
        });

        builder.Block("Entity Lag Improvements", b -> {
            maxTileEntityRenderDistanceSquare = b.define("(TileEntity) Max Horizontal Render Distance [Squared, Default 32^2]", 1024);
            maxTileEntityRenderDistanceY = b.define("(TileEntity) Max Vertical Render Distance [Raw, Default 32]", 24);

            maxEntityRenderDistanceSquare = b.define("(Entity) Max Horizontal Render Distance [Squared, Default 48^2]", 2304);
            maxEntityRenderDistanceY = b.define("(Entity) Max Vertical Render Distance [Raw, Default 32]", 32);

            maxLivingEntityTickDistanceSquare = b.define("(LivingEntity) Max Horizontal Tick Distance [Squared, Default 48^2]", 2304);
            maxLivingEntityTickDistanceY = b.define("(LivingEntity) Max Vertical Tick Distance [Raw, Default 32]", 32);

            maxLivingEntitySpawnDistanceSquare = b.define("(LivingEntity) Max Spawn Check Horizontal Distance [Squared, Default 64^2]", 4096);
            maxLivingEntitySpawnDistanceY = b.define("(LivingEntity) Max Spawn Check Vertical Distance [Raw, Default 32]", 32);

            maxFluidTickDistanceSquare = b.define("(LivingEntity) Max Fluid Tick Horizontal Distance [Squared, Default 48^2]", 2304);
            maxFluidTickDistanceY = b.define("(LivingEntity) Max Fluid Tick Vertical Distance [Raw, Default 32]", 32);

            cloudHeight = b.define("Cloud Height [Raw, Default 196]", 196);

            sodiumUpdateWaitTime = b.define("Sodium Chunk Update Time [Default 0 (Off)]", 0);
        });

        ConfigSpec = builder.Save();
    }

    public static void loadConfig(Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();

        configData.load();
        ConfigSpec.setConfig(configData);
    }
}