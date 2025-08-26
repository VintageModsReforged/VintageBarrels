package vintage.mods.barrels;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.vintage.core.platform.lang.LangManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import vintage.mods.barrels.client.BlockTextureStitched;
import vintage.mods.barrels.network.NetworkHandler;
import vintage.mods.barrels.proxy.CommonProxy;

@Mod(modid = Refs.ID,
        name = Refs.NAME,
        dependencies = Refs.DEPS, useMetadata = true)
@NetworkMod(channels = { Refs.ID }, clientSideRequired = true, packetHandler = NetworkHandler.class)
public class VintageBarrels {

    public static final CreativeTabs TAB = new CreativeTabs(Refs.ID) {

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
        PROXY.preInit();
        LangManager.INSTANCE.loadCreativeTabName(Refs.ID, Refs.NAME);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.Init
    public void init(FMLInitializationEvent e) {
        PROXY.init();
    }

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent e) {
        PROXY.postInit();
    }

    @SideOnly(Side.CLIENT)
    @ForgeSubscribe
    public void onTextureStitchPost(TextureStitchEvent.Post e) {
        BlockTextureStitched.onPostStitch();
    }

    @SideOnly(Side.CLIENT)
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
