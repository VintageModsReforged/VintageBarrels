package vintage.mods.barrels;

import mods.vintage.core.platform.lang.FormattedTranslator;
import vintage.mods.barrels.tiles.*;

public enum BarrelType {
    IRON(54, 9, TileEntityIronBarrel.class, FormattedTranslator.WHITE),
    GOLD(84, 12, TileEntityGoldBarrel.class, FormattedTranslator.YELLOW),
    DIAMOND(104, 13, TileEntityDiamondBarrel.class, FormattedTranslator.DARK_AQUA),
    COPPER(45, 9, TileEntityCopperBarrel.class, FormattedTranslator.GOLD),
    SILVER(72, 12, TileEntitySilverBarrel.class, FormattedTranslator.AQUA),
    CRYSTAL(104, 13, TileEntityCrystalBarrel.class, FormattedTranslator.AQUA),
    OBSIDIAN(104, 13, TileEntityObsidianBarrel.class, FormattedTranslator.BLUE),
    WOOD(27, 9, TileEntityWoodBarrel.class, FormattedTranslator.GREEN);

    public final int size;
    private final int rowLength;
    public final Class<? extends TileEntityBarrel> clazz;
    public final FormattedTranslator formatter;

    BarrelType(int size, int rowLength, Class<? extends TileEntityBarrel> clazz, FormattedTranslator formatter) {
        this.size = size;
        this.rowLength = rowLength;
        this.clazz = clazz;
        this.formatter = formatter;
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
