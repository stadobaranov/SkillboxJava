import java.io.IOException;

public class HdfsExample {
    public static void main(String... args) throws IOException {
        try(FileAccess fileAccess = new FileAccess("hdfs://68b0483a60ed:8020/")) {
            fileAccess.create("/emptyFile.txt");
            fileAccess.create("/files/test.txt");
            fileAccess.append("/files/test.txt", "1\n2\n3");
            System.out.println(fileAccess.read("/files/test.txt"));
            System.out.println(fileAccess.isDirectory("/files"));
            System.out.println(fileAccess.isDirectory("/files/test.txt"));

            for(String fName: fileAccess.list("/")) {
                System.out.println(fName);
            }

            fileAccess.delete("/emptyFile.txt");
            fileAccess.delete("/files");
        }
    }
}
