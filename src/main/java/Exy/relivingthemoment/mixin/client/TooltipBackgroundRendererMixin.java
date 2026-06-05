package Exy.relivingthemoment.mixin.client;

import Exy.relivingthemoment.Relivingthemoment;
import Exy.relivingthemoment.client.RelivingthemomentClient;
import Exy.relivingthemoment.client.guistuff.BeltToolTipShader;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import ladysnake.satin.api.managed.ManagedCoreShader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipBackgroundRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TooltipBackgroundRenderer.class)
public class TooltipBackgroundRendererMixin {

    @Unique
    private static final Identifier WHITE = new Identifier(Relivingthemoment.MODID,"textures/misc/white.png");


    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private static void render(DrawContext context, int x, int y, int width, int height, int z, CallbackInfo ci) {
        if (RelivingthemomentClient.currentFocusItemStack.isOf(Relivingthemoment.WretchLeatherBelt)) {
            int i = x - 3;
            int j = y - 3;
            int k = width + 3 + 3;
            int l = height + 3 + 3;
            drawIcon(WHITE, context.getMatrices(), i, j, k, l , z);
            ci.cancel();
        }
    }

    @Unique
    private static void drawIcon(Identifier tex, MatrixStack matrices, float x, float y, float width, float height, int z) {
        BeltToolTipShader s = RelivingthemomentClient.BELTBARSHADERTOOLTIP;
        s.time.set((float) RelivingthemomentClient.time + MinecraftClient.getInstance().getTickDelta());
        ManagedCoreShader coreShader = s.shader;

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);

        RenderSystem.setShader(coreShader::getProgram);
        RenderSystem.setShaderTexture(0, tex);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        float x1 = (x + width);
        float y1 = (y + height);
        bufferBuilder.vertex(matrix,  x, y1, z).texture(0.0F, 1.0F).next();
        bufferBuilder.vertex(matrix, x1, y1, z).texture(1.0F, 1.0F).next();
        bufferBuilder.vertex(matrix, x1,  y, z).texture(1.0F, 0.0F).next();
        bufferBuilder.vertex(matrix,  x,  y, z).texture(0.0F, 0.0F).next();
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());

        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
    }
}
