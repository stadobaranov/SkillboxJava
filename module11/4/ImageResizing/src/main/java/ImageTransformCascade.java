import java.awt.image.BufferedImage;

public class ImageTransformCascade implements ImageTransformer {
    private final ImageTransformer transformers[];

    public ImageTransformCascade(ImageTransformer... transformers) {
        this.transformers = transformers;
    }

    @Override
    public BufferedImage transform(BufferedImage src) {
        BufferedImage image = src;

        for(ImageTransformer transformer: transformers) {
            image = transformer.transform(image);
        }

        return image;
    }
}
