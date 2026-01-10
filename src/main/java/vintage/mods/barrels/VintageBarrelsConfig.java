package vintage.mods.barrels;

import mods.vintage.core.helpers.ConfigHelper;
import mods.vintage.core.platform.config.ItemBlockID;
import mods.vintage.core.platform.lang.LocalizationProvider;
import net.minecraftforge.common.Configuration;

import java.util.Random;

@LocalizationProvider
public class VintageBarrelsConfig extends Configuration {

    @LocalizationProvider.List(modId = Refs.ID)
    public static String[] LANGUAGES;

    public static ItemBlockID BARREL_ID = ItemBlockID.ofBlock("barrel", 1076);
    public static ItemBlockID WOOD_LABEL_ID = ItemBlockID.ofBlock("woodLabel", 1077);
    public static ItemBlockID UPGRADE_ID = ItemBlockID.ofItem("upgrade", 19602);
    public static ItemBlockID TRANSPORTER_ID = ItemBlockID.ofItem("transporter", 19603);
    public static ItemBlockID NAMING_TOOL_ID = ItemBlockID.ofItem("namingTool", 19611);

    public static int TEXT_COLOR;
    public static boolean TEXT_SHADOW;
    public static boolean USE_CUSTOM_SOUNDS;
    public static boolean ROTATE_ITEM_INSIDE_LABEL;
    public static boolean RESTRICT_TRANSPORTATION;

    public VintageBarrelsConfig() {
        super(ConfigHelper.getConfigFileFor(Refs.ID));
        load();
        LANGUAGES = ConfigHelper.getLocalizations(this, new String[] { "en_US", "ru_RU" }, Refs.ID);
        TEXT_COLOR = ConfigHelper.getId(this, "general", "textColor", 16777215);
        TEXT_SHADOW = ConfigHelper.getBoolean(this, "general", "textShadow", false, "Setting to true renders a shadow behind the text.");
        USE_CUSTOM_SOUNDS = ConfigHelper.getBoolean(this, "general", "useCustomSounds", false, "Use custom sounds for barrel open/close. Located at mods/vintagebarrels/sounds/custom/");
        ROTATE_ITEM_INSIDE_LABEL = ConfigHelper.getBoolean(this, "general", "rotateItemInsideLabel", false, "Whether or not should item rotate inside Wooden Label.");
        RESTRICT_TRANSPORTATION = ConfigHelper.getBoolean(this, "general", "restrictTransportation", true, "Prevent Player from being able to carry lots of Full Inventory Transporters using Backpacks and other portable inventories.");
    }

    public static String getOpenSound(Random random) {
        return USE_CUSTOM_SOUNDS ? "block.barrel.custom.barrel_open" : random.nextInt(2) == 1 ? "block.barrel.barrel_open" : "block.barrel.barrel_open_alt";
    }

    public static String getCloseSound() {
        return USE_CUSTOM_SOUNDS ? "block.barrel.custom.barrel_close" : "block.barrel.barrel_close";
    }
}
