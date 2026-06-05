package Exy.relivingthemoment.client.guistuff;

import Exy.relivingthemoment.Relivingthemoment;
import ladysnake.satin.api.managed.ManagedCoreShader;
import ladysnake.satin.api.managed.ShaderEffectManager;
import ladysnake.satin.api.managed.uniform.Uniform1f;
import ladysnake.satin.api.managed.uniform.Uniform1i;
import ladysnake.satin.api.managed.uniform.Uniform2f;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public class BeltToolTipShader {
    public final ManagedCoreShader shader;
    public final Uniform1f time;
    public final Uniform1i type;
    public final Uniform2f UScreenSize;

    public BeltToolTipShader(String subfix) {
        this.shader = ShaderEffectManager.getInstance().manageCoreShader(new Identifier(Relivingthemoment.MODID,"beltbar_"+subfix), VertexFormats.POSITION_TEXTURE);
        this.time = this.shader.findUniform1f("UTime");
        this.type = this.shader.findUniform1i("UType");
        this.UScreenSize = this.shader.findUniform2f("UScreenSize");
    }
}
