package vintage.mods.barrels.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelLabel extends ModelBase {

    ModelRenderer back;
    ModelRenderer topEdge;
    ModelRenderer bottomEdge;
    ModelRenderer leftEdge;
    ModelRenderer rightEdge;

    public ModelLabel() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.back = new ModelRenderer(this, 7, 5);
        this.back.addBox(-7.0F, -4.0F, 0.0F, 14, 8, 1);
        this.back.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.back.setTextureSize(64, 32);
        this.back.mirror = true;
        this.setRotation(this.back, 0.0F, 0.0F, 0.0F);
        this.topEdge = new ModelRenderer(this, 4, 1);
        this.topEdge.addBox(-8.0F, -5.0F, -1.0F, 16, 1, 2);
        this.topEdge.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.topEdge.setTextureSize(64, 32);
        this.topEdge.mirror = true;
        this.setRotation(this.topEdge, 0.0F, 0.0F, 0.0F);
        this.bottomEdge = new ModelRenderer(this, 4, 16);
        this.bottomEdge.addBox(-8.0F, 4.0F, -1.0F, 16, 1, 2);
        this.bottomEdge.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bottomEdge.setTextureSize(64, 32);
        this.bottomEdge.mirror = true;
        this.setRotation(this.bottomEdge, 0.0F, 0.0F, 0.0F);
        this.leftEdge = new ModelRenderer(this, 0, 5);
        this.leftEdge.addBox(-8.0F, -4.0F, -1.0F, 1, 8, 2);
        this.leftEdge.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftEdge.setTextureSize(64, 32);
        this.leftEdge.mirror = true;
        this.setRotation(this.leftEdge, 0.0F, 0.0F, 0.0F);
        this.rightEdge = new ModelRenderer(this, 38, 5);
        this.rightEdge.addBox(7.0F, -4.0F, -1.0F, 1, 8, 2);
        this.rightEdge.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightEdge.setTextureSize(64, 32);
        this.rightEdge.mirror = true;
        this.setRotation(this.rightEdge, 0.0F, 0.0F, 0.0F);
    }

    public void renderLabel(float scale) {
        this.back.render(scale);
        this.topEdge.render(scale);
        this.bottomEdge.render(scale);
        this.leftEdge.render(scale);
        this.rightEdge.render(scale);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, (Entity)null);
    }
}
