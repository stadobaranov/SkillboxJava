import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ImageResizer implements ImageTransformer {
    public static enum InterpolationType {
        NEAREST_NEIGHBOR(AffineTransformOp.TYPE_NEAREST_NEIGHBOR),
        BICUBIC(AffineTransformOp.TYPE_BICUBIC);

        private final int value;

        private InterpolationType(int value) {
            this.value = value;
        }
    }

    private final int width;
    private final InterpolationType interpolationType;

    public ImageResizer(int width, InterpolationType interpolationType) {
        this.width = width;
        this.interpolationType = interpolationType;
    }

    @Override
    public BufferedImage transform(BufferedImage src) {
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();

        int dstWidth = width;
        int dstHeight = (int)(((double)dstWidth / srcWidth) * srcHeight);
        BufferedImage dst = new BufferedImage(dstWidth, dstHeight, src.getType());

        AffineTransform transform = new AffineTransform();
        transform.scale((double)dstWidth / srcWidth, (double)dstHeight / srcHeight);

        AffineTransformOp transformOp = new AffineTransformOp(transform, interpolationType.value);
        transformOp.filter(src, dst);

        return dst;
    }
}
