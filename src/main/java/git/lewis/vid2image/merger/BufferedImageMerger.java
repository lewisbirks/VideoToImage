package git.lewis.vid2image.merger;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class BufferedImageMerger implements Merger<BufferedImage> {

    private static final BufferedImageMerger INSTANCE = new BufferedImageMerger();

    private BufferedImageMerger() {}

    public static BufferedImageMerger instance() {
        return INSTANCE;
    }

    @Override
    public BufferedImage merge(BufferedImage image1, BufferedImage image2) {
        if (image1 == null) {
            return image2;
        }

        if (image2 == null) {
            return image1;
        }

        if (image1.getHeight() != image2.getHeight() && image1.getWidth() != image2.getWidth()) {
            throw new IllegalArgumentException();
        }

        BufferedImage updated = deepCopy(image1);

        for (int i = 0; i < image1.getWidth(); i++) {
            for (int j = 0; j < image1.getHeight(); j++) {
                int rgbPixel1 = image1.getRGB(i, j);
                float pixel1 = calculatedLuminosity(rgbPixel1);

                int rgbPixel2 = image2.getRGB(i, j);
                float pixel2 = calculatedLuminosity(rgbPixel2);

                updated.setRGB(i, j, Float.compare(pixel1, pixel2) > 0 ? rgbPixel1 : rgbPixel2);
            }
        }

        return updated;
    }

    private float calculatedLuminosity(int rgbPixel) {
        int red   = (rgbPixel >>> 16) & 0xFF;
        int green = (rgbPixel >>>  8) & 0xFF;
        int blue  = (rgbPixel) & 0xFF;

        return (red * 0.2126f) + (green * 0.7152f) + (blue * 0.0722f);
    }

    private BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}
