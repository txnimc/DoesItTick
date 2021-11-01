package vice.kotrt_core.mixins.FluidPerformance;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vice.kotrt_core.KoTRTConfig;
import vice.kotrt_core.util.KoTRTUtilities;

import java.util.List;

@Mixin(FluidState.class)
public class FluidStateMixin
{
    @Inject(at = @At("HEAD"), method = "tick", cancellable = true)
    public void tick(World list, BlockPos blockPos, CallbackInfo cir)
    {
        if (KoTRTUtilities.isInClaimedChunk(list, blockPos))
            return;

        int maxHeight = KoTRTConfig.maxFluidTickDistanceY.get();
        int maxDistanceSquare = KoTRTConfig.maxFluidTickDistanceSquare.get();

        if (KoTRTUtilities.isNearPlayer(list, blockPos, maxHeight, maxDistanceSquare))
            return;

        // could not find player
        cir.cancel();
    }
}