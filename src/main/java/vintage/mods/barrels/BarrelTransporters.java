package vintage.mods.barrels;

import mods.vintage.core.platform.lang.Translator;
import net.minecraft.item.ItemStack;
import vintage.mods.barrels.items.ItemTransporter;

import java.util.Locale;

public enum BarrelTransporters {
    WOOD(1),
    COPPER(7),
    TIN(7),
    IRON(9),
    GOLD(19),
    SILVER(19),
    OBSIDIAN(39),
    DIAMOND(79);

    public int uses;
    public String displayName;
    public static BarrelTransporters[] VALUES = values();
    public ItemTransporter transporter;

    BarrelTransporters(int uses) {
        this.uses = uses;
        this.displayName = Translator.format("item.transporter.name", Translator.format("transporter." + name().toLowerCase(Locale.ROOT)));
    }

    public static BarrelTransporters getFromId(int id) {
        return VALUES[id % values().length];
    }

    public ItemStack getStack() {
        return new ItemStack(this.transporter);
    }

    public void buildItem(int id) {
        this.transporter = new ItemTransporter(id, this);
    }

    public static void initItems(int defaultId) {
        for (BarrelTransporters type : VALUES) {
            type.buildItem(defaultId++);
        }
    }
}
