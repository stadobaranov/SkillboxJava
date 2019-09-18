import java.io.File;
import java.util.regex.Pattern;

public class ImageResizing {
    private static final Pattern imageFilePattern = Pattern.compile("^.+\\.(gif|png|jpg|jpeg)$");

    private final String srcPath;
    private final String dstPath;
    private final int cores;
    private final ImageTransformer transformer;

    public ImageResizing(String srcPath, String dstPath, int cores, ImageTransformer transformer) {
        this.srcPath = srcPath;
        this.dstPath = dstPath;
        this.cores = cores;
        this.transformer = transformer;
    }

    public void run() {
        File srcDirectory = new File(srcPath);

        if(!srcDirectory.exists() || !srcDirectory.isDirectory()) {
            System.out.printf("Путь \"%s\" не является папкой", srcPath);
            return;
        }

        File dstDirectory = new File(dstPath);

        if(dstDirectory.exists()) {
            if(!dstDirectory.isDirectory()) {
                System.out.printf("Путь \"%s\" не является папкой", dstPath);
                return;
            }
        }
        else if(!dstDirectory.mkdirs()) {
            System.out.printf("Не удалось создать папку \"%s\"", dstPath);
            return;
        }

        File files[] = srcDirectory.listFiles((dir, name) -> imageFilePattern.matcher(name).matches());

        if(files == null) {
            return;
        }

        int threads = Math.min(cores, files.length);
        int partLength = (files.length / threads) + (files.length % threads > 0? 1: 0);

        for(int i = 0; i < threads; i++) {
            int partOffset = partLength * i;

            Thread thread = new Thread(
                new ImageResizeTask(
                    files,
                    partOffset,
                    Math.min(partLength, files.length - partOffset),
                    dstDirectory,
                    transformer
                )
            );

            thread.start();
        }
    }

    public static void main(String... args) {
        ImageResizing app = new ImageResizing(
            "src/main/resources/src",
            "src/main/resources/dst",
            Runtime.getRuntime().availableProcessors(),
            new ImageTransformCascade(
                new ImageResizer(600, ImageResizer.InterpolationType.NEAREST_NEIGHBOR),
                new ImageResizer(300, ImageResizer.InterpolationType.BICUBIC)
            )
        );

        app.run();
    }
}
