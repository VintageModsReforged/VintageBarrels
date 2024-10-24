package vintage.mods.barrels;

import cpw.mods.fml.relauncher.FMLInjectionData;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

import java.io.File;

public class VintageBarrelsConfig {

    public static Configuration MAIN_CONFIG;
    public static String LANGUAGES;

    public static int BARREL_ID = 1076;
    public static int UPGRADE_ID = 19602;

    public static void initMainConfig() {
        MAIN_CONFIG = new Configuration(new File((File) FMLInjectionData.data()[6], "config/vintage_barrels/main.cfg"));
        MAIN_CONFIG.load();

        LANGUAGES = getString("languages", "localization_list", "en_US", "Supported localizations. Format: no spaces, comma separated. Ex: <name>,<name>");

        BARREL_ID = getId("IDs", "barrel", BARREL_ID, "barrel_id");
        UPGRADE_ID = getId("IDs", "upgrade", UPGRADE_ID, "upgrade_id");

        if (MAIN_CONFIG != null) {
            MAIN_CONFIG.save();
        }
    }

    private static String getString(String cat, String tag, String defaultValue, String comment) {
        comment = comment.replace("{t}", tag) + "\n";
        Property prop = MAIN_CONFIG.get(cat, tag, defaultValue);
        prop.comment = comment + "Default: " + defaultValue;
        return prop.value;
    }

    private static int getId(String cat, String tag, int defaultValue, String comment) {
        comment = comment.replace("{t}", tag) + "\n";
        Property prop = MAIN_CONFIG.get(cat, tag, defaultValue);
        prop.comment = comment + "Default: " + defaultValue;
        int value = prop.getInt(defaultValue);
        prop.value = Integer.toString(value);
        return value;
    }
}
