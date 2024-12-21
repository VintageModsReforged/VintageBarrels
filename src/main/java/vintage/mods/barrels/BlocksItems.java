package vintage.mods.barrels;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vintage.mods.barrels.blocks.BlockBarrel;
import vintage.mods.barrels.blocks.label.BlockLabel;
import vintage.mods.barrels.items.ItemBarrel;
import vintage.mods.barrels.items.ItemBarrelChanger;
import vintage.mods.barrels.items.ItemLabel;
import vintage.mods.barrels.items.namingtool.ItemNameTag;
import vintage.mods.barrels.tiles.TileEntityLabel;

public class BlocksItems {

    public static BlockBarrel BARREL;
    public static Block LABEL;

    public static Item CHANGER;
    public static ItemStack IRON_GOLD;
    public static ItemStack GOLD_DIAMOND;
    public static ItemStack COPPER_SILVER;
    public static ItemStack SILVER_GOLD;
    public static ItemStack COPPER_IRON;
    public static ItemStack DIAMOND_CRYSTAL;
    public static ItemStack WOOD_IRON;
    public static ItemStack WOOD_COPPER;
    public static ItemStack DIAMOND_OBSIDIAN;

    public static Item NAME_TAG;

    public static void initBlocks() {
        BARREL = new BlockBarrel(VintageBarrelsConfig.BARREL_ID);
        GameRegistry.registerBlock(BARREL, ItemBarrel.class, "barrel");

        CHANGER = new ItemBarrelChanger(VintageBarrelsConfig.UPGRADE_ID);
        IRON_GOLD = new ItemStack(CHANGER, 1, 0);
        GOLD_DIAMOND = new ItemStack(CHANGER, 1, 1);
        COPPER_SILVER = new ItemStack(CHANGER, 1, 2);
        SILVER_GOLD = new ItemStack(CHANGER, 1, 3);
        COPPER_IRON = new ItemStack(CHANGER, 1, 4);
        DIAMOND_CRYSTAL = new ItemStack(CHANGER, 1, 5);
        WOOD_IRON = new ItemStack(CHANGER, 1, 6);
        WOOD_COPPER = new ItemStack(CHANGER, 1, 7);
        DIAMOND_OBSIDIAN = new ItemStack(CHANGER, 1, 8);

        BarrelTransporters.initItems(VintageBarrelsConfig.TRANSPORTER_ID);
        NAME_TAG = new ItemNameTag();

        for (BarrelType typ : BarrelType.values()) {
            GameRegistry.registerTileEntity(typ.clazz, "barrel." + typ.name().toLowerCase());
            VintageBarrels.PROXY.registerTileEntitySpecialRenderer(typ);
        }
        LABEL = new BlockLabel();
        GameRegistry.registerBlock(LABEL, ItemLabel.class, "label");
        GameRegistry.registerTileEntity(TileEntityLabel.class, "VintageWoodenLabel");
        
        NetworkRegistry.instance().registerGuiHandler(VintageBarrels.instance, VintageBarrels.PROXY);
    }
}
