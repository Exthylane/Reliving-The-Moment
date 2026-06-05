package Exy.relivingthemoment.mixin.client;

import Exy.relivingthemoment.client.playerrenderstuff.WretchLeatherBeltFeatureRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityInjector {
    @Inject(method = "<init>",at = @At("TAIL"))
    private void inject(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
        ((PlayerEntityRenderer)(Object)this).addFeature(new WretchLeatherBeltFeatureRenderer<>(((PlayerEntityRenderer)(Object)this), ctx.getModelLoader()));
    }
}
