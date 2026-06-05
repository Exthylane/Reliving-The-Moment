package Exy.relivingthemoment.client.playerrenderstuff;

import Exy.relivingthemoment.Relivingthemoment;
import Exy.relivingthemoment.WretchLeatherBeltItem;
import Exy.relivingthemoment.client.RelivingthemomentClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class WretchLeatherBeltFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {

    private static final Identifier SKIN = new Identifier(Relivingthemoment.MODID, "textures/entity/wretchleatherbeltfeature.png");
    private final WretchLeatherBeltModel belt;

    public WretchLeatherBeltFeatureRenderer(FeatureRendererContext<T, M> context, EntityModelLoader loader) {
        super(context);
        this.belt = new WretchLeatherBeltModel(loader.getModelPart(RelivingthemomentClient.WretchLeatherBeltModelLayer));
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {

        ItemStack beltStack = WretchLeatherBeltItem.getWornBelt(livingEntity);
        if (!(beltStack.getItem() instanceof WretchLeatherBeltItem beltItem)) return;
        int totemCount = beltItem.getItems(beltStack).stream().filter(s -> s.isOf(Items.TOTEM_OF_UNDYING)).mapToInt(ItemStack::getCount).sum();

        belt.setTotemCount(totemCount);
        Vec3d vel = livingEntity.getVelocity();

        float yawRad = (float) Math.toRadians(livingEntity.getBodyYaw());
        double localX =  vel.x * Math.cos(yawRad) + vel.z * Math.sin(yawRad);
        double localZ = -vel.x * Math.sin(yawRad) + vel.z * Math.cos(yawRad);
        belt.applyVelocity(localX, vel.y, localZ);

        matrixStack.push();

        belt.setAngles(livingEntity, f, g, j, k, l);

        belt.render(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getArmorCutoutNoCull(SKIN)), i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
        matrixStack.pop();
    }
}