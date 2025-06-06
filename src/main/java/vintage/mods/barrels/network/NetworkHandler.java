package vintage.mods.barrels.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vintage.mods.barrels.Refs;
import vintage.mods.barrels.VintageBarrels;
import vintage.mods.barrels.tiles.TileEntityBarrel;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NetworkHandler implements IPacketHandler {

    @Override
    public void onPacketData(INetworkManager network, Packet250CustomPayload packet, Player player) {
        ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
        int packetId = dat.readByte();
        switch (packetId) {
            case 0:
                int x = dat.readInt();
                int y = dat.readInt();
                int z = dat.readInt();
                short facing = dat.readShort();
                boolean isActive = dat.readBoolean();
                byte typ = dat.readByte();
                boolean hasStacks = dat.readByte() != 0;
                int[] items = new int[0];
                if (hasStacks) {
                    items = new int[24];
                    for (int i = 0; i < items.length; i++) {
                        items[i] = dat.readInt();
                    }
                }
                World world = VintageBarrels.PROXY.getClientWorld();
                TileEntity te = world.getBlockTileEntity(x, y, z);
                if (te instanceof TileEntityBarrel) {
                    TileEntityBarrel entityBarrel = (TileEntityBarrel) te;
                    entityBarrel.handlePacketData(typ, items);
                    entityBarrel.setFacing(facing);
                    entityBarrel.setActive(isActive);
                }
                break;
            case 1:
                EntityPlayer entityPlayer = (EntityPlayer) player;
                String name = dat.readUTF().trim();
                ItemStack currentStack = entityPlayer.getCurrentEquippedItem();
                if (currentStack != null) {
                    NBTTagCompound tag = Refs.getOrCreateTag(currentStack);
                    tag.setString("labelName", name);
                }
        }
    }

    public static Packet getPacket(TileEntityBarrel tileEntityIronChest) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        int x = tileEntityIronChest.xCoord;
        int y = tileEntityIronChest.yCoord;
        int z = tileEntityIronChest.zCoord;
        short facing = tileEntityIronChest.getFacing();
        boolean isActive = tileEntityIronChest.isActive();
        int typ = tileEntityIronChest.getType().ordinal();
        int[] items = tileEntityIronChest.buildIntDataList();
        boolean hasStacks = (items != null);
        try {
            dos.writeByte(0);
            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeInt(z);
            dos.writeShort(facing);
            dos.writeBoolean(isActive);
            dos.writeByte(typ);
            dos.writeByte(hasStacks ? 1 : 0);
            if (hasStacks) {
                for (int i = 0; i < 24; i++) {
                    dos.writeInt(items[i]);
                }
            }
        } catch (IOException ignored) {}
        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = Refs.ID;
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }
}
