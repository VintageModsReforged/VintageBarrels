package vintage.mods.barrels;

import cpw.mods.fml.relauncher.FMLInjectionData;
import mods.vintage.core.helpers.ConfigHelper;
import net.minecraftforge.common.Configuration;

import java.io.File;

public class VintageBarrelsConfig {

    public static Configuration MAIN_CONFIG;
    public static String[] LANGUAGES;

    public static int BARREL_ID = 1076;
    public static int WOOD_LABEL_ID = 1077;
    public static int UPGRADE_ID = 19602;
    public static int TRANSPORTER_ID = 19603;

    public static int TEXT_COLOR;
    public static boolean TEXT_SHADOW;

    public static void initMainConfig() {
        MAIN_CONFIG = new Configuration(new File((File) FMLInjectionData.data()[6], "config/vintagebarrels.cfg"));
        MAIN_CONFIG.load();

        LANGUAGES = ConfigHelper.getStrings(MAIN_CONFIG, "languages", "localization_list", new String[] { "en_US", "ru_RU" }, "Supported localizations.");
        TEXT_COLOR = ConfigHelper.getId(MAIN_CONFIG, "general", "textColor", 16777215);
        TEXT_SHADOW = ConfigHelper.getBoolean(MAIN_CONFIG, "general", "textShadow", false, "Setting to true renders a shadow behind the text.");

        BARREL_ID = ConfigHelper.getId(MAIN_CONFIG, "IDs", "barrel", BARREL_ID);
        WOOD_LABEL_ID = ConfigHelper.getId(MAIN_CONFIG, "IDs", "woodLabel", WOOD_LABEL_ID);

        UPGRADE_ID = ConfigHelper.getId(MAIN_CONFIG, "IDs", "upgrade", UPGRADE_ID);
        TRANSPORTER_ID = ConfigHelper.getId(MAIN_CONFIG, "IDs", "transporter", TRANSPORTER_ID);

        if (MAIN_CONFIG != null) {
            MAIN_CONFIG.save();
        }
    }
}
