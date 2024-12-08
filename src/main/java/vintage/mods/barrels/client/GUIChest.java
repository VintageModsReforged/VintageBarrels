package vintage.mods.barrels.client;

import mods.vintage.core.platform.lang.FormattedTranslator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import org.lwjgl.opengl.GL11;
import vintage.mods.barrels.BarrelType;
import vintage.mods.barrels.blocks.container.ContainerBarrelBase;
import vintage.mods.barrels.tiles.TileEntityBarrel;

public class GUIChest extends GuiContainer {
    public enum GUI {
        IRON(184, 202, "iron", BarrelType.IRON, FormattedTranslator.GREEN),
        GOLD(184, 256, "gold", BarrelType.GOLD, FormattedTranslator.YELLOW),
        DIAMOND(238, 256, "diamond", BarrelType.DIAMOND, FormattedTranslator.DARK_AQUA),
        COPPER(184, 184, "copper", BarrelType.COPPER, FormattedTranslator.GOLD),
        SILVER(184, 238, "silver", BarrelType.SILVER, FormattedTranslator.AQUA),
        CRYSTAL(238, 256, "diamond", BarrelType.CRYSTAL, FormattedTranslator.AQUA),
        OBSIDIAN(238, 256, "diamond", BarrelType.OBSIDIAN, FormattedTranslator.BLUE),
        WOOD(184, 148, "wood", BarrelType.WOOD, FormattedTranslator.DARK_GRAY);

        private int xSize;
        private int ySize;
        private String guiTexture;
        private String locale;
        private BarrelType mainType;

        GUI(int xSize, int ySize, String guiTexture, BarrelType mainType, FormattedTranslator formatter) {
            this.xSize = xSize;
            this.ySize = ySize;
            this.guiTexture = "/mods/vintagebarrels/textures/gui/" + guiTexture + ".png";
            this.mainType = mainType;
            this.locale = formatter.format("barrel." + this.name().toLowerCase() + ".name");
        }

        private Container makeContainer(IInventory player, IInventory chest) {
            return new ContainerBarrelBase(player, chest, mainType, xSize, ySize);
        }

        public static GUIChest buildGUI(BarrelType type, IInventory playerInventory, TileEntityBarrel barrel) {
            return new GUIChest(values()[type.ordinal()], playerInventory, barrel);
        }
    }

    private final GUI type;

    private GUIChest(GUI type, IInventory player, IInventory chest) {
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
        int texture = this.mc.renderEngine.getTexture(type.guiTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(texture);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
