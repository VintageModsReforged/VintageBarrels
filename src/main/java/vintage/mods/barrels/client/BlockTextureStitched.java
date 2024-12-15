package vintage.mods.barrels.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.vintage.core.VintageCore;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureStitched;
import net.minecraft.client.texturepacks.ITexturePack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

@SideOnly(Side.CLIENT)
public class BlockTextureStitched extends TextureStitched {

    private BufferedImage comparisonImage;
    private TextureStitched mappedTexture;
    private static Map<String, BufferedImage> cachedImages = new HashMap<String, BufferedImage>();
    private static Map<Integer, List<BlockTextureStitched>> existingTextures = new HashMap<Integer, List<BlockTextureStitched>>();

    public BlockTextureStitched(String name) {
        super(name);
    }

    @Override
    public void copyFrom(TextureStitched textureStitched) {
        if (textureStitched.getIconName().equals("missingno") && this.mappedTexture != null) {
            super.copyFrom(this.mappedTexture);
        } else {
            super.copyFrom(textureStitched);
        }
    }

    @Override
    public boolean loadTexture(TextureManager manager, ITexturePack texturepack, String name, String fileName, BufferedImage image, ArrayList textures) {
        int extStart = fileName.lastIndexOf(46);
        int indexStart = fileName.lastIndexOf(46, extStart - 1);
        String textureFile = "/" + fileName.substring(0, indexStart) + ".png";
        int index = Integer.parseInt(fileName.substring(indexStart + 1, extStart));
        BufferedImage bufferedImage = (BufferedImage)cachedImages.get(textureFile);
        if (bufferedImage == null) {
            try {
                bufferedImage = ImageIO.read(texturepack.getResourceAsStream(textureFile));
                cachedImages.put(textureFile, bufferedImage);
            } catch (IOException var20) {
                VintageCore.LOGGER.warning("Can't read texture " + textureFile);
                return false;
            }
        }

        int size = bufferedImage.getHeight();
        int count = bufferedImage.getWidth() / size;
        if (count != 1 && count != 6 && count != 12) {
            if (count != 2) {
                VintageCore.LOGGER.warning("Texture " + textureFile + " is not properly sized!");
                return false;
            }

            index /= 6;
        } else {
            index %= count;
        }

        this.comparisonImage = bufferedImage.getSubimage(index * size, 0, size, size);
        int[] rgbaData = new int[size * size];
        this.comparisonImage.getRGB(0, 0, size, size, rgbaData, 0, size);
        int hash = Arrays.hashCode(rgbaData);
        List<BlockTextureStitched> matchingTextures = existingTextures.get(hash);
        if (matchingTextures != null) {
            int[] rgbaData2 = new int[size * size];

            for(BlockTextureStitched matchingTexture : matchingTextures) {
                if (matchingTexture.comparisonImage.getWidth() == size) {
                    matchingTexture.comparisonImage.getRGB(0, 0, size, size, rgbaData2, 0, size);
                    if (Arrays.equals(rgbaData, rgbaData2)) {
                        this.mappedTexture = matchingTexture;
                        return true;
                    }
                }
            }

            matchingTextures.add(this);
        } else {
            matchingTextures = new ArrayList<BlockTextureStitched>();
            matchingTextures.add(this);
            existingTextures.put(hash, matchingTextures);
        }

        Texture texture = new Texture(name, 2, size, size, 10496, 6408, 9728, 9728, this.comparisonImage);
        textures.add(texture);
        return true;
    }

    public static void onPostStitch() {
        for(List<BlockTextureStitched> textures : existingTextures.values()) {
            for(BlockTextureStitched texture : textures) {
                texture.comparisonImage = null;
            }
        }
        cachedImages.clear();
        existingTextures.clear();
    }
}
