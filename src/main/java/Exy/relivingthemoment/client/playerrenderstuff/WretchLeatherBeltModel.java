package Exy.relivingthemoment.client.playerrenderstuff;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class WretchLeatherBeltModel extends EntityModel<Entity> {
	public final ModelPart emblem;
	public final ModelPart totem2;
	public final ModelPart totem1;
	public final ModelPart base;
	public final ModelPart totem3;

	private int totemCount = 0;

	private final float totem1OrigX, totem2OrigX, totem3OrigX, emblemOrigX;


	public WretchLeatherBeltModel(ModelPart root) {
		this.emblem = root.getChild("emblem");
		this.totem2 = root.getChild("totem2");
		this.totem1 = root.getChild("totem1");
		this.base   = root.getChild("base");
		this.totem3 = root.getChild("totem3");

		this.totem1OrigX = totem1.pivotX;
		this.totem2OrigX = totem2.pivotX;
		this.totem3OrigX = totem3.pivotX;
		this.emblemOrigX = emblem.pivotX;
	}

	public void setTotemCount(int count) {
		this.totemCount = count;
	}

	public void applyVelocity(double motionX, double motionY, double motionZ) {
		totem1.pivotX = totem1OrigX;
		totem2.pivotX = totem2OrigX;
		totem3.pivotX = totem3OrigX;
		emblem.pivotX = emblemOrigX;

		float pitchFromZ = (float) Math.max(-0.4, Math.min(0.4,  motionZ * 5.0));
		float pitchFromY = (float) Math.max(-0.3, Math.min(0.3, -motionY * 3.0));
		float pitch = pitchFromZ + pitchFromY;
		float sideShift = (float) Math.max(-0.3, Math.min(0.3, -motionX * 4.0));

		totem1.pitch = pitch;         totem1.pivotX = totem1OrigX + sideShift;
		totem2.pitch = pitch * 0.90f; totem2.pivotX = totem2OrigX + sideShift * 0.90f;
		totem3.pitch = pitch * 0.95f; totem3.pivotX = totem3OrigX + sideShift * 0.95f;
		emblem.pitch = pitch * 1.05f; emblem.pivotX = emblemOrigX + sideShift * 1.05f;
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();


		ModelPartData emblem = modelPartData.addChild("emblem", ModelPartBuilder.create(), ModelTransform.pivot(-3.76F, 11.0F, -2.807F));
		emblem.addChild("emblem_r1", ModelPartBuilder.create().uv(21, 26).cuboid(-2.0F, -1.0F, 0.0F, 4.0F, 6.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(7.52F, -1.0F, 0.0F, 0.0F, -0.2182F, 0.0F));

		ModelPartData totem1 = modelPartData.addChild("totem1", ModelPartBuilder.create(), ModelTransform.pivot(1.694F, 12.0F, 3.381F));
		totem1.addChild("totem1_r1", ModelPartBuilder.create().uv(1, 26).cuboid(-3.0F, -1.0F, 0.0F, 6.0F, 6.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-3.388F, 1.0F, 0.0F, 0.0F, -0.5672F, 0.0F));

		ModelPartData totem2 = modelPartData.addChild("totem2", ModelPartBuilder.create(), ModelTransform.pivot(3.694F, 13.0F, 3.381F));
		totem2.addChild("totem2_r1", ModelPartBuilder.create().uv(1, 26).cuboid(-3.0F, -1.0F, 0.0F, 6.0F, 6.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-7.388F, 0.0F, 0.0F, 0.0F, -0.5672F, 0.0F));

		ModelPartData base = modelPartData.addChild("base", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		base.addChild("cube_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-4.5F, -1.0F, -3.0F, 9.0F, 2.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0793F, -12.1152F, 0.0F, 0.0F, 0.0F, -0.3927F));

		ModelPartData totem3 = modelPartData.addChild("totem3", ModelPartBuilder.create(), ModelTransform.pivot(-0.306F, 11.0F, 3.381F));
		totem3.addChild("totem3_r1", ModelPartBuilder.create().uv(1, 26).cuboid(-3.0F, -1.0F, 0.0F, 6.0F, 6.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.612F, 1.0F, 0.0F, 0.0F, -0.5672F, 0.0F));

		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		base.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		emblem.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		if (totemCount >= 1) totem1.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		if (totemCount >= 2) totem2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		if (totemCount >= 3) totem3.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}