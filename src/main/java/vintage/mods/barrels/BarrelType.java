package vintage.mods.barrels;

import vintage.mods.barrels.tiles.*;

public enum BarrelType {
    IRON(54, 9, TileEntityIronBarrel.class),
    GOLD(81, 9, TileEntityGoldBarrel.class),
    DIAMOND(108, 12, TileEntityDiamondBarrel.class),
    COPPER(45, 9, TileEntityCopperBarrel.class),
    SILVER(72, 9, TileEntitySilverBarrel.class),
    CRYSTAL(108, 12, TileEntityCrystalBarrel.class),
    OBSIDIAN(108, 12, TileEntityObsidianBarrel.class),
    WOOD(27, 9, TileEntityWoodBarrel.class);

    public final int size;
    private final int rowLength;
    public final Class<? extends TileEntityBarrel> clazz;

    BarrelType(int size, int rowLength, Class<? extends TileEntityBarrel> clazz) {
        this.size = size;
        this.rowLength = rowLength;
        this.clazz = clazz;
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
