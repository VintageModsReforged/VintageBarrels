package vintage.mods.barrels.items.namingtool;

import mods.vintage.core.platform.config.IItemBlockIDProvider;
import mods.vintage.core.platform.lang.Translator;
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

public class ItemNamingTool extends Item implements IItemBlockIDProvider {

    public ItemNamingTool() {
        super(VintageBarrelsConfig.NAMING_TOOL_ID.get());
        this.setMaxStackSize(1);
        this.setCreativeTab(VintageBarrels.TAB);
        this.setTextureFile("/mods/vintagebarrels/textures/item_textures.png");
        this.setItemName("namingTool");
        this.setIconIndex(48);
    }


    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean debug) {
        NBTTagCompound tag = Refs.getOrCreateTag(stack);
        String labelName = tag.getString("labelName");
        list.add(Translator.GRAY.format("namingTool.tooltip.desc"));
        if (!"".equals(labelName)) {
            list.add(Translator.AQUA.format("tooltip.namingTool.labelName", Translator.GOLD.literal(labelName)));
        }
        if (ClientProxy.isShiftKeyDown()) {
            list.add(Translator.GRAY.format("namingTool.message.info.click.block", Translator.GOLD.format("key.sneak"), Translator.GOLD.format("key.mouse.right"), Translator.GOLD.format("namingTool.message.info.click.block.action")));
            list.add(Translator.RED.format("namingTool.tooltip.desc1"));
            list.add(Translator.GRAY.format("namingTool.message.action.info", Translator.GOLD.format("key.mouse.right")));
        } else {
            list.add(Translator.GRAY.format("namingTool.message.info.press", Translator.WHITE.format("key.sneak")));
        }
        super.addInformation(stack, player, list, debug);
    }

    @Override
    public String getItemDisplayName(ItemStack stack) {
        NBTTagCompound tag = Refs.getOrCreateTag(stack);
        String labelName = tag.getString("labelName");
        if (!"".equals(labelName)) {
            return Translator.WHITE.format("%s (%s)", super.getItemDisplayName(stack), Translator.AQUA.literal(labelName));
        } else return super.getItemDisplayName(stack);
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
}
