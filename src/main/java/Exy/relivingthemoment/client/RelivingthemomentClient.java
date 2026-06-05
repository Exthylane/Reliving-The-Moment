package Exy.relivingthemoment.client;

import Exy.relivingthemoment.RelivingTheMomentConfig;
import Exy.relivingthemoment.Relivingthemoment;
import Exy.relivingthemoment.SoulDebtStatusEffect;
import Exy.relivingthemoment.client.guistuff.BeltToolTipShader;
import Exy.relivingthemoment.client.playerrenderstuff.WretchLeatherBeltModel;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import ladysnake.satin.api.managed.uniform.Uniform1f;
import ladysnake.satin.api.managed.uniform.Uniform1i;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.joml.Vector2f;

public class RelivingthemomentClient implements ClientModInitializer {
    public static final BeltToolTipShader BELTBARSHADERTOOLTIP = new BeltToolTipShader("tooltip");

    public static final EntityModelLayer WretchLeatherBeltModelLayer = new EntityModelLayer(new Identifier(Relivingthemoment.MODID,"wretchleatherbelt"),"main");

    public static int time;

    public static ItemStack currentFocusItemStack = ItemStack.EMPTY;

    public static final ManagedShaderEffect SoulDebtShader = ShaderEffectManager.getInstance().manage(new Identifier(Relivingthemoment.MODID,"shaders/post/souldebt.json"));
    public static final Uniform1f Delta = SoulDebtShader.findUniform1f("UDelta");
    public static final Uniform1f shaderTime = SoulDebtShader.findUniform1f("UTime");
    public static final Uniform1i Mode = SoulDebtShader.findUniform1i("UType");


    @Override
    public void onInitializeClient() {

        ShaderEffectRenderCallback.EVENT.register(tickDelta -> {
            if (RelivingTheMomentConfig.mode) {
                Mode.set(1);
                BELTBARSHADERTOOLTIP.type.set(1);
            } else {
                Mode.set(0);
                BELTBARSHADERTOOLTIP.type.set(0);
            }
            if (MinecraftClient.getInstance().player != null) {
                float delta = SoulDebtStatusEffect.getDelta(MinecraftClient.getInstance().player);
                if (delta > 0.0f) {
                    Delta.set(delta);
                    shaderTime.set(time + tickDelta);
                    SoulDebtShader.render(tickDelta);
                }
            }
        });

        EntityModelLayerRegistry.registerModelLayer(WretchLeatherBeltModelLayer, WretchLeatherBeltModel::getTexturedModelData);

        ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {
            time += minecraftClient.isPaused() ? 0 : 1;
            BELTBARSHADERTOOLTIP.UScreenSize.set(new Vector2f(MinecraftClient.getInstance().getWindow().getFramebufferWidth(),MinecraftClient.getInstance().getWindow().getFramebufferHeight()));
        });

        WorldRenderEvents.LAST.register(worldRenderContext -> {
            if (MinecraftClient.getInstance().currentScreen instanceof HandledScreen screen) {
                if (screen.focusedSlot != null && screen.focusedSlot.getStack() != null) {
                    RelivingthemomentClient.currentFocusItemStack = screen.focusedSlot.getStack();
                } else {
                    RelivingthemomentClient.currentFocusItemStack = ItemStack.EMPTY;
                }
            }
        });



    }
}
