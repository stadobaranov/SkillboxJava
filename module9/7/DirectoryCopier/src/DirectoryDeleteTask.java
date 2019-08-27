import java.io.File;

public class DirectoryDeleteTask implements Runnable {
    private final File directory;

    public DirectoryDeleteTask(File directory) {
        this.directory = directory;
    }

    @Override
    public void run() {
        deleteSubFiles(directory);
        DirectoryCopyUtils.delete(directory);
    }

    private static void deleteSubFiles(File directory) {
        File children[] = directory.listFiles();

        if(children != null) {
            for(File child: children) {
                if(child.isDirectory())
                    deleteSubFiles(child);

                DirectoryCopyUtils.delete(child);
            }
        }
    }
}
