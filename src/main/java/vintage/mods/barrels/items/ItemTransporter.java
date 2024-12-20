package vintage.mods.barrels.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.vintage.core.helpers.BlockHelper;
import mods.vintage.core.helpers.Utils;
import mods.vintage.core.platform.lang.FormattedTranslator;
import mods.vintage.core.platform.lang.Translator;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import vintage.mods.barrels.BarrelTransporters;
import vintage.mods.barrels.Refs;
import vintage.mods.barrels.VintageBarrels;
import vintage.mods.barrels.VintageBarrelsConfig;
import vintage.mods.barrels.blocks.BlockNewBarrel;

import java.util.List;
import java.util.Locale;

public class ItemTransporter extends Item {

    public static final String FULL_TAG = "full";
    public BarrelTransporters TYPE;

    public ItemTransporter(int id, BarrelTransporters type) {
        super(id);
        this.setMaxStackSize(1);
        this.setMaxDamage(type.uses);
        this.setCreativeTab(VintageBarrels.TAB);
        this.TYPE = type;
    }

    @Override
    public String getItemDisplayName(ItemStack stack) {
        return Translator.format("item.transporter.name", Translator.format("transporter." + this.TYPE.name().toLowerCase(Locale.ROOT)));
    }

    @Override
    public int getIconIndex(ItemStack stack, int pass) {
        NBTTagCompound tag = Refs.getOrCreateTag(stack);
        if (tag.getBoolean(FULL_TAG)) {
            return 32 + this.TYPE.ordinal();
        } else return 16 + this.TYPE.ordinal();
    }

    @Override
    public String getTextureFile() {
        return "/mods/vintagebarrels/textures/item_textures.png";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean hasEffect(ItemStack stack) {
        return Refs.getOrCreateTag(stack).getBoolean(FULL_TAG);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean par5) {
        NBTTagCompound tag = Refs.getOrCreateTag(stack);
        boolean isFull = tag.getBoolean(FULL_TAG);
        if (isFull && entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (player.capabilities.isCreativeMode) return;

            if (player.getActivePotionEffect(Potion.moveSlowdown) == null || player.getActivePotionEffect(Potion.moveSlowdown).getDuration() < 20) {
                player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(), 60, 2));
            }

            if (player.getActivePotionEffect(Potion.digSlowdown) == null || player.getActivePotionEffect(Potion.digSlowdown).getDuration() < 20) {
                player.addPotionEffect(new PotionEffect(Potion.digSlowdown.getId(), 60, 3));
            }

            if (player.getActivePotionEffect(Potion.jump) == null || player.getActivePotionEffect(Potion.jump).getDuration() < 20) {
                player.addPotionEffect(new PotionEffect(Potion.jump.getId(), 60, -2));
            }

            if (player.getActivePotionEffect(Potion.hunger) == null || player.getActivePotionEffect(Potion.hunger).getDuration() < 20) {
                player.addPotionEffect(new PotionEffect(Potion.hunger.getId(), 60, 0));
            }

            if (VintageBarrelsConfig.RESTRICT_TRANSPORTATION) {
                if (!(player.openContainer instanceof ContainerPlayer)) { // prevent inventories that aren't player inventory from opening
                    player.closeScreen();
                }
            }
        }
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xOffset, float yOffset, float zOffset) {
        NBTTagCompound tag = Refs.getOrCreateTag(stack);
        boolean isFull = tag.getBoolean(FULL_TAG);
        if (!isFull && this.isChestAt(world, x, y, z)) {
            IInventory chestInventory = (IInventory) world.getBlockTileEntity(x, y, z);
            if (chestInventory != null) {
                Block chest = BlockHelper.getBlock(world, x, y, z);
                this.moveItemsIntoStack(chestInventory, stack);
                this.saveContainerInfo(stack, world, x, y, z);
                tag.setBoolean(FULL_TAG, true);
                world.setBlockAndMetadataWithNotify(x, y, z, 0, 0);
                world.playSoundEffect((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F, chest.stepSound.getStepSound(), (chest.stepSound.getVolume() + 1.0F) / 2.0F, chest.stepSound.getPitch() * 0.5F);
            }
        } else if (isFull) {
            int containerID = this.getContainerInfo(stack)[0];
            int containerMeta = this.getContainerInfo(stack)[1];
            ItemStack containerStack = new ItemStack(containerID, 1, containerMeta);
            int[] chestCoords = this.placeChestBlock(containerStack, player, world, x, y, z, side);
            if (chestCoords != null) {
                IInventory chest = (IInventory) world.getBlockTileEntity(chestCoords[0], chestCoords[1], chestCoords[2]);
                this.moveItemsIntoChest(stack, chest);
                tag.setBoolean(FULL_TAG, false);
                stack.damageItem(1, player);
                if (stack.getItemDamage() == stack.getMaxDamage()) {
                    player.renderBrokenItemStack(stack);
                    player.destroyCurrentEquippedItem();
                }
            }
        }
        return false;
    }

    private int[] placeChestBlock(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int facing) {
        int blockId = world.getBlockId(x, y, z);
        if (blockId == Block.snow.blockID && (world.getBlockMetadata(x, y, z) & 7) < 1) {
            facing = 1;
        } else if (blockId != Block.vine.blockID && blockId != Block.tallGrass.blockID && blockId != Block.deadBush.blockID && (Block.blocksList[blockId] == null || !Block.blocksList[blockId].isBlockReplaceable(world, x, y, z))) {
            if (facing == 0) {
                --y;
            }

            if (facing == 1) {
                ++y;
            }

            if (facing == 2) {
                --z;
            }

            if (facing == 3) {
                ++z;
            }

            if (facing == 4) {
                --x;
            }

            if (facing == 5) {
                ++x;
            }
        }

        if (!player.canPlayerEdit(x, y, z, facing, stack)) {
            return null;
        } else if (y == 255 && Block.blocksList[stack.itemID].blockMaterial.isSolid()) {
            return null;
        } else if (world.canPlaceEntityOnSide(stack.itemID, x, y, z, false, facing, player)) {
            Block block = Block.blocksList[stack.itemID];
            int meta = stack.getItemDamage();
            int placed = Block.blocksList[stack.itemID].onBlockPlaced(world, x, y, z, facing, 0, 0, 0, meta);
            if (this.placeBlockAt(stack, player, world, x, y, z, placed)) {
                world.playSoundEffect((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F, block.stepSound.getPlaceSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
            }
            return new int[]{x, y, z};
        } else {
            return null;
        }
    }

    private boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int metadata) {
        if (!world.setBlockAndMetadata(x, y, z, stack.itemID, metadata)) {
            return false;
        } else {
            if (world.getBlockId(x, y, z) == stack.itemID) {
                Block.blocksList[stack.itemID].onBlockPlacedBy(world, x, y, z, player);
                Block.blocksList[stack.itemID].onPostBlockPlaced(world, x, y, z, metadata);
            }

            return true;
        }
    }

    private boolean isChestAt(World world, int x, int y, int z) {
        Block block = BlockHelper.getBlock(world, x, y, z);
        if (block == Block.chest) {
            return true;
        } else if (Utils.instanceOf(block, "cpw.mods.ironchest.BlockIronChest")) {
            return true;
        } else if (block instanceof BlockNewBarrel) {
            return true;
        } else return Utils.instanceOf(block, "cubex2.mods.multipagechest.BlockMultiPageChest");
    }

    public void saveContainerInfo(ItemStack transporter, World world, int x, int y, int z) {
        NBTTagCompound tag = Refs.getOrCreateTag(transporter);
        int blockID = world.getBlockId(x, y, z);
        int metadata = world.getBlockMetadata(x, y, z);
        tag.setInteger("ContainerID", blockID);
        tag.setInteger("ContainerMeta", metadata);
    }

    public int[] getContainerInfo(ItemStack transporter) {
        NBTTagCompound tag = Refs.getOrCreateTag(transporter);
        return new int[]{tag.getInteger("ContainerID"), tag.getInteger("ContainerMeta")};
    }

    private void moveItemsIntoStack(IInventory chest, ItemStack stack) {
        if (stack.stackTagCompound == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
        NBTTagList nbtList = new NBTTagList();
        for (int i = 0; i < chest.getSizeInventory(); ++i) {
            if (chest.getStackInSlot(i) != null) {
                NBTTagCompound slotTag = new NBTTagCompound();
                slotTag.setShort("Slot", (short) i);
                chest.getStackInSlot(i).copy().writeToNBT(slotTag);
                chest.setInventorySlotContents(i, null);
                nbtList.appendTag(slotTag);
            }
        }
        stack.stackTagCompound.setTag("Items", nbtList);
    }

    private void moveItemsIntoChest(ItemStack stack, IInventory chest) {
        NBTTagList nbtList = stack.stackTagCompound.getTagList("Items");
        for (int i = 0; i < nbtList.tagCount(); ++i) {
            NBTTagCompound tag = (NBTTagCompound) nbtList.tagAt(i);
            NBTBase slotTag = tag.getTag("Slot");
            int j = -1;
            if (slotTag instanceof NBTTagByte) {
                j = tag.getByte("Slot") & 255;
            } else {
                j = tag.getShort("Slot");
            }
            if (j >= 0 && j < chest.getSizeInventory()) {
                chest.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(tag).copy());
            }
        }
        stack.stackTagCompound.getTags().remove(nbtList);
    }

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean debug) {
        super.addInformation(stack, player, list, debug);
        NBTTagCompound tag = Refs.getOrCreateTag(stack);
        boolean isFull = tag.getBoolean(FULL_TAG);
        if (isFull) {
            int numItems = 0;
            NBTTagList nbtList = stack.stackTagCompound.getTagList("Items");
            for (int i = 0; i < nbtList.tagCount(); ++i) {
                NBTTagCompound inventoryTag = (NBTTagCompound) nbtList.tagAt(i);
                NBTBase slotTag = inventoryTag.getTag("Slot");
                int j = -1;
                if (slotTag instanceof NBTTagByte) {
                    j = inventoryTag.getByte("Slot") & 255;
                } else {
                    j = inventoryTag.getShort("Slot");
                }

                if (j >= 0) {
                    ItemStack itemstack = ItemStack.loadItemStackFromNBT(inventoryTag);
                    if (itemstack != null) {
                        numItems += itemstack.stackSize;
                    }
                }
            }
            list.add(FormattedTranslator.AQUA.format("tooltip.transporter.content", FormattedTranslator.YELLOW.literal(numItems + "")));
        }
    }
}
