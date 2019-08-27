import java.io.File;

public class DirectoryCopyUtils {
    public static void mkdirs(File directory) {
        if(!directory.mkdirs())
            throw new DirectoryCopyException(
                String.format("Не удалось создать папку \"%s\"", directory));
    }

    public static void delete(File file) {
        if(!file.delete())
            throw new DirectoryCopyException(
                String.format("Не удалось удалить \"%s\"", file.getAbsolutePath()));
    }
}
