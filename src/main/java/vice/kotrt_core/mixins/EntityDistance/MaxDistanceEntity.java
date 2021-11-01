package vice.kotrt_core.mixins.EntityDistance;

import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vice.kotrt_core.KoTRTConfig;
import vice.kotrt_core.util.KoTRTUtilities;

@Mixin(EntityRendererManager.class)
public class MaxDistanceEntity
{
    @Inject(at = @At("HEAD"), method = "shouldRender", cancellable = true)
    public <E extends Entity> void shouldDoRender(E entity, ClippingHelper clippingHelper, double cameraX, double cameraY, double cameraZ, CallbackInfoReturnable<Boolean> cir)
    {
        if (!KoTRTUtilities.isEntityWithinDistance(
                entity,
                cameraX,
                cameraY,
                cameraZ,
                KoTRTConfig.maxEntityRenderDistanceY.get(),
                KoTRTConfig.maxEntityRenderDistanceSquare.get()
        ))
        {
            cir.cancel();
        }
    }
}

