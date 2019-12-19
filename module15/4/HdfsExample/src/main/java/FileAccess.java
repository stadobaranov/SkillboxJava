import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileAccess implements AutoCloseable {
    private final FileSystem fileSystem;
    private boolean closed;

    /**
     * Initializes the class, using rootPath as "/" directory
     *
     * @param rootPath - the path to the root of HDFS,
     * for example, hdfs://localhost:32771
     */
    public FileAccess(String rootPath) throws IOException {
        fileSystem = createFileSystem(rootPath);
    }

    private static FileSystem createFileSystem(String rootPath) throws IOException {
        System.setProperty("HADOOP_USER_NAME", "root");

        Configuration config = new Configuration();
        config.set("dfs.client.use.datanode.hostname", "true");

        try {
            return FileSystem.get(new URI(rootPath), config);
        }
        catch(URISyntaxException exception) {
            throw new IOException("Некорректный корневой путь HDFS", exception);
        }
    }

    /**
     * Creates empty file or directory
     *
     * @param path
     */
    public void create(String path) throws IOException {
        checkClosed();

        if(pathIsDirectory(path))
            createNewDirectory(path);
        else
            createNewFile(path);
    }

    private boolean pathIsDirectory(String path) {
        int slashPos = path.lastIndexOf('/');
        return slashPos < 0 || !path.substring(slashPos).contains(".");
    }

    private void createNewDirectory(String path) throws IOException {
        Path dPath = new Path(path);

        if(fileSystem.exists(dPath)) {
            if(!fileSystem.isDirectory(dPath)) {
                fileSystem.delete(dPath, true);
            }
        }
        else
            fileSystem.mkdirs(dPath);
    }

    private void createNewFile(String path) throws IOException {
        Path fPath = new Path(path);

        if(fileSystem.exists(fPath)) {
            fileSystem.delete(fPath, true);
        }

        fileSystem.createNewFile(fPath);
    }

    /**
     * Appends content to the file
     *
     * @param path
     * @param content
     */
    public void append(String path, String content) throws IOException {
        checkClosed();

        try(BufferedWriter w = new BufferedWriter(new OutputStreamWriter(fileSystem.append(new Path(path))))) {
            w.write(content);
        }
    }

    /**
     * Returns content of the file
     *
     * @param path
     * @return
     */
    public String read(String path) throws IOException {
        checkClosed();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(fileSystem.open(new Path(path))))) {
            StringBuilder content = new StringBuilder();

            for(;;) {
                int ch = reader.read();

                if(ch < 0) {
                    return content.toString();
                }

                content.append((char)ch);
            }
        }
    }

    /**
     * Deletes file or directory
     *
     * @param path
     */
    public void delete(String path) throws IOException {
        checkClosed();
        fileSystem.delete(new Path(path), true);
    }

    /**
     * Checks, is the "path" is directory or file
     *
     * @param path
     * @return
     */
    public boolean isDirectory(String path) throws IOException {
        checkClosed();
        return fileSystem.isDirectory(new Path(path));
    }

    /**
     * Return the list of files and subdirectories on any directory
     *
     * @param path
     * @return
     */
    public List<String> list(String path) throws IOException {
        checkClosed();
        Path directory = new Path(path);

        if(!fileSystem.isDirectory(directory)) {
            return Collections.emptyList();
        }

        ArrayList<String> children = new ArrayList<>();
        RemoteIterator<LocatedFileStatus> iterator = fileSystem.listLocatedStatus(directory);

        while(iterator.hasNext()) {
            children.add(iterator.next().getPath().getName());
        }

        return children;
    }

    private void checkClosed() {
        if(closed) {
            throw new IllegalStateException("Доступ к HDFS уже закрыт");
        }
    }

    @Override
    public void close() {
        if(!closed) {
            try {
                fileSystem.close();
            }
            catch(IOException exception) {
                System.out.println("При закрытии HDFS возникло исключение: ");
                exception.printStackTrace(System.out);
            }

            closed = true;
        }
    }
}
