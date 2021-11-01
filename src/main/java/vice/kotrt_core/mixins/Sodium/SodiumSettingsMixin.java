package vice.kotrt_core.mixins.Sodium;


import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import vice.kotrt_core.KoTRTConfig;

import java.util.ArrayList;
import java.util.List;

@Pseudo
@Mixin(SodiumOptionsGUI.class)
public abstract class SodiumSettingsMixin
{

    @Shadow
    @Final
    private List<OptionPage> pages;
    private static final SodiumOptionsStorage dynamicLightsOpts = new SodiumOptionsStorage();


    @Inject(method = "<init>", at = @At("RETURN"))
    private void DynamicLights(Screen prevScreen, CallbackInfo ci)
    {
        List<OptionGroup> groups = new ArrayList();

        OptionImpl<SodiumGameOptions, Boolean> fpsCounter = OptionImpl.createBuilder(Boolean.class, dynamicLightsOpts)
                .setName("Show FPS Counter")
                .setTooltip("Use the KoTRT FPS counter, which is the best. Shows minimum frames and 15 second averages.")
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> KoTRTConfig.fpsCounter.set(value),
                        (options) -> KoTRTConfig.fpsCounter.get())
                .setImpact(OptionImpact.LOW)
                .build();


        groups.add(OptionGroup
                .createBuilder()
                .add(fpsCounter)
                .build()
        );


        OptionImpl<SodiumGameOptions, Integer> cloudHeight = OptionImpl.createBuilder(Integer.TYPE, dynamicLightsOpts)
                .setName("Cloud Height")
                .setTooltip("Raises cloud height.")
                .setControl((option) -> new SliderControl(option, 64, 256, 4, ControlValueFormatter.quantity("Blocks")))
                .setBinding(
                        (options, value) -> {
                            KoTRTConfig.cloudHeight.set(value);
                        },
                        (options) ->  KoTRTConfig.cloudHeight.get())
                .setImpact(OptionImpact.LOW)
                .build();



        groups.add(OptionGroup
                .createBuilder()
                .add(cloudHeight)
                .build()
        );


        OptionImpl<SodiumGameOptions, Integer> maxEntityDistance = OptionImpl.createBuilder(Integer.TYPE, dynamicLightsOpts)
                .setName("Max Entity Distance")
                .setTooltip("Hides and does not tick entities beyond this many blocks. Huge performance increase, especially around modded farms.")
                .setControl((option) -> new SliderControl(option, 8, 96, 4, ControlValueFormatter.quantity("Blocks")))
                .setBinding(
                        (options, value) -> {
                            KoTRTConfig.maxEntityRenderDistanceSquare.set(value * value);
                            KoTRTConfig.maxLivingEntityTickDistanceSquare.set(value * value);
                        },
                        (options) ->  Math.toIntExact(Math.round(Math.sqrt(KoTRTConfig.maxEntityRenderDistanceSquare.get()))))
                .setImpact(OptionImpact.HIGH)
                .build();

        OptionImpl<SodiumGameOptions, Integer> maxEntityDistanceVertical = OptionImpl.createBuilder(Integer.TYPE, dynamicLightsOpts)
                .setName("Vertical Entity Distance")
                .setTooltip("Hides and does not tick entities underneath this many blocks, improving performance above caves. This should ideally be set lower than the horizontal distance.")
                .setControl((option) -> new SliderControl(option, 8, 64, 4, ControlValueFormatter.quantity("Blocks")))
                .setBinding(
                        (options, value) -> KoTRTConfig.maxEntityRenderDistanceY.set(value ),
                        (options) -> KoTRTConfig.maxEntityRenderDistanceY.get())
                .setImpact(OptionImpact.HIGH)
                .build();


        groups.add(OptionGroup
                .createBuilder()
                .add(maxEntityDistance)
                .add(maxEntityDistanceVertical)
                .build()
        );

        OptionImpl<SodiumGameOptions, Integer> maxTileEntityDistance = OptionImpl.createBuilder(Integer.TYPE, dynamicLightsOpts)
                .setName("Max Tile Distance")
                .setTooltip("Hides block entities beyond this many blocks. Huge performance increase, especially around lots of modded machines.")
                .setControl((option) -> new SliderControl(option, 8, 96, 4, ControlValueFormatter.quantity("Blocks")))
                .setBinding(
                        (options, value) -> KoTRTConfig.maxTileEntityRenderDistanceSquare.set(value * value),
                        (options) -> Math.toIntExact(Math.round(Math.sqrt(KoTRTConfig.maxTileEntityRenderDistanceSquare.get()))))
                .setImpact(OptionImpact.HIGH)
                .build();

        OptionImpl<SodiumGameOptions, Integer> maxTileEntityDistanceVertical = OptionImpl.createBuilder(Integer.TYPE, dynamicLightsOpts)
                .setName("Vertical Tile Distance")
                .setTooltip("Hides block entities underneath this many blocks, improving performance above caves (if you have your machines in caves, for some reason). This should ideally be set lower than the horizontal distance.")
                .setControl((option) -> new SliderControl(option, 8, 64, 4, ControlValueFormatter.quantity("Blocks")))
                .setBinding(
                        (options, value) -> KoTRTConfig.maxTileEntityRenderDistanceY.set(value ),
                        (options) -> KoTRTConfig.maxTileEntityRenderDistanceY.get())
                .setImpact(OptionImpact.HIGH)
                .build();

        groups.add(OptionGroup
            .createBuilder()
                .add(maxTileEntityDistance)
                .add(maxTileEntityDistanceVertical)
            .build()
        );





        OptionImpl<SodiumGameOptions, Integer> updateTime = OptionImpl.createBuilder(Integer.TYPE, dynamicLightsOpts)
                .setName("Delay Chunk Update")
                .setTooltip("Advanced setting, can help improve performance in extremely laggy farms, at the cost of delaying chunk updates. If unsure, leave this at 0")
                .setControl((option) -> new SliderControl(option, 0, 250, 1, ControlValueFormatter.quantity("ms")))
                .setBinding(
                        (options, value) -> {
                            KoTRTConfig.sodiumUpdateWaitTime.set(value);
                        },
                        (options) ->  KoTRTConfig.sodiumUpdateWaitTime.get())
                .setImpact(OptionImpact.LOW)
                .build();


        groups.add(OptionGroup
                .createBuilder()
                .add(updateTime)
                .build()
        );


        pages.add(new OptionPage("KoTRT", ImmutableList.copyOf(groups)));
    }
}