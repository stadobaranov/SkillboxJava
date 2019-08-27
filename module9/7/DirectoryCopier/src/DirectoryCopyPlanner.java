import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryCopyPlanner {
    private final ConflictResolver conflictResolver;
    private List<Runnable> tasks;

    public DirectoryCopyPlanner(ConflictResolver conflictResolver) {
        this.conflictResolver = conflictResolver;
    }

    private void scanDirectory(File src, File dst) {
        File srcChildren[] = src.listFiles();

        if(srcChildren != null) {
            String dstPath = dst.getAbsolutePath();

            for(File srcChild: srcChildren) {
                File dstChild = new File(dstPath + File.separator + srcChild.getName());

                if(srcChild.isDirectory()) {
                    boolean mkdir = true;

                    if(dstChild.exists()) {
                        if(dstChild.isDirectory()) {
                            mkdir = false;
                        }
                        else if(!conflictResolver.resolveFileToDirectoryOverwriting(dstChild)) {
                            continue;
                        }
                        else {
                            tasks.add(new FileDeleteTask(dstChild));
                        }
                    }

                    if(mkdir)
                        tasks.add(new DirectoryMakeTask(dstChild));

                    scanDirectory(srcChild, dstChild);
                }
                else {
                    if(dstChild.exists()) {
                        if(dstChild.isDirectory()) {
                            if(!conflictResolver.resolveDirectoryToFileOverwriting(dstChild)) {
                                continue;
                            }

                            tasks.add(new DirectoryDeleteTask(dstChild));
                        }
                        else {
                            if(!conflictResolver.resolveFileOverwriting(dstChild)) {
                                continue;
                            }

                            tasks.add(new FileDeleteTask(dstChild));
                        }
                    }

                    tasks.add(new FileCopyTask(srcChild, dstChild));
                }
            }
        }
    }

    public List<Runnable> plan(File src, File dst) {
        tasks = new ArrayList<>();
        scanDirectory(src, dst);
        return tasks;
    }
}
