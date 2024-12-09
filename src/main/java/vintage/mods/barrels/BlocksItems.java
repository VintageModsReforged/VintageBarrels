package vintage.mods.barrels;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import jds.bibliocraft.blocks.BlockItemLabelMaster;
import net.minecraft.block.Block;
import vintage.mods.barrels.blocks.BlockNewBarrel;
import vintage.mods.barrels.blocks.label.BlockLabel;
import vintage.mods.barrels.items.ItemBarrel;
import vintage.mods.barrels.items.ItemLabel;

public class BlocksItems {

    public static BlockNewBarrel BARREL;
    public static Block LABEL;

    public static void initBlocks() {
        BARREL = new BlockNewBarrel(VintageBarrelsConfig.BARREL_ID);
        GameRegistry.registerBlock(BARREL, ItemBarrel.class, "barrel");
        BarrelChangerType.buildItems(VintageBarrelsConfig.UPGRADE_ID);
        for (BarrelType typ : BarrelType.values()) {
            GameRegistry.registerTileEntity(typ.clazz, "barrel." + typ.name().toLowerCase());
            VintageBarrels.PROXY.registerTileEntitySpecialRenderer(typ);
        }
        LABEL = new BlockLabel();
        GameRegistry.registerBlock(LABEL, ItemLabel.class, "label");

        NetworkRegistry.instance().registerGuiHandler(VintageBarrels.instance, VintageBarrels.PROXY);
    }
}
