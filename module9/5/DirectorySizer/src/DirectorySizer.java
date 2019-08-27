import java.io.File;
import java.util.Scanner;

public class DirectorySizer {
    public long getDirectorySize(String path) {
        File file = new File(path);

        if(!file.exists()) {
            throw new DirectoryNotFoundException(String.format("Путь \"%s\" не существует", path));
        }
        else if(!file.isDirectory()) {
            throw new DirectoryNotFoundException(String.format("Путь \"%s\" не является папкой", path));
        }

        return calculateFileSize(file);
    }

    private long calculateFileSize(File file) {
        long bytes = 0;

        if(file.isDirectory()) {
            File children[] = file.listFiles();

            if(children != null) {
                for(File child: children) {
                    bytes += calculateFileSize(child);
                }
            }
        }
        else {
            bytes = file.length();
        }

        return bytes;
    }

    public static void main(String... args) {
        DirectorySizer directorySizer = new DirectorySizer();
        Scanner scanner = new Scanner(System.in);

        for(;;) {
            System.out.print("Введите путь папки: ");
            String path = scanner.nextLine();

            long bytes;

            try {
                bytes = directorySizer.getDirectorySize(path);
            }
            catch(DirectoryNotFoundException exception) {
                System.out.println(exception.getMessage());
                continue;
            }

            System.out.printf("Размер составляет: %s%n", SizeUnit.format(bytes));
        }
    }
}
