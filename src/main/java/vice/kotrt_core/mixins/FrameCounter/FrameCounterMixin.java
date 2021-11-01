package vice.kotrt_core.mixins.FrameCounter;

import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.gui.ForgeIngameGui;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vice.kotrt_core.KoTRTConfig;
import vice.kotrt_core.features.FrameCounter.MinFrameProvider;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

@Mixin(ForgeIngameGui.class)
public class FrameCounterMixin
{

    private int lastMeasuredFPS;
    private String runningAverageFPS;
    private final Queue<Integer> fpsRunningAverageQueue = new LinkedList<Integer>();

    @Inject(at = @At("TAIL"), method = "render")
    public void render(MatrixStack matrixStack, float tickDelta, CallbackInfo info)
    {
        if (!KoTRTConfig.fpsCounter.get())
            return;

        Minecraft client = Minecraft.getInstance();

        // return if F3 menu open and graph not displayed
        if (client.options.renderDebug && !client.options.renderFpsChart)
            return;

        MinFrameProvider.recalculate();

        int fps = FpsAccessorMixin.getFPS();

        if (lastMeasuredFPS != fps)
        {
            lastMeasuredFPS = fps;

            if (fpsRunningAverageQueue.size() > 14)
                fpsRunningAverageQueue.poll();

            fpsRunningAverageQueue.offer(fps);

            int totalFps = 0;
            int frameCount = 0;
            for (val frameTime : fpsRunningAverageQueue)
            {
                totalFps += frameTime;
                frameCount++;
            }

            int average = (int) (totalFps / frameCount);
            runningAverageFPS = String.valueOf(average);
        }

        String displayString = fps + " | MIN " + MinFrameProvider.getLastMinFrame() + " | AVG " + runningAverageFPS;

        float textPosX = 8;
        float textPosY = 8;
        int textAlpha = 200;
        int textColor = 0xFFFFFF;
        float fontScale = 0.75F;

        double guiScale = client.getWindow().getGuiScale();
        if (guiScale > 0) {
            textPosX /= guiScale;
            textPosY /= guiScale;
        }

        // Prevent FPS-Display to render outside screenspace
        float maxTextPosX = client.getWindow().getGuiScaledWidth() - client.font.width(displayString);
        float maxTextPosY = client.getWindow().getGuiScaledHeight() - client.font.lineHeight;
        textPosX = Math.min(textPosX, maxTextPosX);
        textPosY = Math.min(textPosY, maxTextPosY);

        int drawColor = ((textAlpha & 0xFF) << 24) | textColor;

        if (client.getWindow().getGuiScale() > 3)
        {
            GL11.glPushMatrix();
            GL11.glScalef(fontScale, fontScale, fontScale);
            client.font.drawShadow(matrixStack, displayString, textPosX, textPosY, drawColor);
            GL11.glPopMatrix();
        }
        else
        {
            client.font.drawShadow(matrixStack, displayString, textPosX, textPosY, drawColor);
        }
    }
}

