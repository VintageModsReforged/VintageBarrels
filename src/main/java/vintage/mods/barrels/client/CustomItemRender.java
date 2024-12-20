package vintage.mods.barrels.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class CustomItemRender extends RenderItem {

    private final Random random = new Random();
    private final RenderBlocks itemRenderBlocks = new RenderBlocks();

    public void doRenderItem(EntityItem item, double x, double y, double z, float partialTicks, boolean rotation) {
        this.random.setSeed(187L);
        ItemStack itemstack = item.getEntityItem();
        if (itemstack.getItem() != null) {
            GL11.glPushMatrix();
            float f2 = this.shouldBob() ? MathHelper.sin(((float)item.age + partialTicks) / 10.0F + item.hoverStart) * 0.1F + 0.1F : 0.0F;
            float f3 = (((float)item.age + partialTicks) / 20.0F + item.hoverStart) * (180F / (float)Math.PI);
            byte b0 = this.getMiniBlockCount(itemstack);
            GL11.glTranslatef((float)x, (float)y + f2, (float)z);
            GL11.glEnable(32826);
            Block block = null;
            if (itemstack.itemID < Block.blocksList.length) {
                block = Block.blocksList[itemstack.itemID];
            }

            if (!ForgeHooksClient.renderEntityItem(item, itemstack, f2, f3, this.random, this.renderManager.renderEngine, this.renderBlocks)) {
                if (itemstack.getItemSpriteNumber() == 0 && block != null && RenderBlocks.renderItemIn3d(Block.blocksList[itemstack.itemID].getRenderType())) {
                    GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
                    if (renderInFrame) {
                        GL11.glScalef(1.25F, 1.25F, 1.25F);
                        GL11.glTranslatef(0.0F, 0.05F, 0.0F);
                        GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                    }

                    this.loadTexture("/terrain.png");
                    float f7 = 0.25F;
                    int j = block.getRenderType();
                    if (j == 1 || j == 19 || j == 12 || j == 2) {
                        f7 = 0.5F;
                    }

                    GL11.glScalef(f7, f7, f7);

                    for(int i = 0; i < b0; ++i) {
                        GL11.glPushMatrix();
                        if (i > 0) {
                            float f5 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f7;
                            float f4 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f7;
                            float f6 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f7;
                            GL11.glTranslatef(f5, f4, f6);
                        }

                        float f5 = 1.0F;
                        this.itemRenderBlocks.renderBlockAsItem(block, itemstack.getItemDamage(), f5);
                        GL11.glPopMatrix();
                    }
                } else if (itemstack.getItem().requiresMultipleRenderPasses()) {
                    if (renderInFrame) {
                        GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                        GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                    } else {
                        GL11.glScalef(0.5F, 0.5F, 0.5F);
                    }

                    this.loadTexture("/gui/items.png");

                    for(int k = 0; k < itemstack.getItem().getRenderPasses(itemstack.getItemDamage()); ++k) {
                        this.random.setSeed(187L);
                        Icon icon = itemstack.getItem().getIcon(itemstack, k);
                        float f8 = 1.0F;
                        if (this.renderWithColor) {
                            int i = Item.itemsList[itemstack.itemID].getColorFromItemStack(itemstack, k);
                            float f5 = (float)(i >> 16 & 255) / 255.0F;
                            float f4 = (float)(i >> 8 & 255) / 255.0F;
                            float f6 = (float)(i & 255) / 255.0F;
                            GL11.glColor4f(f5 * f8, f4 * f8, f6 * f8, 1.0F);
                            this.renderItem(item, icon, partialTicks, f5 * f8, f4 * f8, f6 * f8, rotation);
                        } else {
                            this.renderItem(item, icon, partialTicks, 1.0F, 1.0F, 1.0F, rotation);
                        }
                    }
                } else {
                    if (renderInFrame) {
                        GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                        GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                    } else {
                        GL11.glScalef(0.5F, 0.5F, 0.5F);
                    }

                    Icon icon1 = itemstack.getIconIndex();
                    if (itemstack.getItemSpriteNumber() == 0) {
                        this.loadTexture("/terrain.png");
                    } else {
                        this.loadTexture("/gui/items.png");
                    }

                    if (this.renderWithColor) {
                        int l = Item.itemsList[itemstack.itemID].getColorFromItemStack(itemstack, 0);
                        float f8 = (float)(l >> 16 & 255) / 255.0F;
                        float f9 = (float)(l >> 8 & 255) / 255.0F;
                        float f5 = (float)(l & 255) / 255.0F;
                        float f4 = 1.0F;
                        this.renderItem(item, icon1, partialTicks, f8 * f4, f9 * f4, f5 * f4, rotation);
                    } else {
                        this.renderItem(item, icon1, partialTicks, 1.0F, 1.0F, 1.0F, rotation);
                    }
                }
            }

            GL11.glDisable(32826);
            GL11.glPopMatrix();
        }
    }

    public void renderItem(EntityItem item, Icon icon, float xScale, float yScale, float zScale, float partialTicks, boolean rotation) {
        Tessellator tessellator = Tessellator.instance;
        if (icon == null) {
            icon = this.renderManager.renderEngine.getMissingIcon(item.getEntityItem().getItemSpriteNumber());
        }
        float f4 = icon.getMinU();
        float f5 = icon.getMaxU();
        float f6 = icon.getMinV();
        float f7 = icon.getMaxV();
        float f9 = 0.5F;
        float f10 = 0.25F;
        GL11.glPushMatrix();
        if (rotation) {
            GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        } else GL11.glRotatef((((float)item.age + xScale) / 20.0F + item.hoverStart) * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);


        float f12 = 0.0625F;
        float f11 = 0.021875F;
        ItemStack stack = item.getEntityItem();
        byte b0 = this.getMiniItemCount(stack);
        GL11.glTranslatef(-f9, -f10, -((f12 + f11) * (float)b0 / 2.0F));

        for(int k = 0; k < b0; ++k) {
            if (k > 0 && this.shouldSpreadItems()) {
                float x = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                float y = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                float z = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                GL11.glTranslatef(x, y, f12 + f11);
            } else {
                GL11.glTranslatef(0.0F, 0.0F, f12 + f11);
            }

            if (stack.getItemSpriteNumber() == 0) {
                this.loadTexture("/terrain.png");
            } else {
                this.loadTexture("/gui/items.png");
            }

            GL11.glColor4f(yScale, zScale, partialTicks, 1.0F);
            ItemRenderer.renderItemIn2D(tessellator, f5, f6, f4, f7, icon.getSheetWidth(), icon.getSheetHeight(), f12);
            if (stack.hasEffect()) {
                GL11.glDepthFunc(514);
                GL11.glDisable(2896);
                this.renderManager.renderEngine.bindTexture("%blur%/misc/glint.png");
                GL11.glEnable(3042);
                GL11.glBlendFunc(768, 1);
                float f13 = 0.76F;
                GL11.glColor4f(0.5F * f13, 0.25F * f13, 0.8F * f13, 1.0F);
                GL11.glMatrixMode(5890);
                GL11.glPushMatrix();
                float f14 = 0.125F;
                GL11.glScalef(f14, f14, f14);
                float f15 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
                GL11.glTranslatef(f15, 0.0F, 0.0F);
                GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
                ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, f12);
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glScalef(f14, f14, f14);
                f15 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
                GL11.glTranslatef(-f15, 0.0F, 0.0F);
                GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
                ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, f12);
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
    public byte getMiniBlockCount(ItemStack stack) {
        return 1;
    }

    @Override
    public byte getMiniItemCount(ItemStack stack) {
        return 1;
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
