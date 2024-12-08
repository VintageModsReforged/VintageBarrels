package vintage.mods.barrels.items;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import vintage.mods.barrels.BarrelType;

import java.util.Locale;

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
    public String getUnlocalizedName(ItemStack stack) {
        BarrelType type = BarrelType.values()[stack.getItemDamage()];
        return "barrel." + type.name().toLowerCase(Locale.ROOT);
    }

    @Override
    public String getItemDisplayName(ItemStack stack) {
        BarrelType type = BarrelType.values()[stack.getItemDamage()];
        return type.formatter.literal(super.getItemDisplayName(stack));
    }
}
