package vintage.mods.barrels.tiles;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityLabel extends TileEntity implements IInventory {

    private ItemStack[] labelInventory = new ItemStack[4];
    public int labelAngle;

    public TileEntityLabel() {}

    @Override
    public int getSizeInventory() {
        return this.labelInventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.labelInventory[slot];
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        this.labelInventory[slot] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }

        this.worldObj.updateAllLightTypes(this.xCoord, this.yCoord, this.zCoord);
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        ItemStack stack = this.getStackInSlot(slot);
        if (stack != null) {
            if (stack.stackSize <= amount) {
                this.setInventorySlotContents(slot, (ItemStack)null);
            } else {
                stack = stack.splitStack(amount);
                if (stack.stackSize == 0) {
                    this.setInventorySlotContents(slot, (ItemStack)null);
                }
            }
        }

        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack stack = this.getStackInSlot(slot);
        if (stack != null) {
            this.setInventorySlotContents(slot, (ItemStack)null);
        }

        return stack;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && player.getDistanceSq((double)this.xCoord + (double)0.5F, (double)this.yCoord + (double)0.5F, (double)this.zCoord + (double)0.5F) < (double)64.0F;
    }

    @Override
    public void openChest() {}

    public void closeChest() {}

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagList tagList = nbt.getTagList("LabelInventory");
        this.labelInventory = new ItemStack[this.getSizeInventory()];

        for(int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound tag = (NBTTagCompound)tagList.tagAt(i);
            byte slot = tag.getByte("Slot");
            if (slot >= 0 && slot < this.labelInventory.length) {
                this.labelInventory[slot] = ItemStack.loadItemStackFromNBT(tag);
            }
        }

        this.labelAngle = nbt.getInteger("labelAngle");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        NBTTagList itemList = new NBTTagList();

        for(int i = 0; i < this.labelInventory.length; ++i) {
            ItemStack stack = this.labelInventory[i];
            if (stack != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte)i);
                stack.writeToNBT(tag);
                itemList.appendTag(tag);
            }
        }

        nbt.setTag("LabelInventory", itemList);
        nbt.setInteger("labelAngle", this.labelAngle);
    }

    @Override
    public String getInvName() {
        return "";
    }

    public void setAngle(int angle) {
        this.labelAngle = angle;
    }

    public int getAngle() {
        return this.labelAngle;
    }

    public ItemStack getLabelStack(int slot) {
        ItemStack labelStack = this.labelInventory[slot];
        return labelStack != null ? labelStack : null;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound dataTag = new NBTTagCompound();
        dataTag.setInteger("labelAngle", this.labelAngle);
        NBTTagList itemList = new NBTTagList();

        for(int i = 0; i < this.labelInventory.length; ++i) {
            ItemStack stack = this.labelInventory[i];
            if (stack != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte)i);
                stack.writeToNBT(tag);
                itemList.appendTag(tag);
            }
        }

        dataTag.setTag("LabelInventory", itemList);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, dataTag);
    }

    @Override
    public void onDataPacket(INetworkManager manager, Packet132TileEntityData packet) {
        NBTTagCompound nbtData = packet.customParam1;
        this.labelAngle = nbtData.getInteger("labelAngle");
        NBTTagList tagList = nbtData.getTagList("LabelInventory");
        this.labelInventory = new ItemStack[this.getSizeInventory()];

        for(int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound tag = (NBTTagCompound)tagList.tagAt(i);
            byte slot = tag.getByte("Slot");
            if (slot >= 0 && slot < this.labelInventory.length) {
                this.labelInventory[slot] = ItemStack.loadItemStackFromNBT(tag);
            }
        }

    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1);
    }
}
