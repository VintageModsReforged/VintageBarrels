package vintage.mods.barrels;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.LanguageRegistry;
import mods.vintage.core.platform.lang.ILangProvider;
import mods.vintage.core.platform.lang.LangManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vintage.mods.barrels.network.NetworkHandler;
import vintage.mods.barrels.proxy.CommonProxy;

import java.util.Arrays;
import java.util.List;

@Mod(modid = Refs.ID, name = Refs.NAME, version = Refs.VERSION, dependencies = Refs.DEPS, acceptedMinecraftVersions = Refs.MC_VERSION)
@NetworkMod(channels = { Refs.ID }, clientSideRequired = true, packetHandler = NetworkHandler.class)
public class VintageBarrels implements ILangProvider {

    public static final CreativeTabs TAB = new CreativeTabs(Refs.ID) {{
            LanguageRegistry.instance().addStringLocalization("itemGroup." + Refs.ID, Refs.NAME);
        }

        @Override
        public Item getTabIconItem() {
            return new ItemStack(BlocksItems.BARREL).getItem();
        }
    };

    @SidedProxy(clientSide = Refs.PROXY_CLIENT, serverSide = Refs.PROXY_COMMON)
    public static CommonProxy PROXY;
    @Mod.Instance(Refs.ID)
    public static VintageBarrels instance;

    public VintageBarrels() {}

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent e) {
        // register annoying OreDict materials... Minecraft...
        OreDictionary.registerOre("ingotIron", Item.ingotIron);
        OreDictionary.registerOre("ingotGold", Item.ingotGold);
        OreDictionary.registerOre("gemDiamond", Item.diamond);
        VintageBarrelsConfig.initMainConfig();
        BlocksItems.initBlocks();
        LangManager.THIS.registerLangProvider(this);
    }

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent e) {
        VintageBarrelsRecipe.initRecipes();
    }

    @Override
    public String getModid() {
        return Refs.ID;
    }

    @Override
    public List<String> getLocalizationList() {
        return Arrays.asList(VintageBarrelsConfig.LANGUAGES);
    }
}
