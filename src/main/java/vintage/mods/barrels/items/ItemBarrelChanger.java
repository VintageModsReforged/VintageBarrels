package vintage.mods.barrels.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import vintage.mods.barrels.*;
import vintage.mods.barrels.blocks.BlockBarrel;
import vintage.mods.barrels.tiles.TileEntityBarrel;
import vintage.mods.barrels.tiles.TileEntityWoodBarrel;

import java.util.List;

public class ItemBarrelChanger extends Item {

    public Icon[] ICONS;

    public ItemBarrelChanger(int id) {
        super(id);
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
        this.setUnlocalizedName("barrel_upgrade");
        this.setCreativeTab(VintageBarrels.TAB);
    }

    @Override
    public void getSubItems(int id, CreativeTabs tabs, List list) {
        for (int i = 0; i < BarrelChangerType.VALUES.length; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        this.ICONS = new Icon[BarrelChangerType.VALUES.length];
        for (BarrelChangerType type : BarrelChangerType.VALUES) {
            this.ICONS[type.ordinal()] = register.registerIcon(Refs.ID + ":" + type.itemName);
        }
    }

    @Override
    public Icon getIconFromDamage(int damage) {
        return this.ICONS[damage];
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
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
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) return false;

        TileEntity te = world.getBlockTileEntity(x, y, z);
        short facing;

        TileEntityBarrel newBarrel;
        if (te instanceof TileEntityBarrel && !(te instanceof TileEntityWoodBarrel)) {
            TileEntityBarrel entityBarrel = (TileEntityBarrel) te;
            facing = entityBarrel.getFacing();
            newBarrel = entityBarrel.applyUpgradeItem(this, stack);
            if (newBarrel == null) {
                return false;
            }
        } else if (te instanceof TileEntityWoodBarrel) {
            TileEntityWoodBarrel tec = (TileEntityWoodBarrel) te;
            facing = tec.getFacing();
            if (tec.numUsingPlayers > 0) {
                return false;
            }
            if (!getType(stack).canUpgrade(BarrelType.WOOD)) {
                return false;
            }
            newBarrel = BarrelType.makeEntity(getTargetChestOrdinal(stack));
            int newSize = newBarrel.barrelContents.length;
            ItemStack[] chestContents = tec.barrelContents;
            System.arraycopy(chestContents, 0, newBarrel.barrelContents, 0, Math.min(newSize, chestContents.length));
            BlockBarrel block = BlocksItems.BARREL;
            block.dropContent(newSize, tec, world, tec.xCoord, tec.yCoord, tec.zCoord);
            newBarrel.sortTopStacks();
            for (int i = 0; i < Math.min(newSize, chestContents.length); i++) {
                chestContents[i] = null;
            }
            world.setBlock(x, y, z, 0, 0, 3);
            tec.updateContainingBlockInfo();
            world.setBlock(x, y, z, block.blockID, newBarrel.getType().ordinal(), 3);
        } else {
            return false;
        }
        world.setBlockTileEntity(x, y, z, newBarrel);
        world.setBlockMetadataWithNotify(x, y, z, newBarrel.getType().ordinal(), 3);
        newBarrel.setFacing(facing);
        stack.stackSize = 0;
        return true;
    }
}
