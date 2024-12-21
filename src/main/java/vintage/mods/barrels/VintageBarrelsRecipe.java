package vintage.mods.barrels;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class VintageBarrelsRecipe {

    public static ItemStack IRON = new ItemStack(BlocksItems.BARREL, 1, 0);
    public static ItemStack GOLD = new ItemStack(BlocksItems.BARREL, 1, 1);
    public static ItemStack DIAMOND = new ItemStack(BlocksItems.BARREL, 1, 2);
    public static ItemStack COPPER = new ItemStack(BlocksItems.BARREL, 1, 3);
    public static ItemStack SILVER = new ItemStack(BlocksItems.BARREL, 1, 4);
    public static ItemStack CRYSTAL = new ItemStack(BlocksItems.BARREL, 1, 5);
    public static ItemStack OBSIDIAN = new ItemStack(BlocksItems.BARREL, 1, 6);
    public static ItemStack WOOD = new ItemStack(BlocksItems.BARREL, 1, 7);

    public static ItemStack OAK = new ItemStack(BlocksItems.LABEL, 2, 0);
    public static ItemStack SPRUCE = new ItemStack(BlocksItems.LABEL, 2, 1);
    public static ItemStack BIRCH = new ItemStack(BlocksItems.LABEL, 2, 2);
    public static ItemStack JUNGLE = new ItemStack(BlocksItems.LABEL, 2, 3);

    public static void initRecipes() {
        // wood
        oredictRecipe(WOOD,
                "PSP", "P P", "PSP",
                'P', "plankWood",
                'S', "slabWood");

        // iron from wood - ic2
        oredictRecipe(IRON,
                "III", "IWI", "III",
                'I', "ingotRefinedIron",
                'W', WOOD);

        // iron from wood
        oredictRecipe(IRON,
                "III", "IWI", "III",
                'I', "ingotIron",
                'W', WOOD);

        // iron from copper - ic2
        oredictRecipe(IRON,
                "I#I", "#W#", "I#I",
                'I', "ingotRefinedIron",
                '#', Block.glass,
                'W', COPPER);

        // iron from copper
        oredictRecipe(IRON,
                "I#I", "#W#", "I#I",
                'I', "ingotIron",
                '#', Block.glass,
                'W', COPPER);

        // gold from iron
        oredictRecipe(GOLD,
                "III", "IWI", "III",
                'I', "ingotGold",
                'W', IRON);

        // gold from silver
        oredictRecipe(GOLD,
                "I#I", "#W#", "I#I",
                'I', "ingotGold",
                '#', Block.glass,
                'W', SILVER);

        // diamond from gold
        oredictRecipe(DIAMOND,
                "III", "#W#", "III",
                '#', "gemDiamond",
                'I', Block.glass,
                'W', GOLD);

        // diamond from silver
        oredictRecipe(DIAMOND,
                "III", "IWI", "###",
                '#', "gemDiamond",
                'I', Block.glass,
                'W', SILVER);

        // copper from wood
        oredictRecipe(COPPER,
                "III", "IWI", "III",
                'I', "ingotCopper",
                'W', WOOD);

        // silver from copper
        oredictRecipe(SILVER,
                "III", "IWI", "III",
                'I', "ingotSilver",
                'W', COPPER);

        // silver from iron
        oredictRecipe(SILVER,
                "I#I", "#W#", "I#I",
                '#', Block.glass,
                'I', "ingotSilver",
                'W', IRON);

        // crystal from diamond
        oredictRecipe(CRYSTAL,
                "III", "IWI", "III",
                'I', Block.glass,
                'W', DIAMOND);

        // obsidian from diamond
        oredictRecipe(OBSIDIAN,
                "III", "IWI", "III",
                'I', Block.obsidian,
                'W', DIAMOND);

        // wood to iron
        oredictRecipe(BlocksItems.WOOD_IRON,
                "###", "#@#", "###",
                '#', "ingotIron",
                '@', Block.torchRedstoneActive);

        // wood to copper
        oredictRecipe(BlocksItems.WOOD_COPPER,
                "###", "#@#", "###",
                '#', "ingotCopper",
                '@', Block.torchRedstoneActive);

        // iron to gold
        oredictRecipe(BlocksItems.IRON_GOLD,
                "###", "#@#", "###",
                '#', "ingotGold",
                '@', Block.torchRedstoneActive);

        // gold to diamond
        oredictRecipe(BlocksItems.GOLD_DIAMOND,
                "###", "G@G", "###",
                '#', Block.glass,
                'G', "gemDiamond",
                '@', Block.torchRedstoneActive);

        // copper to silver
        oredictRecipe(BlocksItems.COPPER_SILVER,
                "###", "#@#", "###",
                '#', "ingotSilver",
                '@', Block.torchRedstoneActive);

        // copper to iron
        oredictRecipe(BlocksItems.COPPER_IRON,
                "#G#", "G@G", "#G#",
                'G', Block.glass,
                '#', "ingotIron",
                '@', Block.torchRedstoneActive);

        // silver to gold
        oredictRecipe(BlocksItems.SILVER_GOLD,
                "#G#", "G@G", "#G#",
                'G', Block.glass,
                '#', "ingotGold",
                '@', Block.torchRedstoneActive);

        // diamond to crystal
        oredictRecipe(BlocksItems.DIAMOND_CRYSTAL,
                "###", "#@#", "###",
                '#', Block.glass,
                '@', Block.torchRedstoneActive);

        // diamond to obsidian
        oredictRecipe(BlocksItems.DIAMOND_OBSIDIAN,
                "###", "#@#", "###",
                '#', Block.obsidian,
                '@', Block.torchRedstoneActive);

        ItemStack oakPlank = new ItemStack(Block.planks, 1, 0);
        ItemStack sprucePlank = new ItemStack(Block.planks, 1, 1);
        ItemStack birchPlank = new ItemStack(Block.planks, 1, 2);
        ItemStack junglePlank = new ItemStack(Block.planks, 1, 3);

        oredictRecipe(OAK,
                "###", "   ", "###",
                '#', oakPlank);
        oredictRecipe(SPRUCE,
                "###", "   ", "###",
                '#', sprucePlank);
        oredictRecipe(BIRCH,
                "###", "   ", "###",
                '#', birchPlank);
        oredictRecipe(JUNGLE,
                "###", "   ", "###",
                '#', junglePlank);

        oredictRecipe(BarrelTransporters.WOOD.getStack(),
                "S S", "SSS", " S ",
                'S', Item.stick);

        oredictRecipe(BarrelTransporters.COPPER.getStack(),
                "S S", "SSS", " # ",
                'S', Item.stick,
                '#', "ingotCopper");

        oredictRecipe(BarrelTransporters.TIN.getStack(),
                "S S", "SSS", " # ",
                'S', Item.stick,
                '#', "ingotTin");

        oredictRecipe(BarrelTransporters.IRON.getStack(),
                "S S", "SSS", " # ",
                'S', Item.stick,
                '#', "ingotIron");

        oredictRecipe(BarrelTransporters.GOLD.getStack(),
                "S S", "SSS", " # ",
                'S', Item.stick,
                '#', "ingotGold");

        oredictRecipe(BarrelTransporters.SILVER.getStack(),
                "S S", "SSS", " # ",
                'S', Item.stick,
                '#', "ingotSilver");

        oredictRecipe(BarrelTransporters.OBSIDIAN.getStack(),
                "S S", "SSS", " # ",
                'S', Item.stick,
                '#', Block.obsidian);

        oredictRecipe(BarrelTransporters.DIAMOND.getStack(),
                "S S", "SSS", " # ",
                'S', Item.stick,
                '#', "gemDiamond");


        oredictRecipe(new ItemStack(BlocksItems.NAME_TAG),
                "XXX", "XIX", "XXX",
                'I', "ingotIron", 'X', Item.paper);
    }

    public static void oredictRecipe(ItemStack stack, Object... args) {
        GameRegistry.addRecipe(new ShapedOreRecipe(stack, args));
    }
}
