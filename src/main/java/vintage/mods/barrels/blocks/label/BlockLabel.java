package vintage.mods.barrels.blocks.label;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import vintage.mods.barrels.VintageBarrels;
import vintage.mods.barrels.VintageBarrelsConfig;
import vintage.mods.barrels.proxy.ClientProxy;
import vintage.mods.barrels.tiles.TileEntityLabel;

import java.util.List;
import java.util.Random;

public class BlockLabel extends BlockContainer {

    public int labelAngle;
    public int labelAnglePar2;

    public BlockLabel() {
        super(VintageBarrelsConfig.WOOD_LABEL_ID, Material.wood);
        this.setHardness(3.0F);
        this.setUnlocalizedName("label");
        this.setStepSound(Block.soundWoodFootstep);
        this.setCreativeTab(VintageBarrels.TAB);
        MinecraftForge.setBlockHarvestLevel(this, "axe", 2);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister par1IconRegister) {
        this.blockIcon = par1IconRegister.registerIcon("wood");
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int par1, CreativeTabs tab, List subItems) {
        for(int x = 0; x <= 3; ++x) {
            subItems.add(new ItemStack(this, 1, x));
        }
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityLabel) {
            TileEntityLabel label = (TileEntityLabel) tileEntity;
            ItemStack stack = label.getLabelStack(0);
            ItemStack stack1 = label.getLabelStack(1);
            ItemStack stack2 = label.getLabelStack(2);
            int value1 = 0;
            int value2 = 0;
            int value3 = 0;
            if (stack != null && stack.itemID < 4096) {
                value1 = Block.lightValue[stack.itemID];
            }

            if (stack1 != null && stack1.itemID < 4096) {
                value2 = Block.lightValue[stack1.itemID];
            }

            if (stack2 != null && stack2.itemID < 4096) {
                value3 = Block.lightValue[stack2.itemID];
            }
            return Math.max((Math.max(value1, value2)), value3);
        } else return 0;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
        TileEntity tileEntity = blockAccess.getBlockTileEntity(i, j, k);
        TileEntityLabel labelTile = (TileEntityLabel)tileEntity;
        this.labelAnglePar2 = labelTile.getAngle();
        switch (this.labelAnglePar2) {
            case 0:
                this.setBlockBounds(0.94F, 0.1F, 0.3F, 1.0F, 0.3F, 0.7F);
                break;
            case 1:
                this.setBlockBounds(0.3F, 0.1F, 0.94F, 0.7F, 0.3F, 1.0F);
                break;
            case 2:
                this.setBlockBounds(0.0F, 0.1F, 0.3F, 0.06F, 0.3F, 0.7F);
                break;
            case 3:
                this.setBlockBounds(0.3F, 0.1F, 0.0F, 0.7F, 0.3F, 0.06F);
        }
    }

    @Override
    public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
        this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return null;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {
        return ClientProxy.woodenlabelrenderID;
    }

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int par6, float par7, float par8, float par9) {
        if (!world.isRemote) {
            TileEntityLabel tileLabel = (TileEntityLabel)world.getBlockTileEntity(i, j, k);
            if (tileLabel == null) {
                return false;
            }

            if (tileLabel != null && player.isSneaking()) {
                player.openGui(VintageBarrels.instance, 6, world, i, j, k);
            }
        }

        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving player, ItemStack itemStack) {
        int angle = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + (double)0.5F) & 3;
        ++angle;
        angle %= 4;
        this.labelAngle = angle;
        TileEntityLabel tileLabel = (TileEntityLabel)world.getBlockTileEntity(i, j, k);
        tileLabel.setAngle(this.labelAngle);
    }

    @Override
    public void breakBlock(World world, int i, int j, int k, int par5, int par6) {
        this.dropItems(world, i, j, k);
        super.breakBlock(world, i, j, k, par5, par6);
    }

    private void dropItems(World world, int i, int j, int k) {
        Random rando = new Random();
        TileEntity tileEntity = world.getBlockTileEntity(i, j, k);
        if (tileEntity instanceof IInventory) {
            IInventory inventory = (IInventory)tileEntity;

            for(int x = 0; x < inventory.getSizeInventory(); ++x) {
                ItemStack item = inventory.getStackInSlot(x);
                if (item != null && item.stackSize > 0) {
                    float ri = rando.nextFloat() * 0.8F + 0.1F;
                    float rj = rando.nextFloat() * 0.8F + 0.1F;
                    float rk = rando.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityItem = new EntityItem(world, (double)((float)i + ri), (double)((float)j + rj), (double)((float)k + rk), new ItemStack(item.itemID, item.stackSize, item.getItemDamage()));
                    if (item.hasTagCompound()) {
                        entityItem.getEntityItem().setTagCompound((NBTTagCompound)item.getTagCompound().copy());
                    }

                    float factor = 0.05F;
                    entityItem.motionX = rando.nextGaussian() * (double)factor;
                    entityItem.motionY = rando.nextGaussian() * (double)factor + (double)0.2F;
                    entityItem.motionZ = rando.nextGaussian() * (double)factor;
                    world.spawnEntityInWorld(entityItem);
                    item.stackSize = 0;
                }
            }

        }
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        try {
            return new TileEntityLabel();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
