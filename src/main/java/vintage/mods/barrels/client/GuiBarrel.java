package vintage.mods.barrels.client;

import invtweaks.api.ContainerGUI;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import org.lwjgl.opengl.GL11;
import vintage.mods.barrels.BarrelType;
import vintage.mods.barrels.blocks.container.ContainerBarrelBase;
import vintage.mods.barrels.tiles.TileEntityBarrel;

import java.util.Locale;

@ContainerGUI
public class GuiBarrel extends GuiContainer {
    public enum GuiType {
        IRON(184, 202, "iron", BarrelType.IRON),
        GOLD(184, 256, "gold", BarrelType.GOLD),
        DIAMOND(238, 256, "diamond", BarrelType.DIAMOND),
        COPPER(184, 184, "copper", BarrelType.COPPER),
        SILVER(184, 238, "silver", BarrelType.SILVER),
        CRYSTAL(238, 256, "diamond", BarrelType.CRYSTAL),
        OBSIDIAN(238, 256, "diamond", BarrelType.OBSIDIAN),
        WOOD(184, 148, "wood", BarrelType.WOOD);

        private int xSize;
        private int ySize;
        private String guiTexture;
        private String locale;
        private BarrelType mainType;

        GuiType(int xSize, int ySize, String guiTexture, BarrelType mainType) {
            this.xSize = xSize;
            this.ySize = ySize;
            this.guiTexture = "/mods/vintagebarrels/textures/gui/" + guiTexture + ".png";
            this.mainType = mainType;
            this.locale = mainType.formatter.format("barrel." + this.name().toLowerCase(Locale.ROOT) + ".name");
        }

        private Container makeContainer(IInventory player, IInventory chest) {
            return new ContainerBarrelBase(player, chest, mainType, xSize, ySize);
        }

        public static GuiBarrel buildGUI(BarrelType type, IInventory playerInventory, TileEntityBarrel barrel) {
            return new GuiBarrel(values()[type.ordinal()], playerInventory, barrel);
        }
    }

    private final GuiType type;

    private GuiBarrel(GuiType type, IInventory player, IInventory chest) {
        super(type.makeContainer(player, chest));
        this.type = type;
        this.xSize = type.xSize;
        this.ySize = type.ySize;
        this.allowUserInput = false;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        this.fontRenderer.drawString(type.locale, this.xSize / 2 - this.fontRenderer.getStringWidth(type.locale) / 2, -8, 0);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(type.guiTexture);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
