package vintage.mods.barrels.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vintage.mods.barrels.*;
import vintage.mods.barrels.blocks.BlockNewBarrel;
import vintage.mods.barrels.tiles.TileEntityBarrel;
import vintage.mods.barrels.tiles.TileEntityWoodBarrel;

public class ItemBarrelChanger extends Item {

    BarrelChangerType TYPE;

    public ItemBarrelChanger(int id, BarrelChangerType type) {
        super(id);
        this.setMaxStackSize(1);
        this.setUnlocalizedName(type.itemName);
        this.TYPE = type;
        this.setCreativeTab(VintageBarrels.TAB);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        this.itemIcon = register.registerIcon(Refs.ID + ":" + this.TYPE.itemName);
    }

    public int getTargetChestOrdinal() {
        return TYPE.getTarget();
    }

    public BarrelChangerType getType() {
        return this.TYPE;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) return false;
        TileEntity te = world.getBlockTileEntity(x, y, z);
        TileEntityBarrel newBarrel;
        if (te instanceof TileEntityBarrel && !(te instanceof TileEntityWoodBarrel)) {
            TileEntityBarrel entityBarrel = (TileEntityBarrel) te;
            newBarrel = entityBarrel.applyUpgradeItem(this);
            if (newBarrel == null) {
                return false;
            }
        } else if (te instanceof TileEntityWoodBarrel) {
            TileEntityWoodBarrel tec = (TileEntityWoodBarrel) te;
            if (tec.numUsingPlayers > 0) {
                return false;
            }
            if (!getType().canUpgrade(BarrelType.WOOD)) {
                return false;
            }
            // Force old TE out of the world so that adjacent chests can update
            newBarrel = BarrelType.makeEntity(getTargetChestOrdinal());
            int newSize = newBarrel.barrelContents.length;
            ItemStack[] chestContents = tec.barrelContents;
            System.arraycopy(chestContents, 0, newBarrel.barrelContents, 0, Math.min(newSize, chestContents.length));
            BlockNewBarrel block = BlocksItems.BARREL;
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
        stack.stackSize = 0;
        return true;
    }
}
