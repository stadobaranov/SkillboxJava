import java.io.File;

public class DirectoryMakeTask implements Runnable {
    private final File directory;

    public DirectoryMakeTask(File directory) {
        this.directory = directory;
    }

    @Override
    public void run() {
        DirectoryCopyUtils.mkdirs(directory);
    }
}
