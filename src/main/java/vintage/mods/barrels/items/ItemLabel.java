package vintage.mods.barrels.items;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemLabel extends ItemBlock {

    private static final String[] labelNames = new String[]{"label.oak", "label.spruce", "label.birch", "label.jungle"};

    public ItemLabel(int id) {
        super(id);
        this.setHasSubtypes(true);
        this.setUnlocalizedName("label");
    }

    @Override
    public int getMetadata(int damageValue) {
        return damageValue;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        return labelNames[itemstack.getItemDamage()];
    }
}
