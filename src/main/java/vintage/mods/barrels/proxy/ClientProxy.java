package vintage.mods.barrels.proxy;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import vintage.mods.barrels.BarrelType;
import vintage.mods.barrels.VintageBarrelsConfig;
import vintage.mods.barrels.client.*;
import vintage.mods.barrels.tiles.TileEntityBarrel;
import vintage.mods.barrels.tiles.TileEntityLabel;

public class ClientProxy extends CommonProxy {

    public static int woodenlabelrenderID;

    @Override
    public void registerRenderInformation() {
        MinecraftForgeClient.preloadTexture("/mods/vintagebarrels/textures/block_textures.png");
        MinecraftForgeClient.preloadTexture("/mods/vintagebarrels/textures/item_textures.png");
    }

    @Override
    public void init() {
        super.init();
        woodenlabelrenderID = RenderingRegistry.getNextAvailableRenderId();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLabel.class, new TileEntityLabelRenderer());
        MinecraftForgeClient.registerItemRenderer(VintageBarrelsConfig.WOOD_LABEL_ID, new ItemLabelRenderer());
    }

    @Override
    public void registerTileEntitySpecialRenderer(BarrelType typ) {
        ClientRegistry.bindTileEntitySpecialRenderer(typ.clazz, new TileEntityBarrelRenderer());
    }

    @Override
    public World getClientWorld() {
        return FMLClientHandler.instance().getClient().theWorld;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityBarrel) {
            return GUIChest.GuiType.buildGUI(BarrelType.values()[ID], player.inventory, (TileEntityBarrel) tileEntity);
        } else if (tileEntity instanceof TileEntityLabel) {
            TileEntityLabel tileEntityLabel = (TileEntityLabel) tileEntity;
            return new GuiLabel(player.inventory, tileEntityLabel);
        } else return null;
    }
}
