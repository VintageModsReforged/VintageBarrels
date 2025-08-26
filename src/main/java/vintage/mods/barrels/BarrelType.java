package vintage.mods.barrels;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.vintage.core.platform.lang.Translator;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import vintage.mods.barrels.tiles.*;

import java.util.Locale;

public enum BarrelType {
    IRON(54, 9, TileEntityIronBarrel.class, Translator.WHITE),
    GOLD(84, 12, TileEntityGoldBarrel.class, Translator.YELLOW),
    DIAMOND(104, 13, TileEntityDiamondBarrel.class, Translator.DARK_AQUA),
    COPPER(45, 9, TileEntityCopperBarrel.class, Translator.GOLD),
    SILVER(72, 12, TileEntitySilverBarrel.class, Translator.AQUA),
    CRYSTAL(104, 13, TileEntityCrystalBarrel.class, Translator.AQUA),
    OBSIDIAN(104, 13, TileEntityObsidianBarrel.class, Translator.BLUE),
    WOOD(27, 9, TileEntityWoodBarrel.class, Translator.GREEN);

    public final int size;
    private final int rowLength;
    public final Class<? extends TileEntityBarrel> clazz;
    @SideOnly(Side.CLIENT)
    public Icon icon;
    public final Translator formatter;
    public static final BarrelType[] VALUES = values();

    BarrelType(int size, int rowLength, Class<? extends TileEntityBarrel> clazz, Translator formatter) {
        this.size = size;
        this.rowLength = rowLength;
        this.clazz = clazz;
        this.formatter = formatter;
    }

    public static BarrelType getFromId(int id) {
        return VALUES[id % values().length];
    }

    public static TileEntityBarrel makeEntity(int metadata) {
        int chesttype = validateMeta(metadata);
        if (chesttype == metadata) {
            try {
                return values()[chesttype].clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister registry) {
        this.icon = registry.registerIcon(Refs.ID + ":barrel_" + this.name().toLowerCase(Locale.ROOT));
    }

    public int getRowCount() {
        return size / rowLength;
    }

    public int getRowLength() {
        return rowLength;
    }

    public boolean isTransparent() {
        return this == CRYSTAL;
    }

    public static int validateMeta(int i) {
        if (i <= values().length && values()[i].size > 0) {
            return i;
        } else {
            return 0;
        }
    }

    public boolean isExplosionResistant() {
        return this == OBSIDIAN;
    }
}
