package vintage.mods.barrels.items;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import vintage.mods.barrels.BarrelType;

public class ItemBarrel extends ItemBlock {

    public ItemBarrel(int id) {
        super(id);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int i) {
        return BarrelType.validateMeta(i);
    }

    @Override
    public String getItemNameIS(ItemStack stack) {
        return "barrel." + BarrelType.values()[stack.getItemDamage()].name().toLowerCase();
    }
}
