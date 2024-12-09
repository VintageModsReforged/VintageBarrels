package vintage.mods.barrels;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import vintage.mods.barrels.blocks.BlockNewBarrel;
import vintage.mods.barrels.items.ItemBarrel;

public class BlocksItems {

    public static BlockNewBarrel BARREL;

    public static void initBlocks() {
        BARREL = new BlockNewBarrel(VintageBarrelsConfig.BARREL_ID);
        GameRegistry.registerBlock(BARREL, ItemBarrel.class, "barrel");
        BarrelChangerType.buildItems(VintageBarrelsConfig.UPGRADE_ID);
        for (BarrelType typ : BarrelType.values()) {
            GameRegistry.registerTileEntity(typ.clazz, "barrel." + typ.name().toLowerCase());
            VintageBarrels.PROXY.registerTileEntitySpecialRenderer(typ);
        }

        NetworkRegistry.instance().registerGuiHandler(VintageBarrels.instance, VintageBarrels.PROXY);
    }
}
