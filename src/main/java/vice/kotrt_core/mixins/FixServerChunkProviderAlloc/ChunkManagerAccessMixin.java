package vice.kotrt_core.mixins.FixServerChunkProviderAlloc;

import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ChunkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChunkManager.class)
public interface ChunkManagerAccessMixin
{
    @Invoker
    abstract Iterable<ChunkHolder> callGetChunks();

}
