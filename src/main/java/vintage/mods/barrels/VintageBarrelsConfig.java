package vintage.mods.barrels;

import cpw.mods.fml.relauncher.FMLInjectionData;
import mods.vintage.core.helpers.ConfigHelper;
import net.minecraftforge.common.Configuration;

import java.io.File;

public class VintageBarrelsConfig {

    public static Configuration MAIN_CONFIG;
    public static String[] LANGUAGES;

    public static int BARREL_ID = 1076;
    public static int UPGRADE_ID = 19602;

    public static void initMainConfig() {
        MAIN_CONFIG = new Configuration(new File((File) FMLInjectionData.data()[6], "config/vintagebarrels.cfg"));
        MAIN_CONFIG.load();

        LANGUAGES = ConfigHelper.getStrings(MAIN_CONFIG, "languages", "localization_list", new String[] { "en_US" }, "Supported localizations.");

        BARREL_ID = ConfigHelper.getId(MAIN_CONFIG, "IDs", "barrel", BARREL_ID);
        UPGRADE_ID = ConfigHelper.getId(MAIN_CONFIG, "IDs", "upgrade", UPGRADE_ID);

        if (MAIN_CONFIG != null) {
            MAIN_CONFIG.save();
        }
    }
}
