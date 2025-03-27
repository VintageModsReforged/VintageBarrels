package vintage.mods.barrels;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class Refs {

    public static final String ID = "vintagebarrels";
    public static final String NAME = "Vintage Barrels";
    public static final String VERSION = "1.5.2-1.0.7.2";
    public static final String DEPS = "required-after:VintageCore;after:inventorytweaks;";
    public static final String MC_VERSION = "[1.5.2]";

    public static final String PROXY_CLIENT = "vintage.mods.barrels.proxy.ClientProxy";
    public static final String PROXY_COMMON = "vintage.mods.barrels.proxy.CommonProxy";

    public static String WOODLABELGUI_PNG = "/mods/vintagebarrels/textures/gui/label.png";
    public static String WOODLABEL0_PNG = "/mods/vintagebarrels/textures/models/label.oak.png";
    public static String WOODLABEL1_PNG = "/mods/vintagebarrels/textures/models/label.spruce.png";
    public static String WOODLABEL2_PNG = "/mods/vintagebarrels/textures/models/label.birch.png";
    public static String WOODLABEL3_PNG = "/mods/vintagebarrels/textures/models/label.jungle.png";

    public static boolean isBlock(ItemStack stack) {
        String itemName = stack.toString();
        return !itemName.contains("item") && !itemName.contains("null");
    }

    public static NBTTagCompound getOrCreateTag(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound("tag");
            stack.setTagCompound(tag);
        }
        return tag;
    }
}
