package vintage.mods.barrels.client;

import mods.vintage.core.platform.lang.Translator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import org.lwjgl.opengl.GL11;
import vintage.mods.barrels.BarrelType;
import vintage.mods.barrels.blocks.container.ContainerBarrelBase;
import vintage.mods.barrels.tiles.TileEntityBarrel;


public class GUIChest extends GuiContainer {
    public enum GuiType {
        IRON(176, 222, "iron", BarrelType.IRON),
        GOLD(230, 240, "gold", BarrelType.GOLD),
        DIAMOND(248, 256, "diamond", BarrelType.DIAMOND),
        COPPER(176, 204, "copper", BarrelType.COPPER),
        SILVER(230, 222, "silver", BarrelType.SILVER),
        CRYSTAL(248, 256, "diamond", BarrelType.CRYSTAL),
        OBSIDIAN(248, 256, "diamond", BarrelType.OBSIDIAN),
        WOOD(176, 168, "wood", BarrelType.WOOD);

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
            this.locale = Translator.format("barrel." + this.name().toLowerCase() + ".name");
        }

        private Container makeContainer(IInventory player, IInventory chest) {
            return new ContainerBarrelBase(player, chest, mainType, xSize, ySize);
        }

        public static GUIChest buildGUI(BarrelType type, IInventory playerInventory, TileEntityBarrel barrel) {
            return new GUIChest(values()[type.ordinal()], playerInventory, barrel);
        }
    }

    private final GuiType type;

    private GUIChest(GuiType type, IInventory player, IInventory chest) {
        super(type.makeContainer(player, chest));
        this.type = type;
        this.xSize = type.xSize;
        this.ySize = type.ySize;
        this.allowUserInput = false;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        this.fontRenderer.drawString(type.locale, 8, 6, 4210752);
        int xPos = 8;
        switch (type) {
            case SILVER: case GOLD: xPos = 35; break;
            case DIAMOND: case CRYSTAL: case OBSIDIAN: xPos = 44; break;
        }
        this.fontRenderer.drawString(Translator.format("container.inventory"), xPos, this.ySize - 96 + 2, 4210752);
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

    public int getRowLength() {
        return this.type.mainType.getRowLength();
    }
}
