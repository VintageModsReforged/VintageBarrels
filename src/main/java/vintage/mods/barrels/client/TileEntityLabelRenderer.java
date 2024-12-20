package vintage.mods.barrels.client;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import vintage.mods.barrels.Refs;
import vintage.mods.barrels.VintageBarrelsConfig;
import vintage.mods.barrels.tiles.TileEntityLabel;

public class TileEntityLabelRenderer extends TileEntitySpecialRenderer {

    private ModelLabel labelModel = new ModelLabel();
    private int labelAngle;
    private int degreeAngle;
    private float ichange1;
    private float ichange2;
    private float ichange3;
    private float kchange1;
    private float kchange2;
    private float kchange3;
    private CustomItemRender itemRenderer;

    public TileEntityLabelRenderer() {}

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double i, double j, double k, float tick) {
        TileEntityLabel labelTile = (TileEntityLabel)tileEntity;
        this.labelAngle = labelTile.getAngle();
        switch (this.labelAngle) {
            case 0:
                this.degreeAngle = 270;
                this.ichange1 = 0.97F;
                this.ichange2 = 0.97F;
                this.ichange3 = 0.97F;
                this.kchange1 = 0.35F;
                this.kchange2 = 0.65F;
                this.kchange3 = 0.5F;
                break;
            case 1:
                this.degreeAngle = 180;
                this.ichange1 = 0.65F;
                this.ichange2 = 0.35F;
                this.ichange3 = 0.5F;
                this.kchange1 = 0.97F;
                this.kchange2 = 0.97F;
                this.kchange3 = 0.97F;
                break;
            case 2:
                this.degreeAngle = 90;
                this.ichange1 = 0.03F;
                this.ichange2 = 0.03F;
                this.ichange3 = 0.03F;
                this.kchange1 = 0.65F;
                this.kchange2 = 0.35F;
                this.kchange3 = 0.5F;
                break;
            case 3:
                this.degreeAngle = 0;
                this.ichange1 = 0.35F;
                this.ichange2 = 0.65F;
                this.ichange3 = 0.5F;
                this.kchange1 = 0.03F;
                this.kchange2 = 0.03F;
                this.kchange3 = 0.03F;
        }

        this.itemRenderer = new CustomItemRender();
        this.itemRenderer.setRenderManager(RenderManager.instance);

        int texNum = tileEntity.getBlockMetadata();
        GL11.glPushMatrix();
        switch (this.labelAngle) {
            case 0:
                GL11.glTranslated(i + (double)0.99F, j + (double)0.2F, k + (double)0.5F);
                break;
            case 1:
                GL11.glTranslated(i + (double)0.5F, j + (double)0.2F, k + (double)0.99F);
                break;
            case 2:
                GL11.glTranslated(i + (double)0.01F, j + (double)0.2F, k + (double)0.5F);
                break;
            case 3:
                GL11.glTranslated(i + (double)0.5F, j + (double)0.2F, k + (double)0.01F);
        }

        GL11.glRotatef((float)this.degreeAngle, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        this.bindTextureByName(this.getModelTexture(texNum));
        float scale = 0.03125F;
        this.labelModel.renderLabel(scale);
        GL11.glEnable(2896);
        ItemStack slot1 = labelTile.getLabelStack(1);
        ItemStack slot2 = labelTile.getLabelStack(0);
        ItemStack slot3 = labelTile.getLabelStack(2);
        if (slot1 != null) {
            String text = slot1.getDisplayName();
            this.renderText(text);
        }

        if (slot2 != null) {
            String text = slot2.getDisplayName();
            this.renderText(text);
        }

        if (slot3 != null) {
            String text = slot3.getDisplayName();
            this.renderText(text);
        }

        GL11.glPopMatrix();
        boolean rotateItemInsideLabel = VintageBarrelsConfig.ROTATE_ITEM_INSIDE_LABEL;
        if (slot1 != null) {
            EntityItem slot1Entity = new EntityItem(null, 0.0F, 0.0F, 0.0F, slot1);
            slot1Entity.hoverStart = 0.0F;
            GL11.glPushMatrix();
            GL11.glTranslated(i + (double)this.ichange1, j + 0.22, k + (double)this.kchange1);
            if (Refs.isBlock(slot1)) {
                GL11.glRotatef((float)this.degreeAngle, 0.0F, 1.0F, 0.0F);
            } else if (!rotateItemInsideLabel) {
                GL11.glRotatef((float)(this.degreeAngle + 180), 0.0F, 1.0F, 0.0F);
            }

            if (Refs.isBlock(slot1)) {
                GL11.glTranslated(0.0F, 0.04F, 0.0F);
                GL11.glScalef(0.45F, 0.45F, 0.45F);
            } else {
                GL11.glScalef(0.35F, 0.35F, 0.35F);
            }

            this.itemRenderer.doRenderItem(slot1Entity, 0.0F, 0.0F, 0.0F, 0.0F, rotateItemInsideLabel);
            GL11.glPopMatrix();
        }

        if (slot2 != null) {
            EntityItem slot2Entity = new EntityItem(null, 0.0F, 0.0F, 0.0F, slot2);
            slot2Entity.hoverStart = 0.0F;
            GL11.glPushMatrix();
            GL11.glTranslated(i + (double)this.ichange3, j + (double)0.14F, k + (double)this.kchange3);
            if (Refs.isBlock(slot2)) {
                GL11.glRotatef((float)this.degreeAngle, 0.0F, 1.0F, 0.0F);
            } else if (!rotateItemInsideLabel) {
                GL11.glRotatef((float)(this.degreeAngle + 180), 0.0F, 1.0F, 0.0F);
            }

            if (slot1 == null && slot3 == null) {
                GL11.glScalef(0.8F, 0.8F, 0.8F);
                if (Refs.isBlock(slot2)) {
                    GL11.glScalef(1.0F, 1.0F, 1.0F);
                    GL11.glTranslated(0.0F, 0.06F, 0.0F);
                } else {
                    GL11.glScalef(0.75F, 0.75F, 0.75F);
                }
            } else if (Refs.isBlock(slot2)) {
                GL11.glScalef(0.45F, 0.45F, 0.45F);
            } else {
                GL11.glScalef(0.35F, 0.35F, 0.35F);
            }

            if (slot1 != null && Refs.isBlock(slot2) || slot3 != null && Refs.isBlock(slot2)) {
                GL11.glTranslated(0.0F, 0.01F, 0.0F);
            }

            this.itemRenderer.doRenderItem(slot2Entity, 0.0F, 0.0F, 0.0F, 0.0F, rotateItemInsideLabel);
            GL11.glPopMatrix();
        }

        if (slot3 != null) {
            EntityItem slot3Entity = new EntityItem(null, 0.0F, 0.0F, 0.0F, slot3);
            slot3Entity.hoverStart = 0.0F;
            GL11.glPushMatrix();
            GL11.glTranslated(i + (double)this.ichange2, j + (double)0.22F, k + (double)this.kchange2);
            if (rotateItemInsideLabel && Refs.isBlock(slot3)) {
                GL11.glRotatef((float)this.degreeAngle, 0.0F, 1.0F, 0.0F);
            } else if (!rotateItemInsideLabel) {
                GL11.glRotatef((float)(this.degreeAngle + 180), 0.0F, 1.0F, 0.0F);
            }

            if (Refs.isBlock(slot3)) {
                GL11.glTranslated(0.0F, 0.05F, 0.0F);
                GL11.glScalef(0.45F, 0.45F, 0.45F);
            } else {
                GL11.glScalef(0.35F, 0.35F, 0.35F);
            }

            this.itemRenderer.doRenderItem(slot3Entity, 0.0F, 0.0F, 0.0F, 0.0F, rotateItemInsideLabel);
            GL11.glPopMatrix();
        }

    }

    public void renderText(String text) {
        FontRenderer fontRender = this.getFontRenderer();
        GL11.glDepthMask(false);
        GL11.glScalef(0.005F, 0.005F, 0.005F);
        GL11.glTranslated(0.0F, -25.0F, -6.5F);
        int adjust = fontRender.getStringWidth(text) / 2;
        fontRender.drawString(text, -adjust, -8, VintageBarrelsConfig.TEXT_COLOR, VintageBarrelsConfig.TEXT_SHADOW);
        GL11.glDepthMask(true);
    }

    public String getModelTexture(int metadata) {
        switch (metadata) {
            case 0:
                return Refs.WOODLABEL0_PNG;
            case 1:
                return Refs.WOODLABEL1_PNG;
            case 2:
                return Refs.WOODLABEL2_PNG;
            case 3:
                return Refs.WOODLABEL3_PNG;
            default:
                return Refs.WOODLABEL0_PNG;
        }
    }
}
