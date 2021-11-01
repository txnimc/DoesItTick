package vice.kotrt_core.mixins.WorldEntitySpawnerOptimization;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vice.kotrt_core.KoTRTConfig;
import vice.kotrt_core.util.KoTRTUtilities;

import java.util.List;
import java.util.Random;

@Mixin(WorldEntitySpawner.class)
public class WorldEntitySpawnerOptimizationMixin
{
    @Inject(at = @At("HEAD"), method = "getRandomSpawnMobAt", cancellable = true)
    private static void getRandomSpawnMobAt(ServerWorld list, StructureManager p_234977_0_, ChunkGenerator p_234977_1_, EntityClassification p_234977_2_, Random p_234977_3_, BlockPos blockPos, CallbackInfoReturnable<MobSpawnInfo.Spawners> cir)
    {
        if (KoTRTUtilities.isInClaimedChunk(list, blockPos))
            return;

        int maxHeight = KoTRTConfig.maxLivingEntitySpawnDistanceY.get();
        int maxDistanceSquare = KoTRTConfig.maxLivingEntitySpawnDistanceSquare.get();

        if (KoTRTUtilities.isNearPlayer(list, blockPos, maxHeight, maxDistanceSquare))
            return;

        // could not find player
        cir.cancel();
    }
}
