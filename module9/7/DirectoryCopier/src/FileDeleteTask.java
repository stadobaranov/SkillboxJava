import java.io.File;

public class FileDeleteTask implements Runnable {
    private final File file;

    public FileDeleteTask(File file) {
        this.file = file;
    }

    @Override
    public void run() {
        DirectoryCopyUtils.delete(file);
    }
}
