package vintage.mods.barrels.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import vintage.mods.barrels.BarrelType;
import vintage.mods.barrels.BlocksItems;
import vintage.mods.barrels.blocks.BlockBarrel;
import vintage.mods.barrels.items.ItemBarrelChanger;
import vintage.mods.barrels.network.NetworkHandler;

import java.util.Arrays;
import java.util.Comparator;

public class TileEntityBarrel extends TileEntity implements IInventory {

    private int ticksSinceSync;
    public int numUsingPlayers;
    private BarrelType type;
    public ItemStack[] barrelContents;
    private ItemStack[] topStacks;
    private boolean inventoryTouched;
    private boolean hadStuff;
    private short facing = 0;
    private boolean active = false;

    public TileEntityBarrel() {
        this(BarrelType.WOOD);
    }

    public TileEntityBarrel(BarrelType type) {
        super();
        this.type = type;
        this.barrelContents = new ItemStack[getSizeInventory()];
        this.topStacks = new ItemStack[8];
    }

    @Override
    public int getSizeInventory() {
        return type.size;
    }

    @Override
    public String getInvName() {
        return type.name();
    }

    @Override
    public boolean isInvNameLocalized() {
        return false;
    }

    public BarrelType getType() {
        return type;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        inventoryTouched = true;
        return barrelContents[i];
    }

    @Override
    public void onInventoryChanged() {
        super.onInventoryChanged();
        sortTopStacks();
    }

    public void sortTopStacks() {
        if (!type.isTransparent() || (worldObj != null && worldObj.isRemote)) {
            return;
        }
        ItemStack[] tempCopy = new ItemStack[getSizeInventory()];
        boolean hasStuff = false;
        int compressedIdx = 0;
        mainLoop:
        for (int i = 0; i < getSizeInventory(); i++) {
            if (barrelContents[i] != null) {
                for (int j = 0; j < compressedIdx; j++) {
                    if (tempCopy[j].isItemEqual(barrelContents[i])) {
                        tempCopy[j].stackSize += barrelContents[i].stackSize;
                        continue mainLoop;
                    }
                }
                tempCopy[compressedIdx++] = barrelContents[i].copy();
                hasStuff = true;
            }
        }
        if (!hasStuff && hadStuff) {
            hadStuff = false;
            for (int i = 0; i < topStacks.length; i++) {
                topStacks[i] = null;
            }
            if (worldObj != null) {
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }
            return;
        }
        hadStuff = true;
        Arrays.sort(tempCopy, new Comparator<ItemStack>() {
            @Override
            public int compare(ItemStack o1, ItemStack o2) {
                if (o1 == null) {
                    return 1;
                } else if (o2 == null) {
                    return -1;
                } else {
                    return o2.stackSize - o1.stackSize;
                }
            }
        });
        int p = 0;
        for (int i = 0; i < tempCopy.length; i++) {
            if (tempCopy[i] != null && tempCopy[i].stackSize > 0) {
                topStacks[p++] = tempCopy[i];
                if (p == topStacks.length) {
                    break;
                }
            }
        }
        for (int i = p; i < topStacks.length; i++) {
            topStacks[i] = null;
        }
        if (worldObj != null) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (barrelContents[i] != null) {
            if (barrelContents[i].stackSize <= j) {
                ItemStack itemstack = barrelContents[i];
                barrelContents[i] = null;
                onInventoryChanged();
                return itemstack;
            }
            ItemStack itemstack1 = barrelContents[i].splitStack(j);
            if (barrelContents[i].stackSize == 0) {
                barrelContents[i] = null;
            }
            onInventoryChanged();
            return itemstack1;
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        barrelContents[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
            itemstack.stackSize = getInventoryStackLimit();
        }
        onInventoryChanged();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        barrelContents = new ItemStack[getSizeInventory()];
        for (int i = 0; i < nbttaglist.tagCount(); i++) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if (j >= 0 && j < barrelContents.length) {
                barrelContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
        sortTopStacks();
        this.facing = nbttagcompound.getShort("facing");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < barrelContents.length; i++) {
            if (barrelContents[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                barrelContents[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        nbttagcompound.setTag("Items", nbttaglist);
        nbttagcompound.setShort("facing", this.facing);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        if (worldObj == null) {
            return true;
        }
        if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this) {
            return false;
        }
        return entityplayer.getDistanceSq((double) xCoord + 0.5D, (double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64D;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if ((++ticksSinceSync % 20) * 4 == 0) {
            worldObj.addBlockEvent(xCoord, yCoord, zCoord, BlocksItems.BARREL.blockID, 3, ((numUsingPlayers << 3) & 0xF8));
            if (inventoryTouched) {
                inventoryTouched = false;
                sortTopStacks();
            }
        }
        if (!worldObj.isRemote) {
            boolean newActive;
            newActive = numUsingPlayers > 0;
            if (newActive != this.isActive()) {
                this.setActive(newActive);
            }
        }
    }

    @Override
    public boolean receiveClientEvent(int i, int j) {
        if (i == 1) {
            numUsingPlayers = j;
        } else if (i == 3) {
            numUsingPlayers = (j & 0xF8) >> 3;
        }
        return true;
    }


    @Override
    public void openChest() {
        if (worldObj == null) return;
        numUsingPlayers++;
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, BlocksItems.BARREL.blockID, 1, numUsingPlayers);
        double xOffset = this.xCoord + 0.5F;
        double zOffset = this.zCoord + 0.5F;
        this.worldObj.playSoundEffect(xOffset, this.yCoord + 0.5F, zOffset, worldObj.rand.nextInt(2) == 1 ? "block.barrel.barrel_open" : "block.barrel.barrel_open_alt", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public void closeChest() {
        if (worldObj == null) return;
        numUsingPlayers--;
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, BlocksItems.BARREL.blockID, 1, numUsingPlayers);
        double xOffset = this.xCoord + 0.5F;
        double zOffset = this.zCoord + 0.5F;
        this.worldObj.playSoundEffect(xOffset, this.yCoord + 0.5F, zOffset, "block.barrel.barrel_close", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public boolean isStackValidForSlot(int index, ItemStack stack) {
        return true;
    }

    public TileEntityBarrel applyUpgradeItem(ItemBarrelChanger itemBarrelChanger, ItemStack stack) {
        if (numUsingPlayers > 0) {
            return null;
        }
        if (!itemBarrelChanger.getType(stack).canUpgrade(this.getType())) {
            return null;
        }
        TileEntityBarrel newEntity = BarrelType.makeEntity(itemBarrelChanger.getTargetChestOrdinal(stack));
        int newSize = newEntity.barrelContents.length;
        System.arraycopy(barrelContents, 0, newEntity.barrelContents, 0, Math.min(newSize, barrelContents.length));
        BlockBarrel block = BlocksItems.BARREL;
        block.dropContent(newSize, this, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
        newEntity.sortTopStacks();
        return newEntity;
    }

    public ItemStack[] getTopItemStacks() {
        return topStacks;
    }

    public TileEntityBarrel updateFromMetadata(int l) {
        if (worldObj != null && worldObj.isRemote) {
            if (l != type.ordinal()) {
                worldObj.setBlockTileEntity(xCoord, yCoord, zCoord, BarrelType.makeEntity(l));
                return (TileEntityBarrel) worldObj.getBlockTileEntity(xCoord, yCoord, zCoord);
            }
        }
        return this;
    }

    @Override
    public Packet getDescriptionPacket() {
        return NetworkHandler.getPacket(this);
    }

    public void handlePacketData(int typeData, int[] intData) {
        TileEntityBarrel chest = this;
        if (this.type.ordinal() != typeData) {
            chest = updateFromMetadata(typeData);
        }
        if (BarrelType.values()[typeData].isTransparent() && intData != null) {
            int pos = 0;
            if (intData.length < chest.topStacks.length * 3) {
                return;
            }
            for (int i = 0; i < chest.topStacks.length; i++) {
                if (intData[pos + 2] != 0) {
                    ItemStack is = new ItemStack(intData[pos], intData[pos + 2], intData[pos + 1]);
                    chest.topStacks[i] = is;
                } else {
                    chest.topStacks[i] = null;
                }
                pos += 3;
            }
        }
    }

    public int[] buildIntDataList() {
        if (type.isTransparent()) {
            int[] sortList = new int[topStacks.length * 3];
            int pos = 0;
            for (ItemStack is : topStacks) {
                if (is != null) {
                    sortList[pos++] = is.itemID;
                    sortList[pos++] = is.getItemDamage();
                    sortList[pos++] = is.stackSize;
                } else {
                    sortList[pos++] = 0;
                    sortList[pos++] = 0;
                    sortList[pos++] = 0;
                }
            }
            return sortList;
        }
        return null;
    }

    public ItemStack getStackInSlotOnClosing(int par1) {
        if (this.barrelContents[par1] != null) {
            ItemStack var2 = this.barrelContents[par1];
            this.barrelContents[par1] = null;
            return var2;
        } else {
            return null;
        }
    }

    public short getFacing() {
        return this.facing;
    }

    public void setFacing(short facing) {
        this.facing = facing;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void setActive(boolean active) {
        if (this.active != active) {
            this.active = active;
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    public boolean isActive() {
        return this.active;
    }
}
