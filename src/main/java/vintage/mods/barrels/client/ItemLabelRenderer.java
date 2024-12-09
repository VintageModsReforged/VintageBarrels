package vintage.mods.barrels.client;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import vintage.mods.barrels.Refs;

public class ItemLabelRenderer implements IItemRenderer {
    private ModelLabel labelModel = new ModelLabel();

    public ItemLabelRenderer() {}

    public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
        return true;
    }

    public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
        return true;
    }

    public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
        int metaTest = item.getItemDamage();
        switch (type) {
            case ENTITY:
                this.renderWoodLabel(0.0F, 0.0F, 0.1F, metaTest);
                break;
            case EQUIPPED:
                this.renderWoodLabel(0.6F, 1.0F, 0.8F, metaTest);
                break;
            case EQUIPPED_FIRST_PERSON:
                this.renderWoodLabel(0.6F, 1.0F, 0.8F, metaTest);
                break;
            case INVENTORY:
                this.renderWoodLabel(0.8F, 0.66F, 0.76F, metaTest);
        }

    }

    private void renderWoodLabel(float i, float j, float k, int metaData) {
        float scale = 0.083333336F;
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(this.getModelTexture(metaData));
        GL11.glPushMatrix();
        GL11.glTranslatef(i, j, k);
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        this.labelModel.renderLabel(scale);
        GL11.glPopMatrix();
    }

    public String getModelTexture(int metadata) {
        switch (metadata) {
            case 0:
                return Refs.WOODLABEL0_PNG;
            case 1:
                return Refs.WOODLABEL1_PNG;
            case 2:
                return Refs.WOODLABEL2_PNG;
            case 3:
                return Refs.WOODLABEL3_PNG;
            default:
                return Refs.WOODLABEL0_PNG;
        }
    }
}
