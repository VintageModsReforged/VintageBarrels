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
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
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
        PROXY.init();
        LangManager.THIS.registerLangProvider(this);
        MinecraftForge.EVENT_BUS.register(this);
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

    @ForgeSubscribe
    public void soundLoad(SoundLoadEvent e) {
        // default
        e.manager.soundPoolSounds.addSound("block/barrel/barrel_open.ogg", VintageBarrels.class.getResource("/mods/" + Refs.ID + "/sounds/barrel_open.ogg"));
        e.manager.soundPoolSounds.addSound("block/barrel/barrel_open_alt.ogg", VintageBarrels.class.getResource("/mods/" + Refs.ID + "/sounds/barrel_open_alt.ogg"));
        e.manager.soundPoolSounds.addSound("block/barrel/barrel_close.ogg", VintageBarrels.class.getResource("/mods/" + Refs.ID + "/sounds/barrel_close.ogg"));
        // custom
        e.manager.soundPoolSounds.addSound("block/barrel/custom/barrel_open.ogg", VintageBarrels.class.getResource("/mods/" + Refs.ID + "/sounds/custom/barrel_open.ogg"));
        e.manager.soundPoolSounds.addSound("block/barrel/custom/barrel_close.ogg", VintageBarrels.class.getResource("/mods/" + Refs.ID + "/sounds/custom/barrel_close.ogg"));
    }
}
