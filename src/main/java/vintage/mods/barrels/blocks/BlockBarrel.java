package vintage.mods.barrels.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.vintage.core.VintageCore;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureStitched;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vintage.mods.barrels.BarrelType;
import vintage.mods.barrels.Refs;
import vintage.mods.barrels.VintageBarrels;
import vintage.mods.barrels.client.BarrelRenderingHandler;
import vintage.mods.barrels.client.BlockTextureStitched;
import vintage.mods.barrels.tiles.TileEntityBarrel;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public class BlockBarrel extends BlockContainer {

    protected final Random RANDOM = new Random();
    public static final int[][] SIDE_FACING = new int[][]{{3, 2, 0, 0, 0, 0}, {2, 3, 1, 1, 1, 1}, {1, 1, 3, 2, 5, 4}, {0, 0, 2, 3, 4, 5}, {4, 5, 4, 5, 3, 2}, {5, 4, 5, 4, 2, 3}};
    @SideOnly(Side.CLIENT)
    protected Icon[][] ICONS;

    public BlockBarrel(int id) {
        super(id, Material.iron);
        this.setUnlocalizedName("barrel");
        this.setHardness(3.0F);
        this.setBlockBounds(0, 0, 0, 1, 1, 1);
        this.setCreativeTab(VintageBarrels.TAB);
    }

    public String getTextureFolder() {
        return "barrels";
    }

    public String getTextureName(int index) {
        return BarrelType.VALUES[index].name().toLowerCase(Locale.ROOT);
    }

    public int getIconIndex(int meta) {
        return meta;
    }

    public final int getIconSubIndex(int side, int facing) {
        return this.getIconSubIndex(side, facing, false);
    }

    public final int getIconSubIndex(int side, int facing, boolean active) {
        int ret = SIDE_FACING[side][facing];
        return active ? ret + 6 : ret;
    }

    public int getDefaultFacing(int meta) {
        return 3;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        int metaCount;
        for(metaCount = 0; metaCount < BarrelType.values().length; metaCount++) {}

        this.ICONS = new Icon[metaCount][12];
        String textureFolder = this.getTextureFolder() == null ? "" : this.getTextureFolder() + "/";

        for(int index = 0; index < metaCount; ++index) {
            for(int active = 0; active < 2; ++active) {
                for(int side = 0; side < 6; ++side) {
                    int subIndex = active * 6 + side;
                    String name = Refs.ID +  ":" + textureFolder + this.getTextureName(index) + "." + subIndex;
                    TextureStitched texture = new BlockTextureStitched(name);
                    this.ICONS[index][subIndex] = texture;
                    ((TextureMap)iconRegister).setTextureEntry(name, texture);
                }
            }
        }
    }


    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess iBlockAccess, int x, int y, int z, int side) {
        int facing = this.getFacing(iBlockAccess, x, y, z);
        boolean active = isActive(iBlockAccess, x, y, z);
        int meta = iBlockAccess.getBlockMetadata(x, y, z);
        int index = this.getIconIndex(meta);
        int subIndex = this.getIconSubIndex(side, facing, active);
        if (index >= this.ICONS.length) {
            return null;
        } else {
            try {
                return this.ICONS[index][subIndex];
            } catch (Exception e) {
                VintageCore.LOGGER.info(String.format("Coordinates: [x=%s, y=%s, z=%s]. Side: %s. Block: %s. Meta: %s. Facing: %s. Active: %s. Index: %s. SubIndex: %s", x, y, z, side, this, meta, facing, active, index, subIndex));
                return null;
            }
        }
    }


    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta) {
        int facing = this.getDefaultFacing(meta);
        int index = this.getIconIndex(meta);
        int subIndex = this.getIconSubIndex(side, facing);
        if (index >= this.ICONS.length) {
            return null;
        } else {
            try {
                return this.ICONS[index][subIndex];
            } catch (Exception e) {
                VintageCore.LOGGER.severe("Side: " + side + "\n" + "Block: " + this + "\n" + "Meta: " + meta + "\n" + "Facing: " + facing + "\n" + "Index: " + index + "\n" + "SubIndex: " + subIndex + "\n" + e);
                return null;
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {
        return BarrelRenderingHandler.renderId;
    }

    public static boolean isActive(IBlockAccess iblockaccess, int i, int j, int k) {
        TileEntity te = iblockaccess.getBlockTileEntity(i, j, k);
        return te instanceof TileEntityBarrel && ((TileEntityBarrel) te).isActive();
    }

    public int getFacing(IBlockAccess iBlockAccess, int x, int y, int z) {
        TileEntity te = iBlockAccess.getBlockTileEntity(x, y, z);
        if (te instanceof TileEntityBarrel) {
            return ((TileEntityBarrel) te).getFacing();
        } else {
            int meta = iBlockAccess.getBlockMetadata(x, y, z);
            return this.getDefaultFacing(meta);
        }
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

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityliving, ItemStack itemStack) {
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
}
