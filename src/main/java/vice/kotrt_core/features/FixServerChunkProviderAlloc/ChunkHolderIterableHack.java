package vice.kotrt_core.features.FixServerChunkProviderAlloc;


import net.minecraft.world.server.ChunkHolder;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

public class ChunkHolderIterableHack extends ArrayList<ChunkHolder>
{
    Iterable<ChunkHolder> lmao;

    public ChunkHolderIterableHack()
    {
        super(0);
    }

    public void setIterable(Iterable<ChunkHolder> lmao) {
        this.lmao = lmao;
    }

    @Override
    public void forEach(Consumer<? super ChunkHolder> action) {
        Objects.requireNonNull(action);
        for (ChunkHolder t : lmao)
        {
            action.accept(t);
        }
    }
}
