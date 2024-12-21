package vintage.mods.barrels.blocks.label;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vintage.mods.barrels.BlocksItems;

public class SlotLabelTag extends SlotLabel {

    public SlotLabelTag(ContainerLabel labelContain, IInventory iInventory, int i, int j, int k) {
        super(labelContain, iInventory, i, j, k);
    }

    public boolean isItemValid(ItemStack stack) {
        return stack.itemID == BlocksItems.NAME_TAG.itemID;
    }
}
