package vintage.mods.barrels;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
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
        oredictRecipe(BarrelChangerType.WOOD_IRON.getStack(),
                "###", "#@#", "###",
                '#', "ingotIron",
                '@', Block.torchRedstoneActive);

        // wood to copper
        oredictRecipe(BarrelChangerType.WOOD_COPPER.getStack(),
                "###", "#@#", "###",
                '#', "ingotCopper",
                '@', Block.torchRedstoneActive);

        // iron to gold
        oredictRecipe(BarrelChangerType.IRON_GOLD.getStack(),
                "###", "#@#", "###",
                '#', "ingotGold",
                '@', Block.torchRedstoneActive);

        // gold to diamond
        oredictRecipe(BarrelChangerType.GOLD_DIAMOND.getStack(),
                "###", "G@G", "###",
                '#', Block.glass,
                'G', "gemDiamond",
                '@', Block.torchRedstoneActive);

        // copper to silver
        oredictRecipe(BarrelChangerType.COPPER_SILVER.getStack(),
                "###", "#@#", "###",
                '#', "ingotSilver",
                '@', Block.torchRedstoneActive);

        // copper to iron
        oredictRecipe(BarrelChangerType.COPPER_IRON.getStack(),
                "#G#", "G@G", "#G#",
                'G', Block.glass,
                '#', "ingotIron",
                '@', Block.torchRedstoneActive);

        // silver to gold
        oredictRecipe(BarrelChangerType.SILVER_GOLD.getStack(),
                "#G#", "G@G", "#G#",
                'G', Block.glass,
                '#', "ingotGold",
                '@', Block.torchRedstoneActive);

        // diamond to crystal
        oredictRecipe(BarrelChangerType.DIAMOND_CRYSTAL.getStack(),
                "###", "#@#", "###",
                '#', Block.glass,
                '@', Block.torchRedstoneActive);

        // diamond to obsidian
        oredictRecipe(BarrelChangerType.DIAMOND_OBSIDIAN.getStack(),
                "###", "#@#", "###",
                '#', Block.obsidian,
                '@', Block.torchRedstoneActive);
    }

    public static void oredictRecipe(ItemStack stack, Object... args) {
        GameRegistry.addRecipe(new ShapedOreRecipe(stack, args));
    }
}
