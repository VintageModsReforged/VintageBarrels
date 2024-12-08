package vintage.mods.barrels.client;

import com.google.common.primitives.SignedBytes;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import vintage.mods.barrels.BarrelType;
import vintage.mods.barrels.tiles.TileEntityBarrel;

import java.util.Random;

import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

public class TileEntityBarrelRenderer extends TileEntitySpecialRenderer {

    private Random random;

    private RenderItem itemRenderer = new RenderItem() {
        public byte getMiniBlockCount(ItemStack stack) {
            return SignedBytes.saturatedCast((long)(Math.min(stack.stackSize / 32, 15) + 1));
        }

        public byte getMiniItemCount(ItemStack stack) {
            return SignedBytes.saturatedCast((long)(Math.min(stack.stackSize / 32, 7) + 1));
        }

        public boolean shouldBob() {
            return false;
        }

        public boolean shouldSpreadItems() {
            return false;
        }
    };

    private static float[][] shifts = {{0.3F, 0.45F, 0.3F}, {0.7F, 0.45F, 0.3F}, {0.3F, 0.45F, 0.7F}, {0.7F, 0.45F, 0.7F}, {0.3F, 0.1F, 0.3F},
            {0.7F, 0.1F, 0.3F}, {0.3F, 0.1F, 0.7F}, {0.7F, 0.1F, 0.7F}, {0.5F, 0.32F, 0.5F},};

    public TileEntityBarrelRenderer() {
        random = new Random();
        itemRenderer.setRenderManager(RenderManager.instance);
    }

    public void render(TileEntityBarrel tile, double x, double y, double z) {
        if (tile == null) {
            return;
        }

        BarrelType type = tile.getType();
        if (tile.getWorldObj() != null) {
            int typ = tile.getWorldObj().getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord);
            type = BarrelType.values()[typ];
        }
        glPushMatrix();
        glEnable(32826 /* GL_RESCALE_NORMAL_EXT */);
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        glTranslatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
        glScalef(1.0F, -1F, -1F);
        glTranslatef(0.5F, 0.5F, 0.5F);
        glTranslatef(-0.5F, -0.5F, -0.5F);
        glDisable(32826 /* GL_RESCALE_NORMAL_EXT */);
        glPopMatrix();
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (type.isTransparent() && tile.getDistanceFrom(this.tileEntityRenderer.playerX, this.tileEntityRenderer.playerY, this.tileEntityRenderer.playerZ) < 128d) {
            random.setSeed(254L);
            float shiftX;
            float shiftY;
            float shiftZ;
            int shift = 0;
            float blockScale = 0.70F;
            float timeD = (float) (360.0 * (double) (System.currentTimeMillis() & 0x3FFFL) / (double) 0x3FFFL);
            if (tile.getTopItemStacks()[1] == null) {
                shift = 8;
                blockScale = 0.85F;
            }
            glPushMatrix();
            glDisable(2896 /* GL_LIGHTING */);
            glTranslatef((float) x, (float) y, (float) z);
            EntityItem customitem = new EntityItem(tileEntityRenderer.worldObj);
            customitem.hoverStart = 0f;
            for (ItemStack item : tile.getTopItemStacks()) {
                if (shift > shifts.length) {
                    break;
                }
                if (item == null) {
                    ++shift;
                } else {
                    shiftX = shifts[shift][0];
                    shiftY = shifts[shift][1];
                    shiftZ = shifts[shift][2];
                    shift++;
                    glPushMatrix();
                    glTranslatef(shiftX, shiftY, shiftZ);
                    glRotatef(timeD, 0.0F, 1.0F, 0.0F);
                    glScalef(blockScale, blockScale, blockScale);
                    customitem.setEntityItemStack(item);
                    itemRenderer.doRenderItem(customitem, 0, 0, 0, 0, 0);
                    glPopMatrix();
                }
            }
            glEnable(2896 /* GL_LIGHTING */);
            glPopMatrix();
            glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTick) {
        render((TileEntityBarrel) tileentity, x, y, z);
    }
}
