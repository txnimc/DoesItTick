package vice.kotrt_core.mixins.Sodium;

import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vice.kotrt_core.KoTRTConfig;
import vice.kotrt_core.KoTRTCore;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;


@Mixin(ChunkRenderManager.class)
public class SodiumChunkUpdateMixin
{
    Instant lastChunkUpdateTime;

    @Inject(at = @At("HEAD"), method = "updateChunks", cancellable = true, remap = false)
    public void shouldRun(CallbackInfo ci)
    {
        int updatetime = KoTRTConfig.sodiumUpdateWaitTime.get();
        if (updatetime == 0)
            return;

        if (lastChunkUpdateTime == null)
            lastChunkUpdateTime = Instant.now().minusSeconds(1);

        Instant now = Instant.now();
        Duration res = Duration.between(lastChunkUpdateTime, now);

        if (res.toMillis() < updatetime) {
            ci.cancel();
            return;
        }

        lastChunkUpdateTime = now;
    }
}
