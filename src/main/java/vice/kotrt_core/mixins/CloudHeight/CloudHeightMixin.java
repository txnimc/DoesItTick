package vice.kotrt_core.mixins.CloudHeight;

import net.minecraft.client.world.DimensionRenderInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vice.kotrt_core.KoTRTConfig;

@Mixin(value = DimensionRenderInfo.class)
public class CloudHeightMixin
{
    @Shadow @Final private float cloudLevel;

    @Inject(at = @At("HEAD"), method = "getCloudHeight", cancellable = true)
    private void getCloudHeight(CallbackInfoReturnable<Float> cir)
    {
        if (cloudLevel == 128.0F)
            cir.setReturnValue(KoTRTConfig.cloudHeight.get().floatValue());
    }
}


/*@Mixin(WorldRenderer.class)
public abstract class CloudHeightMixin
{
    @Shadow private ClientWorld level;

    @Redirect(
            method = "renderClouds",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/DimensionRenderInfo;getCloudHeight()F")
    )
    private float getCloudHeight(DimensionRenderInfo skyProperties)
    {
        if(level.dimension() == World.OVERWORLD)
        {
            return 196.0F;
        }

        return skyProperties.getCloudHeight();
    }
}*/