import java.io.File;
import java.util.Scanner;

public class DirectoryCopier {
    private final ConflictResolver conflictResolver;

    public DirectoryCopier(ConflictResolver conflictResolver) {
        this.conflictResolver = conflictResolver;
    }

    private static File getExistingDirectory(String path, ConflictResolver conflictResolver) {
        File directory = new File(path);

        if(!directory.exists()) {
            if(conflictResolver == null || !conflictResolver.resolveDirectoryMaking(directory)) {
                throw new DirectoryCopyException(String.format("Путь \"%s\" не существует", path));
            }

            DirectoryCopyUtils.mkdirs(directory);
        }

        if(!directory.isDirectory()) {
            throw new DirectoryCopyException(String.format("Путь \"%s\" не является пакой", path));
        }

        return directory;
    }

    public void copyDirectory(String srcPath, String dstPath) {
        File src = getExistingDirectory(srcPath, null);
        File dst = getExistingDirectory(dstPath, conflictResolver);

        if(src.equals(dst)) {
            throw new DirectoryCopyException("Исходный путь и путь назначения совпадают");
        }

        DirectoryCopyPlanner planner = new DirectoryCopyPlanner(conflictResolver);

        for(Runnable task: planner.plan(src, dst)) {
            task.run();
        }
    }

    public static void main(String... args) {
        Scanner scanner = new Scanner(System.in);
        ConflictResolver conflictResolver = new ConflictResolver(scanner);
        DirectoryCopier directoryCopier = new DirectoryCopier(conflictResolver);

        for(;;) {
            System.out.print("Путь исходной папки: ");
            String srcPath = scanner.nextLine();

            System.out.print("Путь папки назначения: ");
            String dstPath = scanner.nextLine();

            try {
                directoryCopier.copyDirectory(srcPath, dstPath);
            }
            catch(DirectoryCopyException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }
}
