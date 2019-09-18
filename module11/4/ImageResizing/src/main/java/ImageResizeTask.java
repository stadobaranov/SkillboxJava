import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageResizeTask implements Runnable {
    private final File imageFiles[];
    private final int imageFilesOffset;
    private final int imageFilesLength;
    private final File dstDirectory;
    private final ImageTransformer transformer;

    public ImageResizeTask(
            File[] imageFiles, int imageFilesOffset, int imageFilesLength,
            File dstDirectory, ImageTransformer transformer) {
        this.imageFiles = imageFiles;
        this.imageFilesOffset = imageFilesOffset;
        this.imageFilesLength = imageFilesLength;
        this.dstDirectory = dstDirectory;
        this.transformer = transformer;
    }

    @Override
    public void run() {
        for(int i = imageFilesOffset, e = imageFilesOffset + imageFilesLength; i < e; i++) {
            File srcImageFile = imageFiles[i];
            File dstImageFile = new File(dstDirectory, srcImageFile.getName());

            if(dstImageFile.exists()) {
                if(dstImageFile.isDirectory()) {
                    System.out.printf("Путь \"%s\" не является файлом", dstImageFile.getAbsolutePath());
                    continue;
                }
                else if(!dstImageFile.delete()) {
                    System.out.printf("Не удалось удалить файл \"%s\"", dstImageFile.getAbsolutePath());
                    continue;
                }
            }

            BufferedImage srcImage;

            try {
                srcImage = ImageIO.read(srcImageFile);
            }
            catch(IOException exception) {
                System.out.printf("Не удалось прочитать файл \"%s\"", srcImageFile.getAbsolutePath());
                continue;
            }

            BufferedImage dstImage = transformer.transform(srcImage);
            String dstImageFileName = dstImageFile.getName();

            try {
                ImageIO.write(
                    dstImage,
                    dstImageFileName.substring(dstImageFileName.lastIndexOf(".") + 1),
                    dstImageFile
                );
            }
            catch(IOException exception) {
                System.out.printf("Не удалось записать файл \"%s\"", dstImageFile.getAbsolutePath());
            }
        }
    }
}
