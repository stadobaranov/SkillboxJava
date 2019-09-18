import java.awt.image.BufferedImage;

public interface ImageTransformer {
    public abstract BufferedImage transform(BufferedImage src);
}
