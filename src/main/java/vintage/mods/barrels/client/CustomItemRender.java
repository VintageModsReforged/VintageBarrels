package vintage.mods.barrels.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class CustomItemRender extends RenderItem {

    private final RenderBlocks itemRenderBlocks = new RenderBlocks();
    private final Random random = new Random();

    public void doRenderItem(EntityItem item, double x, double y, double z, float partialTicks, boolean rotation) {
        this.random.setSeed(187L);
        ItemStack stack = item.getEntityItem();
        if (stack.getItem() != null) {
            GL11.glPushMatrix();
            float var11 = this.shouldBob() ? MathHelper.sin(((float) item.age + partialTicks) / 10.0F + item.hoverStart) * 0.1F + 0.1F : 0.0F;
            float var12 = (((float) item.age + partialTicks) / 20.0F + item.hoverStart) * (180F / (float) Math.PI);
            byte var13 = this.getMiniBlockCountForItemStack(stack);
            GL11.glTranslatef((float) x, (float) y + var11, (float) z);
            GL11.glEnable(32826);
            if (!ForgeHooksClient.renderEntityItem(item, stack, var11, var12, this.random, this.renderManager.renderEngine, this.renderBlocks)) {
                if (stack.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.blocksList[stack.itemID].getRenderType())) {
                    GL11.glRotatef(var12, 0.0F, 1.0F, 0.0F);
                    if (field_82407_g) {
                        GL11.glScalef(1.25F, 1.25F, 1.25F);
                        GL11.glTranslatef(0.0F, 0.05F, 0.0F);
                        GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                    }

                    this.loadTexture(Block.blocksList[stack.itemID].getTextureFile());
                    float var22 = 0.25F;
                    int var16 = Block.blocksList[stack.itemID].getRenderType();
                    if (var16 == 1 || var16 == 19 || var16 == 12 || var16 == 2) {
                        var22 = 0.5F;
                    }

                    GL11.glScalef(var22, var22, var22);

                    for (int var23 = 0; var23 < var13; ++var23) {
                        GL11.glPushMatrix();
                        if (var23 > 0) {
                            float var24 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var22;
                            float var19 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var22;
                            float var20 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var22;
                            GL11.glTranslatef(var24, var19, var20);
                        }

                        float var24 = 1.0F;
                        this.itemRenderBlocks.renderBlockAsItem(Block.blocksList[stack.itemID], stack.getItemDamage(), var24);
                        GL11.glPopMatrix();
                    }
                } else if (stack.getItem().requiresMultipleRenderPasses()) {
                    if (field_82407_g) {
                        GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                        GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                    } else {
                        GL11.glScalef(0.5F, 0.5F, 0.5F);
                    }

                    for (int var15 = 0; var15 < stack.getItem().getRenderPasses(stack.getItemDamage()); ++var15) {
                        this.loadTexture(Item.itemsList[stack.itemID].getTextureFile());
                        this.random.setSeed(187L);
                        int var16 = stack.getItem().getIconIndex(stack, var15);
                        float var17 = 1.0F;
                        if (this.field_77024_a) {
                            int var18 = Item.itemsList[stack.itemID].getColorFromItemStack(stack, var15);
                            float var19 = (float) (var18 >> 16 & 255) / 255.0F;
                            float var20 = (float) (var18 >> 8 & 255) / 255.0F;
                            float var21 = (float) (var18 & 255) / 255.0F;
                            GL11.glColor4f(var19 * var17, var20 * var17, var21 * var17, 1.0F);
                            this.renderItem(item, var16, partialTicks, var19 * var17, var20 * var17, var21 * var17, rotation);
                        } else {
                            this.renderItem(item, var16, partialTicks, 1.0F, 1.0F, 1.0F, rotation);
                        }
                    }
                } else {
                    if (field_82407_g) {
                        GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                        GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                    } else {
                        GL11.glScalef(0.5F, 0.5F, 0.5F);
                    }

                    int var15 = stack.getIconIndex();
                    this.loadTexture(stack.getItem().getTextureFile());
                    if (this.field_77024_a) {
                        int var16 = Item.itemsList[stack.itemID].getColorFromItemStack(stack, 0);
                        float var17 = (float) (var16 >> 16 & 255) / 255.0F;
                        float var24 = (float) (var16 >> 8 & 255) / 255.0F;
                        float var19 = (float) (var16 & 255) / 255.0F;
                        float var20 = 1.0F;
                        this.renderItem(item, var15, partialTicks, var17 * var20, var24 * var20, var19 * var20, rotation);
                    } else {
                        this.renderItem(item, var15, partialTicks, 1.0F, 1.0F, 1.0F, rotation);
                    }
                }
            }

            GL11.glDisable(32826);
            GL11.glPopMatrix();
        }

    }

    private void renderItem(EntityItem item, int icon, float xScale, float yScale, float zScale, float partialTicks, boolean rotation) {
        Tessellator var8 = Tessellator.instance;
        float var9 = (float) (icon % 16 * 16) / 256.0F;
        float var10 = (float) (icon % 16 * 16 + 16) / 256.0F;
        float var11 = (float) (icon / 16 * 16) / 256.0F;
        float var12 = (float) (icon / 16 * 16 + 16) / 256.0F;
        float var14 = 0.5F;
        float var15 = 0.25F;
        GL11.glPushMatrix();
        if (rotation) {
            GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        } else GL11.glRotatef((((float) item.age + xScale) / 20.0F + item.hoverStart) * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);

        float var16 = 0.0625F;
        float var17 = 0.021875F;
        ItemStack stack = item.getEntityItem();
        byte var24 = this.getMiniItemCountForItemStack(stack);
        GL11.glTranslatef(-var14, -var15, -((var16 + var17) * (float) var24 / 2.0F));
        for (int var20 = 0; var20 < var24; ++var20) {
            if (var20 > 0 && this.shouldSpreadItems()) {
                float x = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                float y = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                GL11.glTranslatef(x, y, var16 + var17);
            } else {
                GL11.glTranslatef(0.0F, 0.0F, var16 + var17);
            }

            this.loadTexture(Item.itemsList[stack.itemID].getTextureFile());
            GL11.glColor4f(yScale, zScale, partialTicks, 1.0F);
            ItemRenderer.renderItemIn2D(var8, var10, var11, var9, var12, var16);
            if (stack.hasEffect()) {
                GL11.glDepthFunc(514);
                GL11.glDisable(2896);
                this.renderManager.renderEngine.bindTexture(this.renderManager.renderEngine.getTexture("%blur%/misc/glint.png"));
                GL11.glEnable(3042);
                GL11.glBlendFunc(768, 1);
                float var21 = 0.76F;
                GL11.glColor4f(0.5F * var21, 0.25F * var21, 0.8F * var21, 1.0F);
                GL11.glMatrixMode(5890);
                GL11.glPushMatrix();
                float var22 = 0.125F;
                GL11.glScalef(var22, var22, var22);
                float var23 = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
                GL11.glTranslatef(var23, 0.0F, 0.0F);
                GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
                ItemRenderer.renderItemIn2D(var8, 0.0F, 0.0F, 1.0F, 1.0F, var16);
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glScalef(var22, var22, var22);
                var23 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
                GL11.glTranslatef(-var23, 0.0F, 0.0F);
                GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
                ItemRenderer.renderItemIn2D(var8, 0.0F, 0.0F, 1.0F, 1.0F, 0.0625F);
                GL11.glPopMatrix();
                GL11.glMatrixMode(5888);
                GL11.glDisable(3042);
                GL11.glEnable(2896);
                GL11.glDepthFunc(515);
            }
        }
        GL11.glPopMatrix();
    }

    @Override
    public boolean shouldBob() {
        return false;
    }

    @Override
    public boolean shouldSpreadItems() {
        return false;
    }
}
