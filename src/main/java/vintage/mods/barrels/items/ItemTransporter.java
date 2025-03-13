package vintage.mods.barrels.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.vintage.core.helpers.BlockHelper;
import mods.vintage.core.helpers.Utils;
import mods.vintage.core.platform.lang.FormattedTranslator;
import mods.vintage.core.platform.lang.Translator;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import vintage.mods.barrels.BarrelTransporters;
import vintage.mods.barrels.Refs;
import vintage.mods.barrels.VintageBarrels;
import vintage.mods.barrels.VintageBarrelsConfig;
import vintage.mods.barrels.blocks.BlockBarrel;

import java.util.List;
import java.util.Locale;

public class ItemTransporter extends Item {

    public Icon[] EMPTY;
    public Icon[] FULL;
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
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        int size = BarrelTransporters.VALUES.length;
        this.EMPTY = new Icon[size];
        this.FULL = new Icon[size];
        for (BarrelTransporters type : BarrelTransporters.VALUES) {
            this.EMPTY[type.ordinal()] = register.registerIcon(Refs.ID + ":transporter/empty/" + type.name().toLowerCase(Locale.ROOT));
            this.FULL[type.ordinal()] = register.registerIcon(Refs.ID + ":transporter/full/" + type.name().toLowerCase(Locale.ROOT));
        }
    }

    @Override
    public Icon getIcon(ItemStack stack, int pass) {
        NBTTagCompound tag = Refs.getOrCreateTag(stack);
        if (tag.getBoolean(FULL_TAG)) {
            return this.FULL[this.TYPE.ordinal()];
        } else return this.EMPTY[this.TYPE.ordinal()];
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
                world.setBlock(x, y, z, 0, 0, 3);
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
        } else if (world.canPlaceEntityOnSide(stack.itemID, x, y, z, false, facing, player, stack)) {
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
        if (!world.setBlock(x, y, z, stack.itemID, metadata, 3)) {
            return false;
        } else {
            if (world.getBlockId(x, y, z) == stack.itemID) {
                Block.blocksList[stack.itemID].onBlockPlacedBy(world, x, y, z, player, stack);
                Block.blocksList[stack.itemID].onPostBlockPlaced(world, x, y, z, metadata);
            }

            return true;
        }
    }

    private boolean isChestAt(World world, int x, int y, int z) {
        Block block = BlockHelper.getBlock(world, x, y, z);
        return block == Block.chest ||
                block instanceof BlockBarrel ||
                Utils.instanceOf(block, "cpw.mods.ironchest.BlockIronChest") ||
                Utils.instanceOf(block, "need4speed402.mods.barrels.BlockBarrel") ||
                Utils.instanceOf(block, "cubex2.mods.multipagechest.BlockMultiPageChest");
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

    private void moveItemsIntoStack(IInventory chest, ItemStack transporter) {
        if (transporter.stackTagCompound == null) {
            transporter.setTagCompound(new NBTTagCompound());
        }

        NBTTagList nbtList = new NBTTagList();
        for (int i = 0; i < chest.getSizeInventory(); ++i) {
            if (chest.getStackInSlot(i) != null) { // skip empty slots

                // create a tag for each entry of our inventory
                NBTTagCompound itemTag = new NBTTagCompound();
                ItemStack stack = chest.getStackInSlot(i).copy();

                // each tag will contain 3 infos
                // slot
                itemTag.setShort("Slot", (short) i);
                // stack content
                itemTag.setTag("Content", stack.writeToNBT(new NBTTagCompound()));
                // stack amount for items that have stackSize > maxStackSize like barrels, etc.
                itemTag.setInteger("Amount", stack.stackSize);
                // set slot content to null after serializing
                chest.setInventorySlotContents(i, null);
                // add entry tag to list
                nbtList.appendTag(itemTag);
            }
        }
        // add newly created list to transporter under Items tag
        transporter.stackTagCompound.setTag("Items", nbtList);
    }


    private void moveItemsIntoChest(ItemStack transporter, IInventory chest) {
        // get the common list named Items
        NBTTagList nbtList = transporter.stackTagCompound.getTagList("Items");
        for (int i = 0; i < nbtList.tagCount(); ++i) {
            // get each entry tag
            NBTTagCompound itemTag = (NBTTagCompound) nbtList.tagAt(i);

            // read slot info
            int slot = itemTag.getShort("Slot");
            // read stackSize info
            int stackCount = itemTag.getInteger("Amount");
            // read stack info
            ItemStack stack = ItemStack.loadItemStackFromNBT(itemTag.getCompoundTag("Content"));
            if (stack != null) {
                //
                stack.stackSize = stackCount;
                if (slot >= 0 && slot < chest.getSizeInventory()) {
                    chest.setInventorySlotContents(slot, stack);
                }
            }
        }
        transporter.stackTagCompound.removeTag("Items");
    }


    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean debug) {
        super.addInformation(stack, player, list, debug);
        NBTTagCompound tag = Refs.getOrCreateTag(stack);
        boolean isFull = tag.getBoolean(FULL_TAG);

        if (isFull && stack.stackTagCompound != null) {
            int numItems = 0;
            NBTTagList nbtList = stack.stackTagCompound.getTagList("Items");

            for (int i = 0; i < nbtList.tagCount(); i++) {
                NBTTagCompound itemTag = (NBTTagCompound) nbtList.tagAt(i);
                ItemStack itemStack = ItemStack.loadItemStackFromNBT(itemTag.getCompoundTag("Content"));
                int stackCount = itemTag.getInteger("Amount");

                if (itemStack != null) {
                    numItems += stackCount;
                }
            }

            list.add(FormattedTranslator.AQUA.format(
                    "tooltip.transporter.content",
                    FormattedTranslator.YELLOW.literal(String.valueOf(numItems))
            ));
        }
    }
}
