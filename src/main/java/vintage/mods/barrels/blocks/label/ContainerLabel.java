package vintage.mods.barrels.blocks.label;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import vintage.mods.barrels.tiles.TileEntityLabel;
import vintage.mods.barrels.utils.IFilterSlot;

public class ContainerLabel extends Container {

    protected TileEntityLabel tileEntity;
    protected SlotLabel labelSlot;

    public ContainerLabel(InventoryPlayer inventoryPlayer, TileEntityLabel tile) {
        this.tileEntity = tile;
        this.addSlotToContainer(this.labelSlot = new SlotLabel(this, this.tileEntity, 0, 80, 45));
        this.addSlotToContainer(this.labelSlot = new SlotLabel(this, this.tileEntity, 1, 35, 26));
        this.addSlotToContainer(this.labelSlot = new SlotLabel(this, this.tileEntity, 2, 125, 26));
        this.addSlotToContainer(this.labelSlot = new SlotLabelTag(this, this.tileEntity, 3, 152, 62));
        this.bindPlayerInventory(inventoryPlayer);
    }

    @Override
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
    public ItemStack slotClick(int slotIndex, int mouseButton, int modifier, EntityPlayer player) {
        Slot slot = slotIndex < 0 ? null : (Slot) this.inventorySlots.get(slotIndex);
        if (slot instanceof IFilterSlot) { // our slots
            return setGhostStack(slot, mouseButton, modifier, player);
        }
        return super.slotClick(slotIndex, mouseButton, modifier, player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int i) {
        return null;
    }

    public ItemStack setGhostStack(Slot slot, int mouseButton, int modifier, EntityPlayer player) {
        ItemStack stack = null;
        if (mouseButton == 2) {
            slot.putStack(null);
        } else if (mouseButton == 0 || mouseButton == 1) {
            InventoryPlayer playerInv = player.inventory;
            slot.onSlotChanged();
            ItemStack stackSlot = slot.getStack();
            ItemStack stackHeld = playerInv.getItemStack();
            if (stackSlot != null) {
                stack = stackSlot.copy();
            }
            if (stackSlot == null) {
                if (stackHeld != null && slot.isItemValid(stackHeld)) {
                    setFilterSlot(slot, stackHeld, mouseButton);
                }
            } else if (stackHeld == null) {
                changeFilterSlot(slot, mouseButton, modifier);
                slot.onPickupFromSlot(player, playerInv.getItemStack());
            } else if (slot.isItemValid(stackHeld)) {
                if (stackSlot.isItemEqual(stackHeld)) {
                    changeFilterSlot(slot, mouseButton, modifier);
                } else {
                    setFilterSlot(slot, stackHeld, mouseButton);
                }
            }
        }
        return stack;
    }

    protected void changeFilterSlot(Slot slot, int mouseButton, int modifier) {
        ItemStack stackSlot = slot.getStack();
        int stackSize;
        if (modifier == 1) {
            stackSize = mouseButton == 0 ? (stackSlot.stackSize + 1) / 2 : stackSlot.stackSize * 2;
        } else {
            stackSize = mouseButton == 0 ? stackSlot.stackSize - 1 : stackSlot.stackSize + 1;
        }

        if (stackSize > slot.getSlotStackLimit()) {
            stackSize = slot.getSlotStackLimit();
        }

        stackSlot.stackSize = stackSize;

        if (stackSlot.stackSize <= 0) {
            slot.putStack(null);
        }
    }

    protected void setFilterSlot(Slot slot, ItemStack stackHeld, int mouseButton) {
        int stackSize = mouseButton == 0 ? stackHeld.stackSize : 1;
        if (stackSize > slot.getSlotStackLimit()) {
            stackSize = slot.getSlotStackLimit();
        }
        ItemStack phantomStack = stackHeld.copy();
        phantomStack.stackSize = stackSize;

        slot.putStack(phantomStack);
    }
}
