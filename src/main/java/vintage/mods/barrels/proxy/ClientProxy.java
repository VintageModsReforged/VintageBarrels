package vintage.mods.barrels.proxy;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.input.Keyboard;
import vintage.mods.barrels.BarrelType;
import vintage.mods.barrels.VintageBarrelsConfig;
import vintage.mods.barrels.client.*;
import vintage.mods.barrels.items.namingtool.GuiNamingTag;
import vintage.mods.barrels.items.namingtool.ItemNameTag;
import vintage.mods.barrels.tiles.TileEntityBarrel;
import vintage.mods.barrels.tiles.TileEntityLabel;

public class ClientProxy extends CommonProxy {

    public static int woodenlabelrenderID;

    @Override
    public void init() {
        super.init();
        woodenlabelrenderID = RenderingRegistry.getNextAvailableRenderId();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLabel.class, new TileEntityLabelRenderer());
        MinecraftForgeClient.registerItemRenderer(VintageBarrelsConfig.WOOD_LABEL_ID, new ItemLabelRenderer());
        RenderingRegistry.registerBlockHandler(new BarrelRenderingHandler());
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
        ItemStack equippedStack = player.getCurrentEquippedItem();
        if (ID == 10 && equippedStack.getItem() instanceof ItemNameTag) {
            return new GuiNamingTag();
        }
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityBarrel) {
            return GuiBarrel.GuiType.buildGUI(BarrelType.values()[ID], player.inventory, (TileEntityBarrel) tileEntity);
        } else if (tileEntity instanceof TileEntityLabel) {
            TileEntityLabel tileEntityLabel = (TileEntityLabel) tileEntity;
            return new GuiLabel(player.inventory, tileEntityLabel);
        } else return null;
    }

    public static boolean isShiftKeyDown() {
        return Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.keyCode);
    }
}
