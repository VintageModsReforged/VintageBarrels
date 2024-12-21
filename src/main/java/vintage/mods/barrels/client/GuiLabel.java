package vintage.mods.barrels.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import vintage.mods.barrels.Refs;
import vintage.mods.barrels.blocks.label.ContainerLabel;
import vintage.mods.barrels.tiles.TileEntityLabel;

public class GuiLabel extends GuiContainer {

    public GuiLabel(InventoryPlayer inventoryPlayer, TileEntityLabel woodLabel) {
        super(new ContainerLabel(inventoryPlayer, woodLabel));
    }

    protected void drawGuiContainerForegroundLayer(int var1, int var2) {
        this.fontRenderer.drawString("Wooden Label", 8, 6, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        int texture = this.mc.renderEngine.getTexture(Refs.WOODLABELGUI_PNG);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(texture);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }
}
