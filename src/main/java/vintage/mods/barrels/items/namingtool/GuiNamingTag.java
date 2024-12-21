package vintage.mods.barrels.items.namingtool;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.vintage.core.platform.lang.Translator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import vintage.mods.barrels.Refs;
import vintage.mods.barrels.utils.GuiCustomButton;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiNamingTag extends GuiScreen {

    private String NEW_NAME = StatCollector.translateToLocal("gui.namingTool");
    private GuiTextField nameField;
    private GuiButton ok;
    private GuiButton cancel;
    protected int xSize = 139;
    protected int ySize = 57;
    protected int guiLeft;
    protected int guiTop;

    @Override
    public void updateScreen() {
        this.nameField.updateCursorCounter();
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(false);
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        this.buttonList.clear();
        this.ok = new GuiCustomButton(0, this.guiLeft + this.xSize - 107, this.guiTop + 38, 26, 15, 0, 57, 0, 72, StatCollector.translateToLocal("gui.namingTool.ok"), 4210752, 16777120);
        this.ok.enabled = false;
        this.cancel = new GuiCustomButton(1, this.guiLeft + this.xSize - 77, this.guiTop + 38, 45, 15, 26, 57, 26, 72, StatCollector.translateToLocal("gui.namingTool.cancel"), 4210752, 16777120);
        this.buttonList.add(this.ok);
        this.buttonList.add(this.cancel);
        this.nameField = new GuiTextField(this.fontRenderer, this.guiLeft + 7, this.guiTop + 25, 125, 10);
        this.nameField.setFocused(true);
        this.nameField.setMaxStringLength(32);
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) {
        if (guibutton.enabled) {
            switch (guibutton.id) {
                case 0:
                    String name = this.nameField.getText().trim();
                    sendNameToServer(name);
                case 1:
                    this.mc.setIngameFocus();
                default:
            }
        }
    }

    @Override
    protected void keyTyped(char c, int i) {
        this.nameField.textboxKeyTyped(c, i);
        ((GuiButton)this.buttonList.get(0)).enabled = this.nameField.getText().trim().length() > 0;
        if (c == '\n' || c == '\r') {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }

        if (Integer.valueOf(c) == 27) {
            this.actionPerformed((GuiButton)this.buttonList.get(1));
        }
    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        this.nameField.mouseClicked(i, j, k);
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        this.drawDefaultBackground();
        this.drawGuiBackground();
        this.fontRenderer.drawString(Translator.format("item.namingTool.name"), this.guiLeft + 6, this.guiTop + 6, 0);
        this.fontRenderer.drawString(this.NEW_NAME, this.guiLeft + 6, this.guiTop + 16, 4210752);
        this.nameField.drawTextBox();
        super.drawScreen(i, j, f);
    }

    protected void drawGuiBackground() {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/mods/vintagebarrels/textures/gui/namingTool.png");
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 139, 57);
    }

    public static void sendNameToServer(String name) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);
        try {
            dataStream.writeByte(1);
            dataStream.writeUTF(name.trim());
            PacketDispatcher.sendPacketToServer(PacketDispatcher.getPacket(Refs.ID, byteStream.toByteArray()));
        } catch (IOException var4) {
            FMLLog.warning("[Naming Tool] Failed to send new name to server.");
        }

    }
}
