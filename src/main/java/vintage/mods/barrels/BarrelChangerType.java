package vintage.mods.barrels;

import net.minecraft.item.ItemStack;
import vintage.mods.barrels.items.ItemBarrelChanger;

import static vintage.mods.barrels.BarrelType.*;

public enum BarrelChangerType {
    IRON_GOLD(IRON, GOLD, "ironGold"),
    GOLD_DIAMOND(GOLD, DIAMOND, "goldDiamond"),
    COPPER_SILVER(COPPER, SILVER, "copperSilver"),
    SILVER_GOLD(SILVER, GOLD, "silverGold"),
    COPPER_IRON(COPPER, IRON, "copperIron"),
    DIAMOND_CRYSTAL(DIAMOND, CRYSTAL, "diamondCrystal"),
    WOOD_IRON(WOOD, IRON, "woodIron"),
    WOOD_COPPER(WOOD, COPPER, "woodCopper"),
    DIAMOND_OBSIDIAN(DIAMOND, OBSIDIAN, "diamondObsidian");

    private final BarrelType source;
    private final BarrelType target;
    public final String itemName;
    private ItemBarrelChanger item;

    BarrelChangerType(BarrelType source, BarrelType target, String itemName) {
        this.source = source;
        this.target = target;
        this.itemName = itemName;
    }

    public boolean canUpgrade(BarrelType from) {
        return from == this.source;
    }

    public int getTarget() {
        return this.target.ordinal();
    }

    public ItemBarrelChanger buildItem(int id) {
        item = new ItemBarrelChanger(id, this);
        return item;
    }

    public ItemStack getStack() {
        return new ItemStack(this.item);
    }

    public static void buildItems(int defaultId) {
        for (BarrelChangerType type : values()) {
            type.buildItem(defaultId++);
        }
    }
}
