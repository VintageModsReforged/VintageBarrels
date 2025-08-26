package vintage.mods.barrels.client;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import vintage.mods.barrels.tiles.TileEntityBarrel;

@SideOnly(Side.CLIENT)
public class BarrelRenderingHandler implements ISimpleBlockRenderingHandler {

    public static final int renderId = RenderingRegistry.getNextAvailableRenderId();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks) {
        renderBlocks.uvRotateNorth = 1;
        renderBlocks.uvRotateSouth = 1;

        Tessellator tessellator = Tessellator.instance;
        block.setBlockBoundsForItemRender();
        renderBlocks.setRenderBoundsFromBlock(block);

        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        float[][] normals = {
                { 0.0F, -1.0F,  0.0F}, // bottom
                { 0.0F,  1.0F,  0.0F}, // top
                { 0.0F,  0.0F, -1.0F}, // north
                { 0.0F,  0.0F,  1.0F}, // south
                {-1.0F,  0.0F,  0.0F}, // west
                { 1.0F,  0.0F,  0.0F}  // east
        };

        for (int side = 0; side < 6; side++) {
            tessellator.startDrawingQuads();
            tessellator.setNormal(normals[side][0], normals[side][1], normals[side][2]);

            Icon icon = renderBlocks.getBlockIconFromSideAndMetadata(block, side, metadata);
            switch (side) {
                case 0: renderBlocks.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, icon); break;
                case 1: renderBlocks.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, icon); break;
                case 2: renderBlocks.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, icon); break;
                case 3: renderBlocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, icon); break;
                case 4: renderBlocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, icon); break;
                case 5: renderBlocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, icon); break;
            }

            tessellator.draw();
        }

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);

        // reset rotation flags so other blocks aren't affected
        renderBlocks.uvRotateNorth = 0;
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
