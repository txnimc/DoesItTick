package vice.kotrt_core.mixins.EntityTick;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import vice.kotrt_core.KoTRTConfig;
import vice.kotrt_core.util.KoTRTUtilities;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;


@Mixin(value = World.class)
public abstract class EntityTickMixin
{
    @Shadow @Final public Random random;

    /**
     * @author Team Deus Vult
     */
    @Overwrite
    public void guardEntityTick(Consumer<Entity> consumer, Entity entity)
    {
        World world = ((World) (Object) this);
        BlockPos entityPos = entity.blockPosition();

        boolean isInClaimedChunk = KoTRTUtilities.isInClaimedChunk(world, entityPos);

        if (!(entity instanceof LivingEntity) || entity instanceof PlayerEntity)
        {
            if (!isInClaimedChunk && entity instanceof ItemEntity && random.nextInt(4) == 0)
            {
                return;
            }

            handleGuardEntityTick(consumer, entity);
            return;
        }

        int maxHeight = KoTRTConfig.maxLivingEntityTickDistanceY.get();
        int maxDistanceSquare = KoTRTConfig.maxLivingEntityTickDistanceSquare.get();

        if (KoTRTUtilities.isNearPlayer(world, entityPos, maxHeight, maxDistanceSquare))
        {
            handleGuardEntityTick(consumer, entity);
            return;
        }

        // handle edge cases for entities outside radius

        if (isInClaimedChunk && ((LivingEntity) entity).isDeadOrDying())
        {
            handleGuardEntityTick(consumer, entity);
            return;
        }
    }



    public void handleGuardEntityTick(Consumer<Entity> consumer, Entity entity)
    {
        try
        {
            net.minecraftforge.server.timings.TimeTracker.ENTITY_UPDATE.trackStart(entity);
            consumer.accept(entity);
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.forThrowable(throwable, "Ticking entity");
            CrashReportCategory crashreportcategory = crashreport.addCategory("Entity being ticked");
            entity.fillCrashReportCategory(crashreportcategory);
            throw new ReportedException(crashreport);
        }
        finally
        {
            net.minecraftforge.server.timings.TimeTracker.ENTITY_UPDATE.trackEnd(entity);
        }
    }


}