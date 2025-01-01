package vintage.mods.barrels.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vintage.mods.barrels.BarrelType;
import vintage.mods.barrels.VintageBarrels;
import vintage.mods.barrels.client.BarrelRenderingHandler;
import vintage.mods.barrels.tiles.TileEntityBarrel;

import java.util.List;
import java.util.Random;

public class BlockNewBarrel extends BlockContainer {

    protected final Random RANDOM = new Random();
    public static final int[][] SIDE_FACING = new int[][]{{3, 2, 0, 0, 0, 0}, {2, 3, 1, 1, 1, 1}, {1, 1, 3, 2, 5, 4}, {0, 0, 2, 3, 4, 5}, {4, 5, 4, 5, 3, 2}, {5, 4, 5, 4, 2, 3}};

    public BlockNewBarrel(int id) {
        super(id, Material.iron);
        this.setBlockName("barrel");
        this.setHardness(3.0F);
        this.setCreativeTab(VintageBarrels.TAB);
    }

    @Override
    public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int side) {
        TileEntity blockEntity = iblockaccess.getBlockTileEntity(i, j, k);
        int facing = blockEntity instanceof TileEntityBarrel ? ((TileEntityBarrel)blockEntity).getFacing() : 0;
        int meta = iblockaccess.getBlockMetadata(i, j, k);
        return isActive(iblockaccess, i, j, k) ? meta + (SIDE_FACING[side][facing] + 6) * 16 : meta + SIDE_FACING[side][facing] * 16;
    }

    public static boolean isActive(IBlockAccess iblockaccess, int i, int j, int k) {
        TileEntity te = iblockaccess.getBlockTileEntity(i, j, k);
        return te instanceof TileEntityBarrel && ((TileEntityBarrel) te).isActive();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderType() {
        return BarrelRenderingHandler.renderId;
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int meta) {
        return meta + SIDE_FACING[side][3] * 16;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 0;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public String getTextureFile() {
        return "/mods/vintagebarrels/textures/block_textures.png";
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return null;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return BarrelType.makeEntity(metadata);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (!(te instanceof TileEntityBarrel)) {
            return true;
        }
        if (world.isRemote) {
            return true;
        }
        player.openGui(VintageBarrels.instance, ((TileEntityBarrel) te).getType().ordinal(), world, x, y, z);
        return true;
    }

    @Override
    public int damageDropped(int i) {
        return i;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int i, int i1) {
        TileEntityBarrel tileEntityBarrel = (TileEntityBarrel) world.getBlockTileEntity(x, y, z);
        if (tileEntityBarrel != null) {
            dropContent(0, tileEntityBarrel, world, tileEntityBarrel.xCoord, tileEntityBarrel.yCoord, tileEntityBarrel.zCoord);
        }
        super.breakBlock(world, x, y, z, i, i1);
    }

    public void dropContent(int newSize, IInventory chest, World world, int xCoord, int yCoord, int zCoord) {
        for (int l = newSize; l < chest.getSizeInventory(); l++) {
            ItemStack itemstack = chest.getStackInSlot(l);
            if (itemstack == null) {
                continue;
            }
            float f = RANDOM.nextFloat() * 0.8F + 0.1F;
            float f1 = RANDOM.nextFloat() * 0.8F + 0.1F;
            float f2 = RANDOM.nextFloat() * 0.8F + 0.1F;
            while (itemstack.stackSize > 0) {
                int i1 = RANDOM.nextInt(21) + 10;
                if (i1 > itemstack.stackSize) {
                    i1 = itemstack.stackSize;
                }
                itemstack.stackSize -= i1;
                EntityItem entityitem = new EntityItem(world, (float) xCoord + f, (float) yCoord + (newSize > 0 ? 1 : 0) + f1, (float) zCoord + f2,
                        new ItemStack(itemstack.itemID, i1, itemstack.getItemDamage()));
                float f3 = 0.05F;
                entityitem.motionX = (float) RANDOM.nextGaussian() * f3;
                entityitem.motionY = (float) RANDOM.nextGaussian() * f3 + 0.2F;
                entityitem.motionZ = (float) RANDOM.nextGaussian() * f3;
                if (itemstack.hasTagCompound()) {
                    entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                }
                world.spawnEntityInWorld(entityitem);
            }
        }
    }

    @Override
    public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ) {
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te instanceof TileEntityBarrel) {
            TileEntityBarrel tileEntityBarrel = (TileEntityBarrel) te;
            if (tileEntityBarrel.getType().isExplosionResistant()) {
                return 10000f;
            }
        }
        return super.getExplosionResistance(par1Entity, world, x, y, z, explosionX, explosionY, explosionZ);
    }

    @Override
    public void onBlockAdded(World world, int i, int j, int k) {
        super.onBlockAdded(world, i, j, k);
        world.markBlockForUpdate(i, j, k);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityliving) {
        if (!world.isRemote) {
            TileEntityBarrel barrel = (TileEntityBarrel) world.getBlockTileEntity(x, y, z);
            if (entityliving == null) {
                barrel.setFacing((short) 1);
            } else {
                int yaw = MathHelper.floor_double((double) (entityliving.rotationYaw * 4.0F / 360.0F) + 0.5) & 3;
                int pitch = Math.round(entityliving.rotationPitch);
                if (pitch >= 65) {
                    barrel.setFacing((short) 1);
                } else if (pitch <= -65) {
                    barrel.setFacing((short) 0);
                } else {
                    switch (yaw) {
                        case 0:
                            barrel.setFacing((short) 2);
                            break;
                        case 1:
                            barrel.setFacing((short) 5);
                            break;
                        case 2:
                            barrel.setFacing((short) 3);
                            break;
                        case 3:
                            barrel.setFacing((short) 4);
                    }
                }
            }
        }
    }

    @Override
    @SuppressWarnings({"unchecked"})
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int id, CreativeTabs creativeTabs, List list) {
        for (BarrelType type : BarrelType.values()) {
            list.add(new ItemStack(this, 1, type.ordinal()));
        }
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return new ItemStack(this, 1, world.getBlockMetadata(x, y, z));
    }
}
