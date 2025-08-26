package vintage.mods.barrels.proxy;

import cpw.mods.fml.common.network.IGuiHandler;
import mods.vintage.core.platform.config.ConfigHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vintage.mods.barrels.*;
import vintage.mods.barrels.blocks.container.ContainerBarrelBase;
import vintage.mods.barrels.blocks.label.ContainerLabel;
import vintage.mods.barrels.tiles.TileEntityBarrel;
import vintage.mods.barrels.tiles.TileEntityLabel;

public class CommonProxy implements IGuiHandler {

    VintageBarrelsConfig CONFIG = new VintageBarrelsConfig();
    ConfigHandler configHandler = new ConfigHandler(Refs.ID);

    public void preInit() {
        configHandler.initIDs(CONFIG);
    }

    public void init() {
        configHandler.confirmIDs(CONFIG);
        BlocksItems.initBlocks();
    }

    public void postInit() {
        configHandler.confirmOwnership(CONFIG);
        VintageBarrelsRecipe.initRecipes();
    }

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
        } else if (tileEntity instanceof TileEntityLabel) {
            TileEntityLabel tileEntityLabel = (TileEntityLabel) tileEntity;
            return new ContainerLabel(player.inventory, tileEntityLabel);
        } else return null;
    }

    public World getClientWorld() {
        return null;
    }
}
