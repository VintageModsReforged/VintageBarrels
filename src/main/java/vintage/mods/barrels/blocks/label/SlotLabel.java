package vintage.mods.barrels.blocks.label;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotLabel extends Slot {

    final ContainerLabel labelContainer;

    public SlotLabel(ContainerLabel labelContain, IInventory iInventory, int i, int j, int k) {
        super(iInventory, i, j, k);
        this.labelContainer = labelContain;
    }

    public boolean isItemValid(ItemStack stack) {
        return true;
    }

    public int getSlotStackLimit() {
        return 64;
    }
}
