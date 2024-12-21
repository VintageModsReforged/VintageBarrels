package vintage.mods.barrels.utils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiCustomButton extends GuiButton {

    protected String texture;

    protected int uLoc;
    protected int vLoc;
    protected int uHoverLoc;
    protected int vHoverLoc;
    protected int color;
    protected int hoverColor;
    protected boolean isHovering;

    public GuiCustomButton(int id, int xLoc, int yLoc, int width, int height, int uLoc, int vLoc, int uHoverLoc, int vHoverLoc, String text, int color, int hoverColor) {
        super(id, xLoc, yLoc, width, height, text);
        this.enabled = true;
        this.drawButton = true;
        this.id = id;
        this.xPosition = xLoc;
        this.yPosition = yLoc;
        this.width = width;
        this.height = height;
        this.uLoc = uLoc;
        this.vLoc = vLoc;
        this.uHoverLoc = uHoverLoc;
        this.vHoverLoc = vHoverLoc;
        this.displayString = text;
        this.color = color;
        this.hoverColor = hoverColor;
        this.texture = "/mods/vintagebarrels/textures/gui/namingTool.png";
    }

    @Override
    public void drawButton(Minecraft mc, int xLoc, int yLoc) {
        if (drawButton) {
            FontRenderer fr = mc.fontRenderer;
            if (texture != null) {
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                mc.renderEngine.bindTexture(texture);

            }
            isHovering = xLoc >= xPosition && yLoc >= yPosition && xLoc < xPosition + width && yLoc < yPosition + height;
            int hoverState = this.getHoverState(isHovering);
            if (hoverState == 2) {
                this.drawTexturedModalRect(xPosition, yPosition, uHoverLoc, vHoverLoc, width, height);
            } else {
                this.drawTexturedModalRect(xPosition, yPosition, uLoc, vLoc, width, height);
            }
            int renderColor = color;
            if (!enabled) {
                renderColor = -6250336;
            } else if (isHovering) {
                renderColor = hoverColor;
            }
            fr.drawString(displayString, xPosition + (width - fr.getStringWidth(displayString)) / 2, yPosition + (height - 7) / 2, renderColor);
        }
    }
}
