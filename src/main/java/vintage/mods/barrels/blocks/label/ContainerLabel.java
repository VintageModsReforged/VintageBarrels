package vintage.mods.barrels.blocks.label;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import vintage.mods.barrels.tiles.TileEntityLabel;

public class ContainerLabel extends Container {

    protected TileEntityLabel tileEntity;
    protected SlotLabel labelSlot;

    public ContainerLabel(InventoryPlayer inventoryPlayer, TileEntityLabel tile) {
        this.tileEntity = tile;
        this.addSlotToContainer(this.labelSlot = new SlotLabel(this, this.tileEntity, 0, 80, 45));
        this.addSlotToContainer(this.labelSlot = new SlotLabel(this, this.tileEntity, 1, 35, 26));
        this.addSlotToContainer(this.labelSlot = new SlotLabel(this, this.tileEntity, 2, 125, 26));
        this.bindPlayerInventory(inventoryPlayer);
    }

    public boolean canInteractWith(EntityPlayer player) {
        return this.tileEntity.isUseableByPlayer(player);
    }

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
        }

    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
        ItemStack stack = null;
        Slot slotObject = (Slot)this.inventorySlots.get(slot);
        if (slotObject != null && slotObject.getHasStack()) {
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();
            if (slot < 3) {
                if (!this.mergeItemStack(stackInSlot, 3, 39, true)) {
                    return null;
                }
            } else if (!this.mergeItemStack(stackInSlot, 0, 3, false)) {
                return null;
            }

            if (stackInSlot.stackSize == 0) {
                slotObject.putStack(null);
            } else {
                slotObject.onSlotChanged();
            }
            if (stackInSlot.stackSize == stack.stackSize) {
                return null;
            }
            slotObject.onPickupFromSlot(player, stackInSlot);
        }

        return stack;
    }
}
