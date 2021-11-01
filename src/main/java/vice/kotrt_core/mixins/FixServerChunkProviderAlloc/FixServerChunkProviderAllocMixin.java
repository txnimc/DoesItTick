package vice.kotrt_core.mixins.FixServerChunkProviderAlloc;

import lombok.val;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ChunkManager;
import net.minecraft.world.server.ServerChunkProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vice.kotrt_core.features.FixServerChunkProviderAlloc.ChunkHolderIterableHack;

import java.util.ArrayList;

@Mixin(ServerChunkProvider.class)
public class FixServerChunkProviderAllocMixin
{
    private static ChunkHolderIterableHack letsHopeThisDoesntCrash;

    @Shadow @Final public ChunkManager chunkMap;

    @Redirect(method = "tickChunks", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Lists;newArrayList(Ljava/lang/Iterable;)Ljava/util/ArrayList;"))
    public ArrayList<ChunkHolder> handleChunkTick(Iterable<ChunkHolder> elements)
    {
        val chunks = ((ChunkManagerAccessMixin)chunkMap).callGetChunks();

        if (letsHopeThisDoesntCrash == null)
            letsHopeThisDoesntCrash = new ChunkHolderIterableHack();

        letsHopeThisDoesntCrash.setIterable(chunks);

        return letsHopeThisDoesntCrash;
    }
}

