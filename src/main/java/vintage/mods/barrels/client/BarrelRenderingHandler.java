package vintage.mods.barrels.client;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import vintage.mods.barrels.tiles.TileEntityBarrel;

@SideOnly(Side.CLIENT)
public class BarrelRenderingHandler implements ISimpleBlockRenderingHandler {

    public static final int renderId = RenderingRegistry.getNextAvailableRenderId();
    public final TileEntityBarrel barrel = new TileEntityBarrel();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks) {
        renderBlocks.uvRotateNorth = 1;
        renderBlocks.uvRotateSouth = 1;
        Tessellator tessellator = Tessellator.instance;
        block.setBlockBoundsForItemRender();
        renderBlocks.setRenderBoundsFromBlock(block);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderBlocks.renderFaceYPos(block, 0.0, 0.0, 0.0, renderBlocks.getBlockIconFromSideAndMetadata(block, 0, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderBlocks.renderFaceYPos(block, 0.0, 0.0, 0.0, renderBlocks.getBlockIconFromSideAndMetadata(block, 1, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderBlocks.renderFaceZNeg(block, 0.0, 0.0, 0.0, renderBlocks.getBlockIconFromSideAndMetadata(block, 2, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderBlocks.renderFaceZPos(block, 0.0, 0.0, 0.0, renderBlocks.getBlockIconFromSideAndMetadata(block, 3, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderBlocks.renderFaceXNeg(block, 0.0, 0.0, 0.0, renderBlocks.getBlockIconFromSideAndMetadata(block, 4, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderBlocks.renderFaceXPos(block, 0.0, 0.0, 0.0, renderBlocks.getBlockIconFromSideAndMetadata(block, 5, metadata));
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        renderBlocks.uvRotateNorth = 0; // reset the rotation so other blocks aren't affected
        renderBlocks.uvRotateSouth = 0;
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess iBlockAccess, int x, int y, int z, Block block, int modelID, RenderBlocks renderBlocks) {
        TileEntity blockEntity = iBlockAccess.getBlockTileEntity(x, y, z);
        if (blockEntity instanceof TileEntityBarrel) {
            TileEntityBarrel barrelEntity = (TileEntityBarrel) blockEntity;
            short facing = barrelEntity.getFacing();
            renderBlocks.clearOverrideBlockTexture();
            switch (facing) {
                case 0: // Bottom
                    renderBlocks.uvRotateNorth = 0;
                    renderBlocks.uvRotateSouth = 0;
                    renderBlocks.uvRotateWest = 0;
                    renderBlocks.uvRotateEast = 0;
                    break;
                case 1: //Top
                    renderBlocks.uvRotateNorth = 0;
                    renderBlocks.uvRotateSouth = 0;
                    renderBlocks.uvRotateWest = 0;
                    renderBlocks.uvRotateEast = 0;
                    break;
                case 2: //North
                    renderBlocks.uvRotateTop = 0;
                    renderBlocks.uvRotateBottom = 0;
                    renderBlocks.uvRotateNorth = 1;
                    renderBlocks.uvRotateSouth = 1;

                    break;
                case 3: //South
                    renderBlocks.uvRotateTop = 0;
                    renderBlocks.uvRotateBottom = 0;
                    renderBlocks.uvRotateNorth = 1;
                    renderBlocks.uvRotateSouth = 1;
                    renderBlocks.flipTexture = true; //east
                    break;
                case 4: //West
                    renderBlocks.uvRotateTop = 1;
                    renderBlocks.uvRotateBottom = 1;
                    renderBlocks.uvRotateWest = 1;
                    renderBlocks.uvRotateEast = 1;
                    break;
                case 5: //East
                    renderBlocks.uvRotateTop = 1;
                    renderBlocks.uvRotateBottom = 1;
                    renderBlocks.uvRotateWest = 1;
                    renderBlocks.uvRotateEast = 1;
                    renderBlocks.flipTexture = true;
                    break;
            }
        }

        boolean flag = renderBlocks.renderStandardBlock(block, x, y, z);
        renderBlocks.flipTexture = false;
        renderBlocks.uvRotateNorth = 0;
        renderBlocks.uvRotateSouth = 0;
        renderBlocks.uvRotateWest = 0;
        renderBlocks.uvRotateEast = 0;
        renderBlocks.uvRotateTop = 0;
        renderBlocks.uvRotateBottom = 0;
        return flag;
    }

    @Override
    public boolean shouldRender3DInInventory() {
        return true;
    }

    @Override
    public int getRenderId() {
        return renderId;
    }
}
