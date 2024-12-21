package vintage.mods.barrels.items.namingtool;

import mods.vintage.core.platform.lang.FormattedTranslator;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vintage.mods.barrels.Refs;
import vintage.mods.barrels.VintageBarrels;
import vintage.mods.barrels.VintageBarrelsConfig;
import vintage.mods.barrels.proxy.ClientProxy;
import vintage.mods.barrels.tiles.TileEntityLabel;

import java.util.List;

public class ItemNameTag extends Item {

    public ItemNameTag() {
        super(VintageBarrelsConfig.NAMING_TOOL_ID);
        this.setMaxStackSize(1);
        this.setCreativeTab(VintageBarrels.TAB);
        this.setUnlocalizedName("namingTool");
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean debug) {
        NBTTagCompound tag = Refs.getOrCreateTag(stack);
        String labelName = tag.getString("labelName");
        list.add(FormattedTranslator.GRAY.format("namingTool.tooltip.desc"));
        if (!"".equals(labelName)) {
            list.add(FormattedTranslator.AQUA.format("tooltip.namingTool.labelName", FormattedTranslator.GOLD.literal(labelName)));
        }
        if (ClientProxy.isShiftKeyDown()) {
            list.add(FormattedTranslator.GRAY.format("namingTool.message.info.click.block", FormattedTranslator.GOLD.format("key.sneak"), FormattedTranslator.GOLD.format("key.mouse.right"), FormattedTranslator.GOLD.format("namingTool.message.info.click.block.action")));
            list.add(FormattedTranslator.RED.format("namingTool.tooltip.desc1"));
            list.add(FormattedTranslator.GRAY.format("namingTool.message.action.info", FormattedTranslator.GOLD.format("key.mouse.right")));
        } else {
            list.add(FormattedTranslator.GRAY.format("namingTool.message.info.press", FormattedTranslator.WHITE.format("key.sneak")));
        }
        super.addInformation(stack, player, list, debug);
    }

    @Override
    public String getItemDisplayName(ItemStack stack) {
        NBTTagCompound tag = Refs.getOrCreateTag(stack);
        String labelName = tag.getString("labelName");
        if (!"".equals(labelName)) {
            return String.format("%s (%s)", super.getItemDisplayName(stack), FormattedTranslator.AQUA.literal(labelName));
        } else return super.getItemDisplayName(stack);
    }

    @Override
    public void registerIcons(IconRegister register) {
        this.itemIcon = register.registerIcon(Refs.ID + ":namingTool");
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xOff, float yOff, float zOff) {
        if (world.isRemote) return false;
        TileEntity blockEntity = world.getBlockTileEntity(x, y, z);
        NBTTagCompound newTag = Refs.getOrCreateTag(stack);
        String labelName = newTag.getString("labelName");
        if (blockEntity instanceof TileEntityLabel) {
            TileEntityLabel labelBlockEntity = (TileEntityLabel) blockEntity;
            ItemStack existingTag = labelBlockEntity.getStackInSlot(3);
            if (existingTag != null && labelName != null) {
                NBTTagCompound tag = Refs.getOrCreateTag(existingTag);
                tag.setString("labelName", labelName);
                world.markBlockForUpdate(x, y, z);
            }
            return true;
        }
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (world.isRemote) {
            if (!player.isSneaking()) {
                player.openGui(VintageBarrels.instance, 10, world, 0, 0, 0);
            }
        }
        return stack;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityLiving entity) {
        NBTTagCompound tag = Refs.getOrCreateTag(stack);
        String name = tag.getString("labelName");
        if (!"".equals(name)) {
            entity.func_94058_c(name);
            return true;
        }
        return false;
    }
}
