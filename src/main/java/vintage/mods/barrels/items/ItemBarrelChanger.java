package vintage.mods.barrels.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vintage.mods.barrels.BarrelChangerType;
import vintage.mods.barrels.BarrelType;
import vintage.mods.barrels.BlocksItems;
import vintage.mods.barrels.VintageBarrels;
import vintage.mods.barrels.blocks.BlockNewBarrel;
import vintage.mods.barrels.tiles.TileEntityBarrel;
import vintage.mods.barrels.tiles.TileEntityWoodBarrel;

import java.util.List;

public class ItemBarrelChanger extends Item {

    public ItemBarrelChanger(int id) {
        super(id);
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
        this.setCreativeTab(VintageBarrels.TAB);
        this.setItemName("barrel_upgrade");
    }

    @Override
    public void getSubItems(int id, CreativeTabs tabs, List list) {
        for (int i = 0; i < BarrelChangerType.VALUES.length; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public int getIconFromDamage(int damage) {
        return damage;
    }

    @Override
    public String getItemNameIS(ItemStack stack) {
        int index = stack.getItemDamage();
        return "item." + BarrelChangerType.getFromId(index).itemName;
    }

    public int getTargetChestOrdinal(ItemStack stack) {
        int index = stack.getItemDamage();
        return BarrelChangerType.getFromId(index).getTarget();
    }

    public BarrelChangerType getType(ItemStack stack) {
        int index = stack.getItemDamage();
        return BarrelChangerType.getFromId(index);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int X, int Y, int Z, int side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) return false;
        TileEntity te = world.getBlockTileEntity(X, Y, Z);
        TileEntityBarrel newBarrel;
        if (te instanceof TileEntityBarrel && !(te instanceof TileEntityWoodBarrel)) {
            TileEntityBarrel entityBarrel = (TileEntityBarrel) te;
            newBarrel = entityBarrel.applyUpgradeItem(this, stack);
            if (newBarrel == null) {
                return false;
            }
        } else if (te instanceof TileEntityWoodBarrel) {
            TileEntityWoodBarrel tec = (TileEntityWoodBarrel) te;
            if (tec.numUsingPlayers > 0) {
                return false;
            }
            if (!getType(stack).canUpgrade(BarrelType.WOOD)) {
                return false;
            }
            // Force old TE out of the world so that adjacent chests can update
            newBarrel = BarrelType.makeEntity(getTargetChestOrdinal(stack));
            int newSize = newBarrel.barrelContents.length;
            ItemStack[] chestContents = tec.barrelContents;
            System.arraycopy(chestContents, 0, newBarrel.barrelContents, 0, Math.min(newSize, chestContents.length));
            BlockNewBarrel block = BlocksItems.BARREL;
            block.dropContent(newSize, tec, world, tec.xCoord, tec.yCoord, tec.zCoord);
            newBarrel.sortTopStacks();
            for (int i = 0; i < Math.min(newSize, chestContents.length); i++) {
                chestContents[i] = null;
            }
            // Clear the old block out
            world.setBlockAndMetadataWithNotify(X, Y, Z, 0, 0);
            // Force the Chest TE to reset it's knowledge of neighbouring blocks
            tec.updateContainingBlockInfo();
            // And put in our block instead
            world.setBlockAndMetadataWithNotify(X, Y, Z, block.blockID, newBarrel.getType().ordinal());
            world.scheduleBlockUpdate(X, Y, Z, block.blockID, newBarrel.getType().ordinal());
        } else {
            return false;
        }
        world.setBlockTileEntity(X, Y, Z, newBarrel);
        world.setBlockMetadataWithNotify(X, Y, Z, newBarrel.getType().ordinal());
        stack.stackSize = 0;
        return true;
    }

    @Override
    public String getTextureFile() {
        return "/mods/vintagebarrels/textures/item_textures.png";
    }
}
