package vintage.mods.barrels.proxy;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vintage.mods.barrels.BarrelType;
import vintage.mods.barrels.blocks.container.ContainerBarrelBase;
import vintage.mods.barrels.tiles.TileEntityBarrel;

public class CommonProxy implements IGuiHandler {

    public void registerTileEntitySpecialRenderer(BarrelType typ) {}

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int X, int Y, int Z) {
        TileEntity tileEntity = world.getBlockTileEntity(X, Y, Z);
        if (tileEntity instanceof TileEntityBarrel) {
            TileEntityBarrel tileEntityBarrel = (TileEntityBarrel) tileEntity;
            return new ContainerBarrelBase(player.inventory, tileEntityBarrel, tileEntityBarrel.getType(), 0, 0);
        } else {
            return null;
        }
    }

    public World getClientWorld() {
        return null;
    }
}
